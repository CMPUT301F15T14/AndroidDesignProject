package ca.ualberta.t14.gametrader;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.FileNotFoundException;
import java.io.InputStream;


public class EditInventoryItemActivity extends Activity {

    private final int REQ_IMG_UP = 465132;
    private GameController gc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_inventory_item);
        gc = new GameController();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_inventory_item, menu);
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

    public void uploadImage(View view) {

        // TODO: opens a prompt to select an image from file on phone and then put into Game http://javatechig.com/android/writing-image-picker-using-intent-in-android and http://www.sitepoint.com/web-foundations/mime-types-complete-list/
        // http://developer.android.com/reference/android/content/Intent.html#ACTION_GET_CONTENT
        Intent imgGet = new Intent(Intent.ACTION_GET_CONTENT);
        imgGet.setType("image/*");
        startActivityForResult(Intent.createChooser(imgGet, "Pick a photo representing the Game:"), REQ_IMG_UP);

    }

    // Taken from http://javatechig.com/android/writing-image-picker-using-intent-in-android
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case REQ_IMG_UP:
                if(resultCode == RESULT_OK){
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        gc.addPhoto(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

}

//here is the link on how to radio buttons: http://developer.android.com/guide/topics/ui/controls/radiobutton.html
//by default I have checked the Public radio button
