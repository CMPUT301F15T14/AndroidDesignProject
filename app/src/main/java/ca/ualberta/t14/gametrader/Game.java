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
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * This obsservable class represents the Game Model which stores an inventory item's data: a game.
 * It covers Use Cases 1.1, 4.1, 1.2:
 * Create items that get added to your inventory (1.1)
 * including attaching a photo (4.1)
 * and being able to flag an item as not listed (1.4). Allow users to view their inventory (1.2)
 *
 * Commenting conventions taken from oracle: http://www.oracle.com/technetwork/articles/java/index-137868.html
 *
 * @author  Ryan Satyabrata
 */
public class Game implements AppObservable {

    /**
     * An enumeration of all the different video game consoles a game can be in this app.
     */
    public enum Platform { PLAYSTATION1, PLAYSTATION2, PLAYSTATION3, PLAYSTATION4,
        XBOX, XBOX360, XBOXONE, WII, WIIU, OTHER }

    /**
     * An enumeration of the condition that the game is in. Best condition is NEW and
     * worst is ACCEPTABLE which implies the game is still playable at the very least but
     * e.g. has a scratched disc without its original case.
     */
    public enum Condition { NEW, LIKENEW, MINT, ACCEPTABLE }

    private Platform platform;
    private Condition condition;
    private String title;
    private Boolean sharableStatus;
    private String additionalInfo;

    // TODO: many pictures to 1 item... see eclass prof. hindle's response:
    //Re: Pictures of Items by Abram Hindle - Friday, 6 November 2015, 12:39 AM
    //>  Do we have to allow a user to have multiple pictures for an item or can it just be one picture per item?
    //US06.01.01 As an owner, I want to optionally attach photographs of items to the item. Photos are optional for items. => many to 1
    volatile private Bitmap picture;
    private String pictureJsonable;

    private int quantities;
    private final int COMPRESSION_QUALITY = 85;
    private final Long MAXSIZE = new Long(65536);

    // volatile because GSON shouldn't store this.
    private volatile ArrayList<AppObserver> observers;


    /**
     * Constructor for the Game class. Initializes variables to a default value..
     */
    public Game() {
        platform = Platform.OTHER;
        condition = Condition.NEW;
        title = "";
        sharableStatus = Boolean.FALSE;
        additionalInfo = "";
        picture = null;
        pictureJsonable = "";
        quantities = 0;
        observers = new ArrayList<AppObserver>();

    }


    /**
     * The tag for the game of which platform it runs on.
     * To be used in a View, e.g. to display what platform this game runs on.
     * Returns the platform the game was intended to run on.
     * @return the enumeration of target platform of the game.
     */
    public Platform getPlatform() {
        return platform;
    }

    /**
     * The tag for the game of which platform it runs on.
     * Should be used in a Controller where the user can set the Platform of the item.
     * Set the platform the game was intended to primarily run on.
     * @param platform the proper platform enumeration for the game. Use Platform.OTHER if it isn't listed.
     */
    public void setPlatform(Platform platform) {
        this.platform = platform;
        notifyAllObservers();
    }

    /**
     * The tag to find out what kind of condition this game is in.
     * To be used in a View, e.g. to display the condition of the game.
     * Returns what condition the game is in.
     * @return the enumeration of the condition of the game.
     */
    public Condition getCondition() {
        return condition;
    }

    /**
     * The tag to find out what kind of condition this game is in.
     * Should be used in a Controller where the user sets the condition of the item.
     * Set the condition the game is in.
     * @param condition the enumeration of the condition of the game.
     */
    public void setCondition(Condition condition) {
        this.condition = condition;
        notifyAllObservers();
    }

    /**
     * Returns the title of the given game.
     * Should be used when something wants to retrieve the title of the game item.
     * To be used in a View, e.g. to display the title of the game.
     * @return a String containing the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the title of the game: the game's name.
     * Should be used in a Controller where the user sets the title of the game.
     * @param title a String containing the game's name.
     */
    public void setTitle(String title) {
        this.title = title;
        notifyAllObservers();
    }

    /**
     * Returns a flag of whether or not the game is to be listed/shared.
     * Should be used by the View or model to determine to show this or not or store it in a public list or not..
     * @return a Boolean if the game is listed/shared or not.
     */
    public Boolean isShared() {
        return sharableStatus;
    }

    /**
     * Set the sharable flag of the game, whether the game will be listed/shared or not.
     * Should be used by a Controller so the user can change the sharable flag.
     * @param sharableStatus the Boolean if TRUE then the game will be listed & shared.
     */
    public void setShared(Boolean sharableStatus) {
        this.sharableStatus = sharableStatus;
        notifyAllObservers();
    }

    /**
     * Returns any additional info or description the game may have.
     * Used by View so the additional information can be retrieved and displayed.
     * @return a String containing additional info or just an empty String if no info..
     */
    public String getAdditionalInfo() {
        return additionalInfo;
    }

