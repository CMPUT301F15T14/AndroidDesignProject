/*  Copyright (C) 2015  Aaron Arnason, Tianyu Hu, Michael Xi, Ryan Satyabrata, Joel Johnston, Suzanne Boulet, Ng Yuen Tung(Brigitte)

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*/

package ca.ualberta.t14.gametrader;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.impl.conn.SingleClientConnManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observer;

/**
 * Created by jjohnston on 10/30/15.
 *
 *  Handles modifications to profile information within the User class (any publicly visible data
 *  such as contact info).
 */

public class ProfileController implements AppObserver, AppObservable {
    public static String MainUser = "MainUserProfile";
    private static Context context;
    private NetworkController network;
    private ArrayList<User> searchResults;
    private Boolean canAdd;

    public ProfileController(User model, Context context) {
        this.context = context;
        this.model = model;
        network=new NetworkController(context);
        canAdd = Boolean.FALSE;
        observers = new ArrayList<AppObserver>();
    }


    private ArrayList<AppObserver> observers;

    public void addObserver(AppObserver observer){ observers.add(observer);}

    public void deleteObserver(AppObserver observer){observers.remove(observer);}

    public void notifyAllObservers(){
        for(AppObserver o : observers) {
            o.appNotify(this);
        }
    }

    private User model;


    /**
     * Saves any changes to the profile information. Used by EditProfileActivity upon clicking save.
     * @param userName is a String containing the updated user name.
     * @param email is a String containing the updated email.
     * @param address is a String containing the updated address.
     * @param phoneNumber is a String containing the updated phone number.
     */
    void SaveProfileEdits(String userName, String email, String address, String phoneNumber) {
        UserSingleton.getInstance().getUser().setUserName(userName);
        UserSingleton.getInstance().getUser().setEmail(email);
        UserSingleton.getInstance().getUser().setAddress(address);
        UserSingleton.getInstance().getUser().setPhoneNumber(phoneNumber);
        UserSingleton.getInstance().getUser().saveJson(MainUser, this.context);
        UserSingleton.getInstance().getUser().notifyAllObservers();
    }

    public boolean profileExist(String inputName){
        searchResults = new ArrayList<User>();
        new searchFriends().execute(inputName);
        if (!searchResults.isEmpty()){
            return true;
        }else {
            System.out.print("Checking if profile exists\n");
            return false;
        }
    }

    public Boolean getcanAdd() {
        return canAdd;
    }


    public void appNotify(AppObservable observable) {
        this.notifyAllObservers();
    }

    private class searchFriends extends AsyncTask<String, Integer, ArrayList<User>> implements AppObservable {

        private ArrayList<AppObserver> obs;

        searchFriends() {
            obs = new ArrayList<AppObserver>();
        }

        public void addObserver(AppObserver observer){ obs.add(observer);}

        public void deleteObserver(AppObserver observer){obs.remove(observer);}

        public void notifyAllObservers(){
            for(AppObserver o : obs) {
                o.appNotify(this);
            }
        }


        protected ArrayList<User> doInBackground(String... params) {
            String userName = params[0];
            ProfileController pc = ProfileController.this;
            addObserver(pc);

            ArrayList<User> results = new ArrayList<User>();
            try{
                results = network.searchByUserName(userName);
                User toRemove = null;
                for(User each : results) {
                    // User is not changing his profile name, self don't count
                    User deviceUser = UserSingleton.getInstance().getUser();
                    if(each.getAndroidID().compareTo(deviceUser.getAndroidID()) == 0) {
                        toRemove = each;
                        break;
                    }
                }
                if(toRemove != null)
                    results.remove(toRemove);
            }
            catch(IOException e) {
                e.printStackTrace();
            }

            return results;
        }

        protected void onPostExecute(ArrayList<User> result) {
            super.onPostExecute(result);
            canAdd = result.isEmpty();
            notifyAllObservers();
        }
    }
}
