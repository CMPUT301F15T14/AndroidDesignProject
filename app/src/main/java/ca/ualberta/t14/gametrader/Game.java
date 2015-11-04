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
    volatile private Bitmap picture;
    private String pictureJsonable;
    private int quantities;
    private final int MAX_BYTES_IMG_SIZE = 65536;
    private final int COMPRESSION_QUALITY = 100;

    // volatile because GSON shouldn't store this.
    private volatile ArrayList<AppObserver> observers;

    /**
     * Constructor for the Game class.Initializes its variables.
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
     * Returns the platform the game was intended to run on.
     * @return the enumeration of target platform of the game.
     */
    public Platform getPlatform() {
        return platform;
    }

    /**
     * Set the platform the game was intended to primarily run on.
     * @param platform the proper platform enumeration for the game. Use Platform.OTHER if it isn't listed.
     */
    public void setPlatform(Platform platform) {
        this.platform = platform;
        notifyAllObservers();
    }

    /**
     * Returns what condition the game is in.
     * @return the enumeration of the condition of the game.
     */
    public Condition getCondition() {
        return condition;
    }

    /**
     * Set the condition the game is in.
     * @param condition the enumeration of the condition of the game.
     */
    public void setCondition(Condition condition) {
        this.condition = condition;
        notifyAllObservers();
    }

    /**
     * Returns the title of the given game.
     * @return a String containing the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the title of the game: the game's name.
     * @param title a String containing the game's name.
     */
    public void setTitle(String title) {
        this.title = title;
        notifyAllObservers();
    }

    /**
     * Returns a flag of whether or not the game is listed/shared.
     * @return a Boolean if the game is listed/shared or not.
     */
    public Boolean isShared() {
        return sharableStatus;
    }

    /**
     * Set the sharable flag of the game.
     * @param sharableStatus the Boolean if TRUE then the game will be listed & shared.
     */
    public void setShared(Boolean sharableStatus) {
        this.sharableStatus = sharableStatus;
        notifyAllObservers();
    }

    /**
     * Returns any additional info or description the game may have.
     * @return a String containing additional info or just an empty String if no info..
     */
    public String getAdditionalInfo() {
        return additionalInfo;
    }

    /**
     * Sets any additional info or description of the game.
     * @param additionalInfo
     */
    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
        notifyAllObservers();
    }

    /**
     * Returns the number of available copies of the game.
     * @return the quantity available.
     */
    public int getQuantities() {
        return quantities;
    }

    /**
     * Sets the numbner of available copies of the game.
     * @param quantities the quantity available.
     */
    public void setQuantities(int quantities) {
        this.quantities = quantities;
        notifyAllObservers();
    }

    /**
     * Get the picture of the game.
     * @return a Bitmap picture of the game.
     */
    public Bitmap getPicture() {
        return picture;
    }

    /**
     * Get the string representation of the bitmap.
     * @return a string containing the byteArray of the bitmap encoded as a string in Base64.
     */
    public String getPictureJson() {
        return pictureJsonable;
    }

    /**
     * sets the Bitmap of the game object to the json-able image given.
     * @param jsonBitmap a string containing the byteArray of the bitmap encoded as a string in Base64.
     * @return a Boolean: whether or not the image data could be decoded.
     */
    public Boolean setPictureFromJson(String jsonBitmap) {
        //taken from http://mobile.cs.fsu.edu/converting-images-to-json-objects/
        byte[] decodedString = Base64.decode(jsonBitmap, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        if(decodedByte != null) {
            picture = decodedByte.copy(Bitmap.Config.ARGB_8888, Boolean.FALSE);
            // set the json image to the current image.
            pictureJsonable = jsonBitmap;
        }
        return decodedByte != null;
    }

    /**
     * Sets a picture for the game.
     * @param image a Bitmap picture of the game.
     * @return a Boolean whether the image was set or not. If not it means image is bigger than 65536 bytes or Bitmap was null.
     */
    public Boolean setPicture(Bitmap image) {
        // TODO: store these images json separately with ID that belongs to this game, since stored it with the model makes no sense: no bandwidth saved if downloadImages is off!
        // set the volatile bitmap, when resizing: maintains aspect ratio of the image.
        if (image.getWidth() > 128 || image.getHeight() > 128) {
            if(image.getWidth() < image.getHeight()) {
                float aspectRatio = image.getWidth() / image.getHeight();
                int newHeight = 128;
                int newWidth = Math.round(aspectRatio * newHeight);
                picture = Bitmap.createScaledBitmap(image,newWidth, newHeight, Boolean.TRUE);
            } else if(image.getWidth() > image.getHeight()) {
                float aspectRatio = image.getHeight() / image.getWidth();
                int newWidth = 128;
                int newHeight = Math.round(aspectRatio * newWidth);
                picture = Bitmap.createScaledBitmap(image, newWidth, newHeight, Boolean.TRUE);
            }
        } else {
            picture = image.copy(Bitmap.Config.ARGB_8888, Boolean.FALSE);
        }

        // Make the Bitmap JSON-able (Bitmap is not JSON-able) Taken from http://mobile.cs.fsu.edu/converting-images-to-json-objects/
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        picture.compress(Bitmap.CompressFormat.JPEG, COMPRESSION_QUALITY, byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        pictureJsonable = Base64.encodeToString(b, Base64.DEFAULT);

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
        return new Long(b.length);
    }

}
