package ca.ualberta.t14.gametrader;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by satyabra on 11/24/15.
 */
public class PictureFileHandler {

    PictureFileHandler() {}

    Boolean storePictureToDisk(String fileName, Bitmap image, Context context) {
        // Make the Bitmap JSON-able (Bitmap is not JSON-able) Taken from http://mobile.cs.fsu.edu/converting-images-to-json-objects/
        ByteArrayOutputStream byteArrayBitmapStream = null;
        byte[] b = null;
        String base64Encoded = "";
        FileOutputStream saveToDisk = null;
        try {
            byteArrayBitmapStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, PictureManager.COMPRESSION_QUALITY, byteArrayBitmapStream);
            b = byteArrayBitmapStream.toByteArray();
            base64Encoded = Base64.encodeToString(b, Base64.DEFAULT);

            saveToDisk = context.openFileOutput(fileName, context.MODE_PRIVATE);
            saveToDisk.write(base64Encoded.getBytes());
            saveToDisk.flush();
            saveToDisk.close();
            byteArrayBitmapStream.close();
            return Boolean.TRUE;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }

    String getStoredPictureAsString(String fileName, Context context){
        try{
            FileInputStream input = context.openFileInput(fileName);
            BufferedReader myReader = new BufferedReader(new InputStreamReader(input));

            myReader.close();
            input.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "NO PLZ DONT PUNCH ME!";
    }

}
