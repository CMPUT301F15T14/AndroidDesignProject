package ca.ualberta.t14.gametrader;

import java.util.ArrayList;

/**
 * Created by jjohnsto on 11/28/15.
 */
public class Friends {
    ArrayList<User> friendList;

    public void AddFriend(User friend) {
        friendList.add(friend);
    }

    public void RemoveFriend(User friend) {
        friendList.remove(friend);
    }

    public ArrayList<User> GetFriends() { return friendList; }
}
