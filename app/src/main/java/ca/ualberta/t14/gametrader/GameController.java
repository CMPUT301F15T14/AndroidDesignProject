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
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.ImageButton;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

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

        //model.notifyAllObservers();
    }

    /**
     * This will return a Bitmap, the image-json on file that should be shown must pass it's imageID here.
     * @param imageId the image id of image to show.
     * @param context the application's context.
     * @return a Bitmap of that image.
     */
    public Bitmap setPreviewImage(String imageId, Context context) {
        String json  = new String();
        try {
            json = PictureManager.loadImageJsonFromJsonFile(imageId, context);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return PictureManager.getBitmapFromJson(json);
    }

    /**
     * This will return a Bitmap after it resolved the given Uri to Bitmap.
     * @param uri the uri containing the path to an image
     * @param contentResolver activitie's content resolver
     * @return Bitmap of uri image
     */
    public Bitmap setPreviewUriImage(Uri uri, ContentResolver contentResolver) {
        return PictureManager.resolveUri(uri, contentResolver);
    }

    /**
     * Given game, it will add the uri containing a bitmap to the game.
     * @param game the game to receive the image.
     * @param uri the URI of the image to be added to the game.
     * @param contentResolver is the contentResolver from the activitie's getContentResolver();
     * @return returns false if the Bitmap supplied is invalid: has a height or width of 0;
     */
    public Boolean addPhoto(Game game, Uri uri, ContentResolver contentResolver, Context context) {
        Boolean success = Boolean.FALSE;

        Bitmap selectedImage = PictureManager.resolveUri(uri, contentResolver);
        if(selectedImage != null) {
            success = game.setPicture(selectedImage, context);
            selectedImage = null;
        }
        return success;
    }

    /**
     * Given game, this will remove an image from the game, as well add it to the removal list
     * so it will be removed from the database as well.
     * @param game the game containing the image.
     * @param imageId the ID of the image
     * @param context the context of the application
     * @return a Boolean of whether the image was removed or not
     */
    public Boolean removePhotos(Game game, String imageId, Context context) {
        PictureNetworkerSingleton.getInstance().getPicNetMangager().addImageFileToRemove(imageId);
        return game.removePictureId(imageId, context);
    }

    /**
     * This updates the temporary image box preview of the image in the activity. These images are not actually in the game
     * They are preview of what would be added to the game. If all images are removed it will display the "no photos" image.
     * @param imgBtn The image button that should contain the preview image
     * @param imageIds a list of imageIds that might become the preview.
     * @param uriList a list of Uri containing an image that might become the preview
     * @param context the context of the application.
     * @param content the contentResolver of the application.
     * @param activity the activity.
     */
    public void updateTemporaryImageBox(ImageButton imgBtn, ArrayList<String> imageIds, ArrayList<Uri> uriList, Context context, ContentResolver content, Activity activity) {
        if(!imageIds.isEmpty()) {
            imgBtn.setImageBitmap(setPreviewImage(imageIds.get(0), context));
        } else if(!uriList.isEmpty()) {
            imgBtn.setImageBitmap(setPreviewUriImage(uriList.get(0), content));
        } else {
            imgBtn.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.cd_empty));
        }
    }

    /**
     * Removes specified game from the given user's inventory.
     * @param game the game to be removed
     * @param user the user who's inventory he game is to be deleted from.
     */
    public void removeGame(Game game, User user, Context context) {
        if(isOwner(game, user)) {
            ArrayList<String> imageIds = game.getPictureIds();
            for(String each : imageIds) {
                PictureNetworkerSingleton.getInstance().getPicNetMangager().addImageFileToRemove(each);
            }
            user.getInventory().remove(game, context);
        }
    }

}
