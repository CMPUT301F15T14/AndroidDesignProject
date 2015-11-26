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

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.InputStream;

/**
 * This is the Controller class for EditInventoryItemActivity.
 * It provides methods to take input and make changes to the Game model.
 * @author  Ryan Satyabrata
 */
public class GameController {

    private Game model;

    /**
     * Constructor for the GameController. Does nothing. Really.
     */
    public GameController() { }

    /**
     * Creates a new game with given user and adds that created game into the user's inventory.
     * To be used when user wants to create a game.
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
     * It will update the Game's model with the new input received from the user.
     * @param game The game that is to be updated.
     * @param title A new title for the Game.
     * @param platform A new platform for the Game.
     * @param condition A new condition for the Game.
     * @param sharableStatus Change sharable status to either Public or Private
     * @param additionalInfo New additional information for the game.
     */
    public void editGame(Game game, String title, Game.Platform platform,
                         Game.Condition condition, Boolean sharableStatus, String additionalInfo) {
        game.setPlatform(platform);
        game.setCondition(condition);
        game.setTitle(title);
        game.setShared(sharableStatus);
        game.setAdditionalInfo(additionalInfo);

    }

    /**
     * Given game, it will add the bitmap given to the game.
     * @param game the game to receive the image.
     * @param uri the URI of the image to be added to the game.
     * @param contentResolver is the contentResolver from the activitie's getContentResolver();
     * @return returns false if the Bitmap supplied is invalid: has a height or width of 0;
     */
    public Boolean addPhoto(Game game, Uri uri, ContentResolver contentResolver, Context context) {
        Boolean success = Boolean.FALSE;

        Bitmap selectedImage = resolveUri(uri, contentResolver);
        if(selectedImage != null) {
            success = game.setPicture(selectedImage, context);
            selectedImage = null;
            success = Boolean.TRUE;
        }
        return success;
    }

    public Bitmap resolveUri(Uri uri, ContentResolver contentResolver){
        Bitmap selectedImage = null;
        try {
            InputStream imageStream = contentResolver.openInputStream(uri);

            //Start of should GO into controller
            // getting just image bounds taken from http://developer.android.com/training/displaying-bitmaps/load-bitmap.html
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(imageStream, null, o);
            int approxSample = (int) Math.ceil((o.outHeight * o.outWidth) / (500.0f * 500.0f));

            // Bitmap options taken from http://stackoverflow.com/questions/11944182/android-out-of-memory-exception-how-does-decoderesource-add-to-the-vm-budget
            o.inJustDecodeBounds = false;
            o.inSampleSize = approxSample;
            o.inDither = false;

            // taken from http://stackoverflow.com/questions/12006785/android-skimagedecoder-factory-returned-null
            if (imageStream.markSupported()) {
                // will only throw if markSupported is false.
                imageStream.reset();
            } else {
                // reload it again just to reset reader position...
                imageStream = contentResolver.openInputStream(uri);
            }
            selectedImage = BitmapFactory.decodeStream(imageStream, null, o);
            imageStream.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return selectedImage;
    }

    /**
     * Manually force download the image from the network even if settings is set to don't download images.
     * @param game The game that contains the image you want to force download.
     * @return the bitmap from that game.
     */
    public Bitmap manualDownloadPhoto(Game game) {
        // TODO: from cache or elastisearch, find the image associated to this game and force download it (disregard the settings)
        return null;
    }

    /**
     * Removes specified game from the given user's inventory.
     * @param game the game to be removed
     * @param user the user who's inventory he game is to be deleted from.
     */
    public void removeGame(Game game, User user) {
        // TODO: removeGame will remove the entry from the inventory.
        if(isOwner(game, user)) {
            user.getInventory().remove(game);
        }
    }

}
