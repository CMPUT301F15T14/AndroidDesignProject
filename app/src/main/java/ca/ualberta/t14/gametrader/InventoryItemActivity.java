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
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.FileNotFoundException;

public class InventoryItemActivity extends Activity implements AppObserver {

    Game game;
    InventoryItemController inventoryItemController;
    User ownerProfile;

    public String getGameTitle() {
        return gameTitle.getText().toString();
    }

    private TextView gameTitle;

    public String  getPlatform() {
        return platform.getText().toString();
    }

    private TextView platform;
    TextView condition;
    TextView owner;
    TextView additionalInfo;
    TextView phone;
    TextView address;
    private Button editGame;

    public Button getEditGame() {
        return editGame;
    }

    ImageButton imageButton;


    Gson gson = new Gson();
    //public static final int tradeItemSelected = 100;
    public static final int offerItemSelected = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_item);

        game = (Game) ObjParseSingleton.getInstance().popObject("game");
        if( game == null) {
            game = new Game();
        }

        game.addObserver(this);

        PictureNetworkerSingleton.getInstance().getPicNetMangager().addObserver(this);

        ownerProfile = (User) ObjParseSingleton.getInstance().popObject("gameOwner");
        if(ownerProfile == null) {
            System.err.print("InventoryItemActivity was NOT passed a user, somehow lost mainUser!");
            finish();
            return;
        }

        inventoryItemController = new InventoryItemController(ownerProfile.getInventory());

        gameTitle = (TextView) findViewById(R.id.gameInfoTitle);
        platform = (TextView) findViewById(R.id.gameInfoConsole);
        condition = (TextView) findViewById(R.id.gameInfoCondition);
        owner = (TextView) findViewById(R.id.gameInfoOwner);
        additionalInfo = (TextView) findViewById(R.id.additionalInfoText);
        imageButton = (ImageButton) findViewById(R.id.inventoryItemImage);
        phone = (TextView) findViewById(R.id.phoneEditField);
        address = (TextView) findViewById(R.id.contactAddress);

        gameTitle.setText(game.getTitle());
        platform.setText(game.getPlatform().toString());
        condition.setText(game.getCondition().toString());
        owner.setText(ownerProfile.getUserName());
        phone.setText(ownerProfile.getPhoneNumber());
        address.setText(ownerProfile.getAddress());
        additionalInfo.setText(game.getAdditionalInfo());
        // Important, have to load bitmap from it's json first! Because bitmap is volatile.
        String imageJson  = new String();
        try {
            imageJson =  PictureManager.loadImageJsonFromJsonFile(game.getFirstPictureId(), getApplicationContext());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            PictureNetworkerSingleton.getInstance().getPicNetMangager().getLocalCopyOfImageIds().remove(game.getFirstPictureId());
        }

        Boolean isInTrade = ObjParseSingleton.getInstance().keywordExists("isInTrade");
        Button tradeItem = (Button) findViewById(R.id.tradeButton);
        Button offerItem = (Button) findViewById(R.id.offerMyItemButton);

        if(UserSingleton.getInstance().getUser().getInventory().contains(game) && isInTrade) {
            // Show offerItem ONLY if game belongs to device user AND currently in a trade session.
            offerItem.setVisibility(View.VISIBLE);
            offerItem.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    Intent myIntent = new Intent(InventoryItemActivity.this, CreateTradeActivity.class);
                    myIntent.putExtra("offeredItem", gson.toJson(game));

                    ObjParseSingleton.getInstance().popObject("isInTrade");
                    setResult(offerItemSelected, myIntent);
                    finish();

                    //TODO: add item back if trade is cancelled
                }
            });
            tradeItem.setVisibility(View.INVISIBLE);
        } else if (ownerProfile != UserSingleton.getInstance().getUser()){
            // Trade item ONLY if the game currently viewing is not the current device user.
            tradeItem.setVisibility(View.VISIBLE);
            tradeItem.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    ObjParseSingleton.getInstance().addObject("tradegame", game);
                    ObjParseSingleton.getInstance().addObject("tradeGameOwner", ownerProfile);

                    Intent myIntent = new Intent(InventoryItemActivity.this, CreateTradeActivity.class);

                    startActivity(myIntent);
                }
            });
            offerItem.setVisibility(View.INVISIBLE);
        } else {
            // not in a trading or friendInventory session at all.
            tradeItem.setVisibility(View.INVISIBLE);
            offerItem.setVisibility(View.INVISIBLE);
        }

        editGame = (Button)findViewById(R.id.buttonEditItem);
        if (!inventoryItemController.clonable(ownerProfile)){
            editGame.setText("Edit");
        }else{
            editGame.setText("Clone");
        }
        editGame.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (!inventoryItemController.clonable(ownerProfile)){
                    ObjParseSingleton.getInstance().addObject("game", game);
                    Intent myIntent = new Intent(InventoryItemActivity.this, EditInventoryItemActivity.class);
                    startActivityForResult(myIntent, 1);
                }else{
                    inventoryItemController.clone(game,getApplicationContext());
                    Toast.makeText(InventoryItemActivity.this, "Game has been cloned to your inventory!", Toast.LENGTH_SHORT).show();

                };
            }
        });

        imageButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                ObjParseSingleton.getInstance().addObject("game", game);
                Intent myIntent = new Intent(InventoryItemActivity.this, InventoryItemPictureViewer.class);
                startActivityForResult(myIntent, 1);
            }
        });

        inventoryItemController.tryDownloadImages(game, getApplicationContext());
        if(!imageJson.isEmpty()) {
            inventoryItemController.setImageToImageButtons(game, imageButton, this);
        } else {
            imageButton.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cd_empty));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 1 && resultCode == RESULT_OK){
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inventory_item, menu);
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
            Intent intent = new Intent(InventoryItemActivity.this, SettingActivity.class);
            startActivity(intent);        }

        return super.onOptionsItemSelected(item);
    }

    public void ViewProfile(View v) {
        Intent intent = new Intent(InventoryItemActivity.this, ProfileActivity.class);
        ObjParseSingleton.getInstance().addObject("userProfile", ownerProfile);
        startActivity(intent);
    }


    public void appNotify(AppObservable observable) {
        inventoryItemController.setImageToImageButtons(game, imageButton, this);
        gameTitle.setText(game.getTitle());
        platform.setText(game.getPlatform().toString());
        condition.setText(game.getCondition().toString());
        owner.setText(ownerProfile.getUserName());
        phone.setText(ownerProfile.getPhoneNumber());
        address.setText(ownerProfile.getAddress());
        additionalInfo.setText(game.getAdditionalInfo());
    }

}
