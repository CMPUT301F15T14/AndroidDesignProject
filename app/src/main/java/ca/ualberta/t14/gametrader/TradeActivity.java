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
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.io.IOException;
import java.util.ArrayList;

public class TradeActivity extends Activity {
    private Spinner offerGameforTrade;
    ArrayList<String> spinnerArray =  new ArrayList<String>();

    private ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
    //private Button offerGameButton;

    private ArrayList<String> mobileArray;
    private ArrayAdapter<String> adapter;
    private ListView GameList;
    Game g1;
    Button offerGameButton;
    Button tradeAskButton;
    Button cancelTradeButtom;

    public ListView getGameList() {
        return GameList;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade);
        g1 = (Game) ObjParseSingleton.getInstance().popObject("game");
        if( g1 == null) {
            g1 = new Game();
        }

        //  Array reserved for storing names of game.
        mobileArray = new ArrayList<String>();
        // later add observer observing the inventory:
        mobileArray.clear();
        mobileArray.add(g1.getTitle());

        GameList=(ListView)findViewById(R.id.tradeFor);
        /*
        // get this from Ryan's part Inventory item load, guess it should be loaded from Json
        // all info should be save in Json before and get it here.
<<<<<<< HEAD
        //-------------------------------------------------------*/

        /*offerGameButton = (Button) findViewById(R.id.offerGame);
        offerGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TradeActivity.this, InventoryItemActivity.class);
                startActivity(intent);
            }
        });*/
        offerGameforTrade = (Spinner)findViewById(R.id.offerGame);


        //-------------------------------------------------------
        User mainUser = UserSingleton.getInstance().getUser();
        try {
            mainUser = (User) mainUser.loadJson("MainUserProfile", getApplicationContext());
            UserSingleton.getInstance().setUser(mainUser);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //  Array reserved for storing names of game.
        mobileArray = new ArrayList<String>();
        // later add observer observing the inventory:
        mobileArray.clear();
        for(Game each : UserSingleton.getInstance().getUser().getInventory().getAllGames()) {
            mobileArray.add(each.getTitle());
        }

        offerGameButton = (Button) findViewById(R.id.offerGame);
        offerGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TradeActivity.this, InventoryListActivity.class);
                startActivity(intent);
            }
        });
        tradeAskButton = (Button) findViewById(R.id.tradeAsk);
        tradeAskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TradeActivity.this, InventoryListActivity.class);
                startActivity(intent);
            }
        });
        /*
        //cannot work becaseu the nullpointer exception in InventoyItemActivity line 56,57,58
        cancelTradeButtom = (Button) findViewById(R.id.cancelTrade);
        cancelTradeButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TradeActivity.this, InventoryItemActivity.class);
                startActivity(intent);
            }
        });
        */
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trade, menu);
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
            Intent intent = new Intent(TradeActivity.this, SettingActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart(){
        super.onStart();
        adapter=new ArrayAdapter<String>(this,R.layout.text_view,R.id.GameList,mobileArray);
        GameList.setAdapter(adapter);
    }
    /*
    @Override
    public void onResume() {
        super.onResume();
        mobileArray.clear();
        mobileArray.add(g1.getTitle());
    }
    */
}
