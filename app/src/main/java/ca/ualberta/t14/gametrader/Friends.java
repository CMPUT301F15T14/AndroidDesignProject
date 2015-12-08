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
