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

import android.graphics.Bitmap;

/**
 * This is the Controller class for EditInventoryItemActivity.
 * It provides methods to take input and make changes to the Game model.
 * @author  Ryan Satyabrata
 */
public class GameController {

    private Game model;

    /**
     * Constructor for the GameController. Does nothing.
     */
    public GameController() { }

    /**
     * Creates a new game with given user and adds that created game into the user's inventory.
     * @param user the user that receives the new game item.
     * @return the Game object reference that was also passed into te inventory.
     */
    public Game createGame(User user) {
        model = new Game();
        Inventory inventory = user.getInventory();
        inventory.add(model);
        return model;
    }

    /**
     * Determines if the user given is the owner of that given game.
     * @param game the game object to check ownership
     * @param user user to check against
     * @return a boolean if the given user is the owner of the game or not.
     */
    public Boolean isOwner(Game game, User user) {
        // TODO: 1. get device-id from user here (the owner of this device has a profile) and 2. search for inventory that belongs to this user. 3. Then check this inventory if it contains this game
        Inventory inventory = user.getInventory();
        return inventory.contains(game);
    }

    /**
     * It will update the Game's model with the new input.
     * @param game The game that is to be updated.
     * @param title A new title for the Game.
     * @param picture A new picture for the Game.
     * @param platform A new platform for the Game.
     * @param condition A new condition for the Game.
     * @param sharableStatus Change sharable status to either Public or Private
     * @param additionalInfo New additional information for the game.
     */
    public void editGame(Game game, String title, Bitmap picture, Game.Platform platform,
                         Game.Condition condition, Boolean sharableStatus, String additionalInfo) {
        game.setPlatform(platform);
        game.setCondition(condition);
        game.setTitle(title);
        game.setShared(sharableStatus);
        game.setAdditionalInfo(additionalInfo);
        if(picture != null) {
            game.setPicture(picture);
        }

    }

    /**
     * Given game, it will add the bitmap given to the game.
     * @param game the game to receive the image.
     * @param img the image to be added to the game
     * @return returns false if the Bitmap supplied is invalid: has a height or width of 0;
     */
    public Boolean addPhoto(Game game, Bitmap img) {
        return game.setPicture(img);
    }

    /**
     *
     * @param game
     * @return
     */
    public Bitmap manualDownloadPhoto(Game game) {
        // TODO: from cache or elastisearch, find the image associated to this game and force download it (disregard the settings)
        return null;
    }

    public void removeGame(Game game, User user) {
        // TODO: removeGame will remove the entry from the inventory.
        if(isOwner(game, user)) {
            user.getInventory().remove(game);
        }
    }

}
