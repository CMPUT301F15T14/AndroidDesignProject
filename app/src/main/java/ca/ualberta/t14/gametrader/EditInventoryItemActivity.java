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
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.io.FileNotFoundException;
import java.io.InputStream;


public class EditInventoryItemActivity extends Activity {

    private final int PICK_IMAGE = 465132;
    private GameController gc;
    private Game g;
    private EditText gameTitle;
    private Spinner spinConsole;
    private Spinner spinCondition;
    private RadioButton radioShared;
    private RadioButton radioNotShared;
    private EditText additionalInfo;
    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_inventory_item);

        gameTitle = (EditText) findViewById(R.id.inventoryItemTitle);

        spinConsole = (Spinner) findViewById(R.id.gameConsole);
        spinCondition = (Spinner) findViewById(R.id.gameCondition);

        radioShared = (RadioButton) findViewById(R.id.sharePublic);
        radioNotShared = (RadioButton) findViewById(R.id.sharePrivate);

        additionalInfo = (EditText) findViewById(R.id.AddInfoText);

        imageButton = (ImageButton) findViewById(R.id.uploadImage);

        g = (Game) ObjParseSingleton.getInstance().popObject("game");

        gc = new GameController();

        gameTitle.setText(g.getTitle());

        // Assumes ordinal value of initial enum never changed and matches the string.xml array too!
        int i = 0;
        for(Game.Platform p: Game.Platform.values()) {
            if(p.equals(g.getPlatform())) {
                break;
            }
            i += 1;
        }
        spinConsole.setSelection(i);

        // Assumes ordinal value of initial enum never changed and matches the string.xml array too!
        int j = 0;
        for(Game.Condition p: Game.Condition.values()) {
            if(p.equals(g.getCondition())) {
                break;
            }
            j += 1;
        }
        spinCondition.setSelection(j);

        // Assumes the public shared radio button is still selected by default.
        if(!g.isShared()) {
            radioShared.setChecked(false);
            radioNotShared.setChecked(true);
        }

        additionalInfo.setText(g.getAdditionalInfo());

        imageButton.setImageBitmap(g.getPicture());

        addInputEvents();

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

    private void addInputEvents() {

        // image button doesnt work, image picker never launches...

        ((ImageButton) findViewById(R.id.uploadImage)).setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: opens a prompt to select an image from file on phone and then put into Game http://javatechig.com/android/writing-image-picker-using-intent-in-android and http://www.sitepoint.com/web-foundations/mime-types-complete-list/
                // http://developer.android.com/reference/android/content/Intent.html#ACTION_GET_CONTENT
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select photo representing the game:"), PICK_IMAGE);

            }
        });

        ((Button) findViewById(R.id.saveInventory)).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                String gameTitle = ((EditText) findViewById(R.id.inventoryItemTitle)).getText().toString();

                Spinner consoles = (Spinner) findViewById(R.id.gameConsole);
                Spinner conditions = (Spinner) findViewById(R.id.gameCondition);

                String consoleStrEnumId = consoles.getSelectedItem().toString().toUpperCase().replace(" ", "");
                String conditionStrEnumId = conditions.getSelectedItem().toString().toUpperCase().replace(" ", "");

                Game.Platform platform = Game.Platform.valueOf(consoleStrEnumId);
                Game.Condition condition = Game.Condition.valueOf(conditionStrEnumId);

                // if sharePublic is false, that means the only other possible is not share.
                Boolean shareStatus = ((RadioButton) findViewById(R.id.sharePublic)).isChecked();

                String additionalInfo = ((EditText) findViewById(R.id.AddInfoText)).getText().toString();

                gc.editGame(g, gameTitle, null, platform, condition, shareStatus, additionalInfo);

                finish();
            }
        });

    }

                // Taken from http://javatechig.com/android/writing-image-picker-using-intent-in-android
        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case PICK_IMAGE:
                if(resultCode == RESULT_OK){
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        gc.addPhoto(g, selectedImage);
                        imageButton.setImageBitmap(g.getPicture());
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
