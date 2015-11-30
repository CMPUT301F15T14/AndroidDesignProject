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

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Aaron on 11/3/2015.
 */
public class FileIO {
    /**
     * Saves an object to android's internal storage in json format, to use simply provide a filename
     * which is just a string. The openFileOutput depends on context, when using in an activity/view
     * pass it the parameter getApplicationContext().
     * @param fileName - String to name the file you want to create
     * @param context - getApplicationContext()
     */
    public void saveJson(String fileName, Context context){
        Gson gson = new Gson();
        String stringObject = gson.toJson(this);
        try {
            FileOutputStream saveToDisk = context.openFileOutput(fileName, context.MODE_PRIVATE);
            saveToDisk.write(stringObject.getBytes());
            saveToDisk.flush();
            saveToDisk.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a single object, this works with all models: game, user, inventory...
     * converts json back to object form, and returns that object back.
     *
     * use case:
     * User x = new User();
     * x = (User) x.loadJson("FileNameOfUserObject", getApplicationContext);
     * x will now be loaded with the object stored in memory.
     *
     * @param fileName - String of the filename that exists, so that you can load an object.
     * @param context - getApplicationContext();
     * @return
     * @throws IOException
     */
    public Object loadJson(String fileName, Context context) throws IOException {
        FileInputStream input = context.openFileInput(fileName);
        BufferedReader myReader = new BufferedReader(new InputStreamReader(input));

        Gson gson = new Gson();

        Object x = gson.fromJson(myReader.readLine(), this.getClass());

        myReader.close();
        input.close();

        return x;
    }

    /**
     * A function that saves a given object to android's internal storage in json format, to use simply provide a filename
     * which is just a string. The openFileOutput depends on context, when using in an activity/view
     * pass it the parameter getApplicationContext().
     * @param fileName - String to name the file you want to create
     * @param context - getApplicationContext()
     */
    public void saveJsonWithObject(Object objectToSaveInJson, String fileName, Context context){
        Gson gson = new Gson();
        String stringObject = gson.toJson(objectToSaveInJson);
        try {
            FileOutputStream saveToDisk = context.openFileOutput(fileName, context.MODE_PRIVATE);
            saveToDisk.write(stringObject.getBytes());
            saveToDisk.flush();
            saveToDisk.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a single object of given class, this works with all models: game, user, inventory...
     * converts json back to object form, and returns that object back.
     *
     * use case:
     * User x = new User();
     * x = (User) x.loadJson("FileNameOfUserObject", getApplicationContext);
     * x will now be loaded with the object stored in memory.
     *
     * @param fileName - String of the filename that exists, so that you can load an object.
     * @param context - getApplicationContext();
     * @return
     * @throws IOException
     */
    public Object loadJsonGivenObject(String fileName, Context context, Object objectToGetClass) throws IOException {
        FileInputStream input = context.openFileInput(fileName);
        BufferedReader myReader = new BufferedReader(new InputStreamReader(input));

        Gson gson = new Gson();
        Object x = gson.fromJson(myReader.readLine(), objectToGetClass.getClass());

        myReader.close();
        input.close();

        return x;
    }

    /**
     * This method checks if file exists and if so removes it.
     * @param fileName - String of the filename that you want to remove.
     * @param context - getApplicationContext();
     * @return Returns true if successfully removed file. Returns false if removal failed, or file does not exist.
     */
    public boolean removeFile(String fileName, Context context){
        //Taken from stackoverflow --> http://stackoverflow.com/questions/13205385/how-to-check-if-file-is-available-in-internal-memory
        String path=context.getFilesDir().getAbsolutePath() + "/" + fileName;
        File file = new File( path );

        if (file.exists()) {
            return file.delete();
        } else {
            return false;
        }
    }

    /**
     * This static function checks if the file exists, returns true if it does, and false if it doesn't.
     * @param context - see above methods
     * @param fileName -see above methods
     * @return
     */
    public static boolean doesFileExist(String fileName, Context context){
        //Taken from stackoverflow --> http://stackoverflow.com/questions/13205385/how-to-check-if-file-is-available-in-internal-memory
        String path=context.getFilesDir().getAbsolutePath() + "/" + fileName;
        File file = new File( path );

        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }
}
