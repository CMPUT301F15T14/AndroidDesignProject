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
 * Created by satyabra on 11/1/15.
 */
public class GameController {

    private Game model;

    public GameController() {
        model = new Game();
        //model.loadJson();//???
    }

    public Game createGame() {
        model = new Game();

        return model;
    }

    public Boolean isOwner(Game game) {
        // TODO: 1. get device-id from user here (the owner of this device has a profile) and 2. search for inventory that belongs to this user. 3. Then check this inventory if it contains this game
        return Boolean.FALSE;
    }

    public void editGame(Game game) {
        // TODO: launch the activity's edit game screen??

    }

    public void addPhoto(Bitmap img) {
        Boolean imageSizeOk = model.setPicture(img);
    }

    public Bitmap manualDownloadPhoto(Game game) {
        // TODO: from cache or elastisearch, find the image associated to this game and force download it (disregard the settings)
        return null;
    }

    public void removeGame(Game game) {
        // TODO: removeGame will remove the entry from the inventory.
    }

}
