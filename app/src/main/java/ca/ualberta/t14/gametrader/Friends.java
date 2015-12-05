package ca.ualberta.t14.gametrader;

import java.util.ArrayList;
import java.util.Observable;

/**
 * A "friend" is a user in the network that we want to track. By adding a friend, we can view
 * their publicly shared items, offer trades, and track their updates.
 * Created by jjohnsto on 11/28/15.
 */
public class Friends extends FileIO implements AppObservable {
    private transient ArrayList<AppObserver> observers = new ArrayList<AppObserver>();

    @Override
    public void addObserver(AppObserver observer) {
        observers.add(observer);
    }

    @Override
    public void deleteObserver(AppObserver o) {
        observers.remove(o);
    }


    public void notifyAllObservers() {
        for(AppObserver obs : observers) {
            obs.appNotify(this);
        }
    }

    private ArrayList<User> friendList = new ArrayList<User>();

    public void AddFriend(User friend) {
        friendList.add(friend);
        notifyAllObservers();
    }

    public void RemoveFriend(User friend) {
        friendList.remove(friend);
    }

    public void setFriends(ArrayList<User> friendsList) {
        this.friendList = friendsList;
    }

    public ArrayList<User> GetFriends() { return friendList; }
}
