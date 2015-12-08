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
 * Controller class of Inventory Class.
 */
public class InventoryListController {
    private ArrayList<Game> stock;

    /**
     * Constructor for the InventoryListController. It takes an array of games to initialize it's own array of games.
     * @param inventory
     */
    public InventoryListController(ArrayList<Game> inventory){
        this.stock=inventory;
    }

    /**
     * Adds an item to this object's private array
     * @param game
     */
    public void addItem(Game game){
        stock.add(game);
    }

    /**
     * Clears this object's private array.
     */
    public void clearInventory(){
        stock.clear();
    }

    /**
     * Checks whether this object's private array contains a given game or not.
     * @param game
     * @return
     */
    public boolean contains(Game game){
        return stock.contains(game);
    }


}
