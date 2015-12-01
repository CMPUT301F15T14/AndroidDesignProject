package ca.ualberta.t14.gametrader;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GameImageRemover extends Activity {

    GameImageRemoverController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_image_remover);

        ArrayList<String> imageIdsList = (ArrayList<String>) ObjParseSingleton.getInstance().popObject("gameImagesList");
        ArrayList<Uri> imageUriList = (ArrayList<Uri>) ObjParseSingleton.getInstance().popObject("imagesUriArray");

        controller = new GameImageRemoverController(imageIdsList, imageUriList, getApplicationContext(), this);
        controller.setOnClickButtons();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_image_remover, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
