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

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.widget.ImageButton;

import java.io.FileNotFoundException;
import java.util.ArrayList;

// Controller class of Inventory Class.
public class InventoryController {
    private Inventory stock;
    public InventoryController(Inventory inventory){
        this.stock=inventory;
    }

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

    public void setImageToImageButtons(Game game, ImageButton imageButton, Activity activity) {
        String imageJson  = new String();
        try {
            imageJson =  PictureManager.loadImageJsonFromJsonFile(game.getFirstPictureId(), activity.getApplicationContext());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(!imageJson.isEmpty()) {
            imageButton.setImageBitmap(PictureManager.getBitmapFromJson(imageJson));
        } else {
            imageButton.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.cd_empty));
        }
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

        //add notify network thingy that user has been updated and needs to be uploaded
        deviceUser.saveJson("MainUserProfile", context);
    }

}
