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

/**
 * Created by michaelximac on 2015-11-01.
 */
public class Inventory {
    private ArrayList<Game> gameCollections;
    private User owner;
    public Inventory(){
        this.gameCollections=new ArrayList<Game>();
    }

    public void add(Game game){
        gameCollections.add(game);;
    }
    
    public void remove(Game game){
        gameCollections.remove(game);
    }

    public void clear(){
        gameCollections.clear();
    }

    public boolean contains(Game game){
        return gameCollections.contains(game);
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getOwner() {
        return owner;
    }

    public ArrayList<Game> getAllGames() {
        return gameCollections;
    }
}