    /**
     * Sets any additional info or description of the game.
     * To be used in a Controller so the user can make changes to the additional information.
     * @param additionalInfo
     */
    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
        notifyAllObservers();
    }

    /**
     * Returns the number of available copies of the game.
     * To be used by a view to display the number of copies the owner has of this game.
     * @return the quantity available.
     */
    public int getQuantities() {
        return quantities;
    }

    /**
     * Sets the numbner of available copies of the game.
     * To be used in a Controller so the user can change the available copies they have of the game.
     * @param quantities the quantity available.
     */
    public void setQuantities(int quantities) {
        this.quantities = quantities;
        notifyAllObservers();
    }

    /**
     * Get the picture of the game.
     * To be used in a view so the picture (if any) can be displayed.
     * @return a Bitmap picture of the game.
     */
    public Bitmap getPicture() {
        return picture;
    }

    /**
     * Get the string representation of the bitmap.
     * To be used by the model so this picture can be put in a JSON and put online.
     * @return a string containing the byteArray of the bitmap encoded as a string in Base64.
     */
    public String getPictureJson() {
        return pictureJsonable;
    }

    /**
     * sets the Bitmap of the game object to the json-able image given.
     * To be used by a view when using game items from the network to retrieve the picture (if any) from the JSON format.
     * @param jsonBitmap a string containing the byteArray of the bitmap encoded as a string in Base64.
     * @return a Boolean: whether or not the image data could be decoded.
     */
    public Boolean setPictureFromJson(String jsonBitmap) {
        //taken from http://mobile.cs.fsu.edu/converting-images-to-json-objects/
        byte[] decodedString = Base64.decode(jsonBitmap, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        if(decodedByte != null) {
            picture = decodedByte;
            // set the json image to the current image.
            pictureJsonable = jsonBitmap;
        }
        return decodedByte != null;
    }

    /**
     * Sets a picture for the game.
     * To be used by a Controller so the user can add an image to the game item.
     * If image given is bigger than 200x200 it gets scaled down with longest edge becoming 200, the aspect ratio is kept same.
     * It also compresses the Bitmap to JPG with 85% compression quality so its JSON-able and stores the JSON to the game object.
     * @param image a Bitmap picture of the game.
     * @return a Boolean whether the image was set or not. If false, the provided Bitmap is invalid: has a dimension that is 0.
     */
    public Boolean setPicture(Bitmap image) {
        // TODO: store these images json separately with ID that belongs to this game, since stored it with the model makes no sense: no bandwidth saved if downloadImages is off!
        // 400x400 seems acceptable = below 65536 bytes when compress jpg 85% in gimp. Try 350x350 and hope its always below 65536 bytes
        // set the volatile bitmap, when resizing: maintains aspect ratio of the image.

        if( image.getWidth() <=0 || image.getHeight() <= 0) {
            return Boolean.FALSE;
        } else {
            // preserve aspect ratio of image and make it smaller if its bigger than 65.536KB
            picture = makeSmaller(image);
        }

        pictureJsonable = makeBitmapJsonable(picture);

        notifyAllObservers();
        return Boolean.TRUE;
    }


    /**
     * The Game Model is observable thus anything wanting to obsserve can be added to the watch list.
     * @param observer the observer wanting to watch this Model.
     */
    public void addObserver(AppObserver observer) {
        observers.add(observer);
    }

    private void notifyAllObservers() {
        for(AppObserver obs : observers) {
            obs.appNotify(this);
        }
    }

    private Long getImageJpgSize(Bitmap image) {
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, COMPRESSION_QUALITY, byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        Long size = new Long(b.length);
        try{byteArrayBitmapStream.close();} catch(Exception e){}
        return size;
    }

    private String makeBitmapJsonable(Bitmap image) {
        // Make the Bitmap JSON-able (Bitmap is not JSON-able) Taken from http://mobile.cs.fsu.edu/converting-images-to-json-objects/
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, COMPRESSION_QUALITY, byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        String base64Encoded = Base64.encodeToString(b, Base64.DEFAULT);
        try{byteArrayBitmapStream.close();} catch(Exception e){}
        return base64Encoded;
    }

    private Bitmap makeSmaller(Bitmap image) {
        Bitmap img = image;
        if(getImageJpgSize(img) < MAXSIZE) {
            return img;
        } else {
            Integer longestEdge = 500;
            while(getImageJpgSize(img) >= MAXSIZE) {
                img = preserveAspectRatio(img, longestEdge, Boolean.TRUE);
                longestEdge -= 25;
            }
        }
        return img;
    }

    private Bitmap preserveAspectRatio(Bitmap image, Integer resize_value, Boolean recycleArgumentImage) {
        int imgW = image.getWidth();
        int imgH = image.getHeight();

        if(imgW < imgH) {
            float aspectRatio = ((float) imgW) / imgH;
            int newHeight = resize_value;
            int newWidth = Math.round(aspectRatio * newHeight);
            if(recycleArgumentImage)
                image.recycle();
            return Bitmap.createScaledBitmap(image, newWidth, newHeight, Boolean.TRUE);
        } else if(imgW > imgH) {
            float aspectRatio = ((float) imgH) / imgW;
            int newWidth = resize_value;
            int newHeight = Math.round(aspectRatio * newWidth);
            if(recycleArgumentImage)
                image.recycle();
            return Bitmap.createScaledBitmap(image, newWidth, newHeight, Boolean.TRUE);
        } else if(imgW == imgH) {
            if(recycleArgumentImage)
                image.recycle();
            return Bitmap.createScaledBitmap(image, resize_value, resize_value, Boolean.TRUE);
        }
        // something went horribly wrong.
        return null;
    }

}
