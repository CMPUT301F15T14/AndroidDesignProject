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

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.widget.ImageButton;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Controller class of Inventory Class.
 */
public class InventoryItemController {
    private Inventory stock;
    public InventoryItemController(Inventory inventory){
        this.stock=inventory;
    }

    /**
     * A method used in InventoryItemActivity, it's purpose is to try to download all images of the given game.
     * @param game to get the images from this game.
     * @param context the application context from the activity should be passed here.
     */
    public void tryDownloadImages(Game game, Context context) {
        PictureNetworker pn = PictureNetworkerSingleton.getInstance().getPicNetMangager();
        if(SettingsSingleton.getInstance().getSettings().getEnableDownloadPhoto1()
                && !pn.getLocalCopyOfImageIds().containsAll(game.getPictureIds())) {
            final Game g = game;
            final Context c = context;
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    PictureNetworker pn = PictureNetworkerSingleton.getInstance().getPicNetMangager();
                    for(String each : g.getPictureIds()) {
                        pn.addImageToDownload(each);
                    }
                }
            };
            Thread t = new Thread(r);
            t.start();
        }
    }

    /**
     * A method used in InventoryItemActivity, it sets the given image button to the given game's first picture.
     * If no picture associated to the game, it will put in the default "no photos" picture for the game.
     * @param game
     * @param imageButton
     * @param activity
     */
    public void setImageToImageButtons(Game game, ImageButton imageButton, Activity activity) {
        String imageJson  = new String();
        try {
            imageJson =  PictureManager.loadImageJsonFromJsonFile(game.getFirstPictureId(), activity.getApplicationContext());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(!imageJson.isEmpty()) {
            imageButton.setImageBitmap(PictureManager.getBitmapFromJson(imageJson));
        } else if(PictureNetworkerSingleton.getInstance().getPicNetMangager().getImagesToDownload().contains(game.getFirstPictureId())) {
            imageButton.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.saveimages));
        } else {
            imageButton.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.cd_empty));
        }
    }

    /**
     * A method to check if the inventory contains a given game.
     * @param game
     * @return
     */
    public boolean contains(Game game){
        return stock.contains(game);
    }
    
    /**
     * A method to check if cloning is available to this user. It is used in InventoryItemActivity to check
     * if the given user can clone this game (cannot clone own items basicall)
     * @param user
     * @return
     */
    public boolean clonable(User user){
        return UserSingleton.getInstance().getUser().getUserName() != user.getUserName();
    }


    /**
     * Given game and application context, the given game will be cloned to the inventory of the
     * device owner. Including images if images were downloaded before, or network is present.
     * @param game
     * @param context
     */
    public void clone(Game game,Context context){
        Game clonedGame = new Game(game, context);
        User deviceUser = UserSingleton.getInstance().getUser();
        deviceUser.getInventory().add(clonedGame);

        //add notify network thingy that user has been updated and needs to be uploaded
        deviceUser.saveJson("MainUserProfile", context);
    }

}
