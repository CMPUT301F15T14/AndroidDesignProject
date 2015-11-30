package ca.ualberta.t14.gametrader;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by jjohnsto on 11/28/15.
 */
public class FriendsController {
    Friends model;

    public PriorityQueue<String> getMostRecentUpdates() {
        return MostRecentUpdates;
    }

    public PriorityQueue<String> MostRecentUpdates = new PriorityQueue<String>();

    FriendsController(Friends friends) {
        model = friends;
    }

    public void WriteFriends(Context context) {
        model.saveJson("myFriends", context);
    }

    public void LoadFriends(Context context) {
        try {
            model.loadJson("myFriends", context);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Talks to the nextwork in another thread when adding a friend.
     * @param friend is the User we wish to add.
     */
    private class AddFriendThread extends AsyncTask<String, Integer, User> {
        protected User doInBackground(String... params) {
            NetworkController nc = new NetworkController();

            String userName = params[0];

            try{
                ArrayList<User> results = nc.SearchByUserName(userName);

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
                model.AddFriend(result);
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
                model.RemoveFriend(existingFriend);
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
        NetworkController nc = new NetworkController();
        String date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        for(User friend : model.GetFriends()) {
            User serverFriend = nc.LoadUser(friend.getAndroidID());
            if(!friend.equals(serverFriend)) { // if the server copy does not match our local copy
                // figure out what was updated
                if(friend.getInventory() != serverFriend.getInventory()) {
                    MostRecentUpdates.add(date + " " + friend.getUserName() + " updated their inventory");
                }
                else if(friend.getUserName() != serverFriend.getUserName()) {
                    MostRecentUpdates.add(date + " " + friend.getUserName() + " updated their user name to " + serverFriend.getUserName());
                }
                else if(friend.getAddress() != serverFriend.getAddress()) {
                    MostRecentUpdates.add(date + " " + friend.getUserName() + " updated their address to" + serverFriend.getAddress());
                }
                else if(friend.getEmail() != serverFriend.getEmail()) {
                    MostRecentUpdates.add(date + " " + friend.getUserName() + " updated their email to" + serverFriend.getEmail());
                }
                else if(friend.getPhoneNumber() != serverFriend.getPhoneNumber()) {
                    MostRecentUpdates.add(date + " " + friend.getUserName() + " updated their phone to" + serverFriend.getPhoneNumber());
                }
                else { // no updates -- do nothing
                    return;
                }
                // update the our copy
                model.RemoveFriend(friend);
                model.AddFriend(serverFriend);
            }
        }
    }
}
