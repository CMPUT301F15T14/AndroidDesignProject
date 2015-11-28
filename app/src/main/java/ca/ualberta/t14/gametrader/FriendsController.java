package ca.ualberta.t14.gametrader;

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
    PriorityQueue<String> MostRecentUpdates = new PriorityQueue<String>();

    FriendsController(Friends friends) {
        model = friends;
    }

    /**
     * Adds another user in the network as a friend. We will see updates from them and be able
     * to browse their publicly listed items.
     * @param friend is the User we wish to add.
     */
    void AddFriend(User friend) {
        String id = friend.getAndroidID();
        for(User existingFriend : model.GetFriends()) {
            if( existingFriend.getAndroidID() == id ) {
                throw new RuntimeException("Tried to add a friend already on our list. The UI should not allow this.");
            }
        }
        model.AddFriend(friend);
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
                RemoveFriend(friend);
                AddFriend(serverFriend);
            }
        }
    }
}
