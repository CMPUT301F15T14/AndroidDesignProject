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

import android.content.Context;

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
     * Add Games to gameCollections. Must be called from InventoryListController.
     * @param game
     */
    public void add(Game game){
        gameCollections.add(game);
        this.notifyAllObservers();
    }

    /**
     * Remove Games to gameCollections. Must be called from InventoryListController.
     * @param game
     */
    public void remove(Game game, Context context){
        // Remove all associated images first.
        ArrayList<String> picsIds = game.getPictureIds();
        for(String each : picsIds) {
            PictureManager.removeFile(each, context);
        }
        gameCollections.remove(game);
        this.notifyAllObservers();
    }

    /**
     * Clear all the objects stored in gameCollections. Must be called from InventoryListController.
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

    public ArrayList<Game> getAllPublicGames() {
        ArrayList<Game> publicOnly = new ArrayList<Game>();
        for(Game each : gameCollections) {
            if(each.isShared()) {
                publicOnly.add(each);
            }
        }
        return publicOnly;
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

    /**
     * Returns the games in the inventory whose title contains the given search query
     * @param query the String to search for
     * @return a list of games containing the given string
     */
    public ArrayList<Game> Search(String query) {
        ArrayList<Game> result = new ArrayList<Game>();

        for(Game game : gameCollections) {
            if(game.getTitle().contains(query)){
                result.add(game);
            }
        }

        return result;
    }

    /**
     * Search the inventory by String and Platform
     * @param query the String to search
     * @param platform only results for this platform will be returned
     * @return an array list of Games containing the search results
     */
    public ArrayList<Game> Search(String query, Game.Platform platform) {
        ArrayList<Game> result = new ArrayList<Game>();

        for(Game game : gameCollections) {
            if(game.getTitle().contains(query) && game.getPlatform() == platform){
                result.add(game);
            }
        }

        return result;
    }
}
