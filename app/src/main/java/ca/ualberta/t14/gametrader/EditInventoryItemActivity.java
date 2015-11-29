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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;


public class EditInventoryItemActivity extends Activity {

    private final int PICK_IMAGE = 465132;
    private GameController gc;
    private Uri imageUri;
    private Game g;
    private EditText gameTitle;
    private Spinner spinConsole;
    private Spinner spinCondition;
    private RadioButton radioShared;
    private RadioButton radioNotShared;
    private EditText additionalInfo;
    private ImageButton imageButton;
    private Button save;
    private Button delete;

    public Button getSaveButton() {
        return save;
    }

    public EditText getGameTitle() {
        return gameTitle;
    }

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
        if( g == null) {
            g = new Game();
        }

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
        deleteItem();
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
            Intent intent = new Intent(EditInventoryItemActivity.this, SettingActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void addInputEvents() {

        // image button doesnt work, image picker never launches...

        ((ImageButton) findViewById(R.id.uploadImage)).setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (g.hasPictureId()){
                    selectPicture();
                }else{
                    AlertDialog SinglePrompt = new AlertDialog.Builder(EditInventoryItemActivity.this).create();
                    SinglePrompt.setTitle("Warning");
                    SinglePrompt.setMessage("Do you want to delete or reselect the picture?");
                    SinglePrompt.setButton(AlertDialog.BUTTON_POSITIVE, "Delete", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    deletePicture();
                                    Toast.makeText(EditInventoryItemActivity.this, "Picture Deleted!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }
                    );
                    SinglePrompt.setButton(AlertDialog.BUTTON_NEGATIVE, "Reselect", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    selectPicture();
                                    dialog.dismiss();
                                }
                            }
                    );
                    SinglePrompt.show();
                }
            }
        });

        save=(Button) findViewById(R.id.saveInventory);
        save.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(EditInventoryItemActivity.this, "Saving...", Toast.LENGTH_SHORT).show();

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

                // Save game data
                gc.editGame(g, gameTitle, platform, condition, shareStatus, additionalInfo);

                // Save picture of game
                gc.addPhoto(g, imageUri, getContentResolver(), getApplicationContext());

                // Save user to JSON. The user contains Inventory which contains the item.
                UserSingleton.getInstance().getUser().saveJson("MainUserProfile", getApplicationContext());
                UserSingleton.getInstance().getUser().notifyAllObservers();

                Toast.makeText(EditInventoryItemActivity.this, "Game Saved!", Toast.LENGTH_SHORT).show();
                //setResult(RESULT_CANCELED);
                finish();
            }
        });

    }

    private void selectPicture(){
        // TODO: opens a prompt to select an image from file on phone and then put into Game http://javatechig.com/android/writing-image-picker-using-intent-in-android and http://www.sitepoint.com/web-foundations/mime-types-complete-list/
        // http://developer.android.com/reference/android/content/Intent.html#ACTION_GET_CONTENT
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select photo representing the game:"), PICK_IMAGE);
    }

    private void deleteItem(){
        delete=(Button) findViewById(R.id.deleteInventory);
        delete.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v){
                AlertDialog SinglePrompt = new AlertDialog.Builder(EditInventoryItemActivity.this).create();
                SinglePrompt.setTitle("Warning");
                SinglePrompt.setMessage("Are you sure you want to delete this item?");
                SinglePrompt.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                InventoryController ic = new InventoryController(UserSingleton.getInstance().getUser().getInventory());
                                ic.removeItem(g);
                                UserSingleton.getInstance().getUser().saveJson("MainUserProfile", getApplicationContext());
                                UserSingleton.getInstance().getUser().notifyAllObservers();
                                Toast.makeText(EditInventoryItemActivity.this, "Game Deleted!", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finish();
                                dialog.dismiss();
                            }
                        }
                );

                SinglePrompt.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }
                );

                SinglePrompt.show();
            }
        });
    }

    private void deletePicture(){
        g.removePictureId(getApplicationContext());
        imageButton.setImageResource(android.R.color.transparent);
    }

    // Taken from http://javatechig.com/android/writing-image-picker-using-intent-in-android
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case PICK_IMAGE:
                if(resultCode == RESULT_OK){
                    imageUri = imageReturnedIntent.getData();
                    imageButton.setImageBitmap( gc.resolveUri(imageUri, getContentResolver()) );
                }
                break;
        }
    }

}

