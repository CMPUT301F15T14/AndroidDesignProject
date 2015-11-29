package ca.ualberta.t14.gametrader;

import java.util.ArrayList;

/**
 * A "friend" is a user in the network that we want to track. By adding a friend, we can view
 * their publicly shared items, offer trades, and track their updates.
 * Created by jjohnsto on 11/28/15.
 */
public class Friends {
    ArrayList<User> friendList = new ArrayList<User>();

    public void AddFriend(User friend) {
        friendList.add(friend);
    }

    public void RemoveFriend(User friend) {
        friendList.remove(friend);
    }

    public ArrayList<User> GetFriends() { return friendList; }
}
