/*
 * Copyright (C) 2015  Aaron Arnason, Tianyu Hu, Michael Xi, Ryan Satyabrata, Joel Johnston, Suzanne Boulet, Ng Yuen Tung(Brigitte)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package ca.ualberta.t14.gametrader;

import android.content.Context;
import android.os.AsyncTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.PriorityQueue;

/**
 * Created by jjohnsto on 11/28/15.
 */
public class FriendsController {
    Friends model;
    transient Context context;

    public PriorityQueue<String> getMostRecentUpdates() {
        return MostRecentUpdates;
    }

    public PriorityQueue<String> MostRecentUpdates = new PriorityQueue<String>();

    FriendsController(Friends friends, Context context) {
        model = friends;
        this.context = context;
    }

    public void WriteFriends(Context context) {
        model.saveJson("myFriends", context);
    }

    public void LoadFriends(Context context) {
        try {
            model = (Friends) model.loadJson("myFriends", context);
            UserSingleton.getInstance().getUser().getFriends().setFriends(model.GetFriends());

        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Talks to the nextwork in another thread when adding a friend.
     */
    private class AddFriendThread extends AsyncTask<String, Integer, User> {
        protected User doInBackground(String... params) {
            NetworkController nc = new NetworkController(context);

            String userName = params[0];

            try{
                ArrayList<User> results = nc.searchByUserName(userName);

                if(!results.isEmpty()) {
                    return results.get(0);
                }
            }
            catch(IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(User result) {
            super.onPostExecute(result);
            if(result != null) {
                for(User friend : model.GetFriends()){
                    if(friend.getAndroidID() == result.getAndroidID()){ // don't add an existing friend
                        return;
                    }
                }
                System.out.println("Adding: " + result.getUserName());
                UserSingleton.getInstance().getUser().getFriends().AddFriend(result);
                WriteFriends(context);
            }
        }
    }

    /**
     * Searches the sever for a User with the given user name. If found (and not already in the frineds
     * list) they are added as a friend.
     * @param name
     */
    public void AddFriend(String name) {
        new AddFriendThread().execute(name);
    }

    /**
     * Removes an existing friend.
     * @param friend is the User we wish to follow
     */
    void RemoveFriend(User friend) {
        String id = friend.getAndroidID();
        for(User existingFriend : model.GetFriends()) {
            if( existingFriend.getAndroidID() == id ) {
                UserSingleton.getInstance().getUser().getFriends().RemoveFriend(existingFriend);
                WriteFriends(context);
                return;
            }
        }
        throw new RuntimeException("Tried to remove a friend not on our list. The UI should not allow this.");
    }

    /**
     * Querries for updates from friends in our list. The user should be notified of any changes made
     * by his friends.
     */
    void UpdateFriends(){
        NetworkController nc = new NetworkController(context);
        String date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        ArrayList<User> friendsToCheck = new ArrayList<User>(model.GetFriends());
        for(User friend : friendsToCheck) {
            User serverFriend = nc.loadUser(friend.getAndroidID());
            if(!friend.equals(serverFriend)) { // if the server copy does not match our local copy
                // figure out what was updated, multiple updates at once too.
                if(friend.getInventory() != serverFriend.getInventory()) {
                    MostRecentUpdates.add(date + " " + friend.getUserName() + " updated their inventory.");
                }
                else if(friend.getUserName() != serverFriend.getUserName()) {
                    MostRecentUpdates.add(date + " " + friend.getUserName() + " updated their user name to: " + serverFriend.getUserName());
                }
                else if(friend.getAddress() != serverFriend.getAddress()) {
                    MostRecentUpdates.add(date + " " + friend.getUserName() + " updated their address to: " + serverFriend.getAddress());
                }
                else if (friend.getEmail() != serverFriend.getEmail()) {
                    MostRecentUpdates.add(date + " " + friend.getUserName() + " updated their email to: " + serverFriend.getEmail());
                }
                else if(friend.getPhoneNumber() != serverFriend.getPhoneNumber()) {
                    MostRecentUpdates.add(date + " " + friend.getUserName() + " updated their phone to: " + serverFriend.getPhoneNumber());
                }
                else {
                    // no updates -- do nothing
                    return;
                }
                // update the our copy
                UserSingleton.getInstance().getUser().getFriends().RemoveFriend(friend);
                UserSingleton.getInstance().getUser().getFriends().AddFriend(serverFriend);
                WriteFriends(context);
            }
        }
    }
}
