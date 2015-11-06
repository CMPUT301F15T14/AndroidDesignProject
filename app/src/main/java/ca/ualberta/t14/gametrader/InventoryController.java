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

/**
 * Created by michaelximac on 2015-11-01.
 */
public class InventoryController {
    private Inventory stock;
    public InventoryController(Inventory inventory,User owner){
        this.stock=inventory;
        stock.setOwner(owner);
    }
    public void addItem(Game game){
        stock.add(game);
    }

    public void removeItem(Game game){
        stock.remove(game);
    }

    public User identifyOwner(){
        return stock.getOwner();
    }

    public void clearInventory(){
        stock.clear();
    }
    public boolean contains(Game game){
        return stock.contains(game);
    }

}
