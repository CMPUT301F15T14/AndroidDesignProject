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

// class Inventory is designed to store a collection (list) of Game objects and associate them with users.

public class Inventory implements AppObservable {
    private volatile transient ArrayList<AppObserver> observers;

    private ArrayList<Game> gameCollections;

    /**
     * Default constructor
     */
    public Inventory(){
        observers = new ArrayList<AppObserver>();
        this.gameCollections=new ArrayList<Game>();
    }

    /**
     * Add Games to gameCollections. Must be called from InventoryController.
     * @param game
     */
    public void add(Game game){
        gameCollections.add(game);
        this.notifyAllObservers();
    }

    /**
     * Remove Games to gameCollections. Must be called from InventoryController.
     * @param game
     */
    public void remove(Game game){
        gameCollections.remove(game);
        this.notifyAllObservers();
    }

    /**
     * Clear all the objects stored in gameCollections. Must be called from InventoryController.
     */
    public void clear(){
        gameCollections.clear();
    }

    /**
     * Determine if the object is in gameCollection.
     * @param game
     * @return true if gameCollections contain the input Game object.
     */
    public boolean contains(Game game){
        return gameCollections.contains(game);
    }

    /**
     * Set Owner as User.
     * @param owner
     */

    /**
     * Getter of gameCollections.
     * @return gameCollections.
     */
    public ArrayList<Game> getAllGames() {
        return gameCollections;
    }

    public void addObserver(AppObserver observer) {
        observers.add(observer);
    }

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
