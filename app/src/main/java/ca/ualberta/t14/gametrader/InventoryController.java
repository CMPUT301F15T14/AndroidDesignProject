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

import android.content.Context;

import java.util.ArrayList;

// Controller class of Inventory Class.
public class InventoryController {
    private Inventory stock;
    public InventoryController(Inventory inventory){
        this.stock=inventory;
    }
    public void addItem(Game game){
        stock.add(game);
    }

    public void clearInventory(){
        stock.clear();
    }

    public boolean contains(Game game){
        return stock.contains(game);
    }

    /**
     * Returns the games in the inventory whose title contains the given search query
     * @param query the String to search for
     * @return a list of games containing the given string
     */
    public ArrayList<Game> Search(String query) {
        ArrayList<Game> result = new ArrayList<Game>();

        for(Game game : stock.getAllGames()) {
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

        for(Game game : stock.getAllGames()) {
            if(game.getTitle().contains(query) && game.getPlatform() == platform){
                result.add(game);
            }
        }

        return result;
    }

    public boolean clonable(User user){
        return UserSingleton.getInstance().getUser().getUserName() != user.getUserName();
    }

    public void clone(Game game,Context context){
        Game clonedGame = new Game(game, context);
        User deviceUser = UserSingleton.getInstance().getUser();
        deviceUser.getInventory().add(clonedGame);
        deviceUser.saveJson("MainUserProfile", context);
    }

}
