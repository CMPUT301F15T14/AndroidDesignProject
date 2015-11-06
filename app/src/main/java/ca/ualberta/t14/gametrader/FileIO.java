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
     * This function checks if the file exists, returns true if it does, and false if it doesn't.
     * @param context - see above methods
     * @param fileName -see above methods
     * @return
     */
    public boolean doesFileExist(String fileName, Context context){
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
