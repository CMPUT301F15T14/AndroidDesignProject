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
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * This is a manager for photos, the images will actually be stored in the elastic search online.
 * This Model will store the local JSON string of the images and downloaded JSON strings of images online.
 *
 * @author  Ryan Satyabrata
 */
public class PictureManager extends FileIO {

    public static final int COMPRESSION_QUALITY = 85;

    private static final Long MAXSIZE = new Long(65536);

    private Manager imgMng;

    /**
     * Constructor for the PictureManager class
     */
    public PictureManager() {
        imgMng = new Manager();
    }

    /**
     * Saves a given base64 encoded image into a file with a unique filename
     * that becomes that image's image ID.
     * @param imageJsonString The base64 encoded image as a string
     * @param userToGetInstallationId The user's android ID
     * @param context
     * @return the image ID of that image, which is the file name of the saved base64 image as well.
     */
    public String addImageToJsonFile(String imageJsonString, User userToGetInstallationId, Context context) {
        String id = imgMng.addItemToTrack(userToGetInstallationId, "img");
        // Save file here as IO.
        saveJsonWithObject(imageJsonString, id, context);

        return id;
    }

    /**
     * This static method retrieves the base64 encoded image as a string from the
     * saved base64 encoded image that is stored on the user's device in the application folder.
     * @return a string containing the byteArray of the bitmap encoded as a string in Base64.
     */
    public static String loadImageJsonFromJsonFile(String fileName, Context context) throws FileNotFoundException {
        String imageJson = "";
        // Load file here as IO.
        try {
            imageJson = (String) loadJsonGivenObject(fileName, context, new String());
        } catch(IOException e) {
            e.printStackTrace();
        }
        return imageJson;
    }

    /**
     * This static method converts a given Bitmap image into a base64 encoded string representation
     * of that image.
     * @param image
     * @return the base64 encoded string.
     */
    public static String getStringFromBitmap(Bitmap image) {
        // Make the Bitmap JSON-able (Bitmap is not JSON-able) Taken from http://mobile.cs.fsu.edu/converting-images-to-json-objects/
        ByteArrayOutputStream byteArrayBitmapStream = null;
        String base64Encoded = "";

        byteArrayBitmapStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, PictureManager.COMPRESSION_QUALITY, byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        base64Encoded = Base64.encodeToString(b, Base64.DEFAULT);
        try {
            if (byteArrayBitmapStream != null)
                byteArrayBitmapStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64Encoded;
    }

    /**
     * Manually force download the image from the network even if settings is set to don't download images.
     * @param game The game that contains the image you want to force download.
     * @return the bitmap from that game.
     */
    public Bitmap manualDownloadPhoto(Game game) {
        // TODO: from cache or elastisearch, find the image associated to this game and force download it (disregard the settings)
        // perhaps this method is redundant due to loadImageJsonFromJsonFile();
        return null;
    }

    /**
     * This static method estimates the Base64 encoded file size of the given Bitmap.
     * @param image the Bitmap image to estimate the file size.
     * @return the estimated file size.
     */
    public static Long getImageFileSize(Bitmap image) {
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, PictureManager.COMPRESSION_QUALITY, byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        // taken from http://stackoverflow.com/questions/13378815/base64-length-calculation
        // Output file will be ceil(4*(n/3)) bytes long because Base64
        Long size =  Math.round( Math.ceil( 4.0f * (b.length / 3.0f) ) ) + 2000;
        try{byteArrayBitmapStream.close();} catch(Exception e){}
        return size;
    }

    /**
     * This static method decodes a given Base64 image to android's Bitmap format.
     * @param jsonBitmap base64 to decode
     * @return the Android's Bitmap
     */
    public static Bitmap getBitmapFromJson(String jsonBitmap) {
        //taken from http://mobile.cs.fsu.edu/converting-images-to-json-objects/
        byte[] decodedString = Base64.decode(jsonBitmap, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        if(decodedByte != null) {
            return decodedByte;
        }
        return null;
    }

    /**
     * This static method resolves the Uri given and returns the Bitmap that the Uri points to.
     * @param uri
     * @param contentResolver
     * @return Android's bitmap image from that Uri.
     */
    public static Bitmap resolveUri(Uri uri, ContentResolver contentResolver){
        Bitmap selectedImage = null;
        try {
            InputStream imageStream = contentResolver.openInputStream(uri);

            BitmapFactory.Options o = new BitmapFactory.Options();

            selectedImage = BitmapFactory.decodeStream(imageStream, null, o);
            imageStream.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return selectedImage;
    }

    /**
     * This static method takes a Bitmap and resizes it until it's estimated filesize is smaller
     * than the PictureManager's MAXSIZE value. That smaller image will then be returned.
     * @param image
     * @return The resized Bitmap image.
     */
    public static Bitmap makeImageSmaller(Bitmap image) {
        Bitmap img = image;
        if(getImageFileSize(img) < MAXSIZE) {
            return img;
        } else {
            Integer longestEdge = 500;
            while(getImageFileSize(img) >= MAXSIZE) {
                img = preserveAspectRatio(image, longestEdge);
                longestEdge -= 25;
            }
        }
        return img;
    }

    private static Bitmap preserveAspectRatio(Bitmap image, Integer resize_value) {
        int imgW = image.getWidth();
        int imgH = image.getHeight();

        if(imgW < imgH) {
            float aspectRatio = ((float) imgW) / imgH;
            int newHeight = resize_value;
            int newWidth = Math.round(aspectRatio * newHeight);
            return Bitmap.createScaledBitmap(image, newWidth, newHeight, Boolean.TRUE);
        } else if(imgW > imgH) {
            float aspectRatio = ((float) imgH) / imgW;
            int newWidth = resize_value;
            int newHeight = Math.round(aspectRatio * newWidth);
            return Bitmap.createScaledBitmap(image, newWidth, newHeight, Boolean.TRUE);
        } else if(imgW == imgH) {
            return Bitmap.createScaledBitmap(image, resize_value, resize_value, Boolean.TRUE);
        }
        // something went horribly wrong.
        return null;
    }

}
