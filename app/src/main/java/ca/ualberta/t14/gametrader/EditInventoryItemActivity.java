package ca.ualberta.t14.gametrader;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class EditInventoryItemActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_inventory_item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_inventory_item, menu);
        return true;
    }

    /*
        // TODO: opens a prompt to select an image from file on phone and then put into Game http://javatechig.com/android/writing-image-picker-using-intent-in-android and http://www.sitepoint.com/web-foundations/mime-types-complete-list/
        // http://developer.android.com/reference/android/content/Intent.html#ACTION_GET_CONTENT
        Intent imgGet = new Intent(Intent.ACTION_GET_CONTENT);
        imgGet.setType("image/*");
        startActivityForResult(Intent.createChooser(imgGet, "Pick a photo representing the Game:"));
     */

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

//here is the link on how to radio buttons: http://developer.android.com/guide/topics/ui/controls/radiobutton.html
//by default I have checked the Public radio button
