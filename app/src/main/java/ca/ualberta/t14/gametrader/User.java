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

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Created by jjohnston on 10/30/15.
 *
 * Represents a user (one for each phone accessing the app) and their related information (profile).
 * Also contains an inventory which lists the games owned by that user.
 *
 */

public class User extends FileIO implements Serializable, AppObservable {

    private volatile ArrayList<AppObserver> observers;
    private Inventory inventory;

    ArrayList<User> friendList;
    ArrayList<User> pendingFriendList;

    public User() {
        // if a user file already exists simply load it from the file
        // otherwise, create a new user file and prompt the user to create a user name
        observers = new ArrayList<AppObserver>();
        inventory = new Inventory();
    }

    /**
     * Get the user's name. Used by ProfileActivity to obtain a String for the view to render.
     * @return the User's name in string format.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Used by ProfileController to change the user's name
     * @param userName the String value userName should be set to.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String userName;

    /**
     * Get the user's email address.
     * @return the user's email address in String format.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Used by ProfileController to change the user's email address
     * @param email a string representing the new email address.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    private String email;

    /**
     * Get the user's address.
     * @return a String containing the user's address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Used by ProfileController to change the user's home address
     * @param address a String representing the new address.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    private String address;

    /**
     * Returns the user's phone number.
     * @return a String containing the user's phone number.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Used by ProfileController to change the user's phone number
     * @param phoneNumber a String containing the new phone number.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    private String phoneNumber;

    public String getInstallationId() {
        return installationId;
    }

    public void setInstallationId(String installationId) {
        this.installationId = installationId;
    }

    // used as a unique identifier http://stackoverflow.com/questions/2785485/is-there-a-unique-android-device-id
    private String installationId;

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }


    /**
     * Adds a class to the observer's list
     * @param observer The observer to be added which wants to be notified on the update.
     */
    public void addObserver(AppObserver observer) {
        observers.add(observer);
    }

    @Override
    public void deleteObserver(AppObserver o) {
        observers.remove(o);
    }


    /**
     * Called to notify all observers that the model has been updated.
     */
    public void notifyAllObservers() {
        for(AppObserver obs : observers) {
            obs.appNotify(this);
        }
    }
}
