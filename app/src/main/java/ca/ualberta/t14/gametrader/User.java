package ca.ualberta.t14.gametrader;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jjohnston on 10/30/15.
 *
 * Represents the phone user (one for each phone accessing the app) and their related information (profile)
 */
public class User implements Serializable, AppObservable {
    public User() {
        // if a user file already exists simply load it from the file
        // otherwise, create a new user file and prompt the user to create a user name
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    String userName;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    String email;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    String address;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    String phoneNumber;

    private String androidID; // used as a unique identifier http://stackoverflow.com/questions/2785485/is-there-a-unique-android-device-id

    ArrayList<User> friendList;
    ArrayList<User> pendingFriendList;

    public void addObserver(AppObserver observer) {

    }
}
