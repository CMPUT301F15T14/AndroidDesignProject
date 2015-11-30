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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

public class EditTradeActivity extends Activity {
    //private Spinner offerGameforTrade;
    //ArrayList<String> spinnerArray =  new ArrayList<String>();

    //private ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
    //private Button offerGameButton;

    private ArrayList<String> mobileArray;
    private ArrayList<String> mobileArray1;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adapter1;
    private ListView GameAskList;
    private ListView GameOfferList;
    Game game;
    Game g1;
    Game g2;
    Button offerGameButton;
    Button offerTradeButton;

    public ListView getGameAskList() {
        return GameAskList;
    }
    public ListView getGameOfferList(){
        return GameOfferList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade);

        g1 = (Game) ObjParseSingleton.getInstance().popObject("tradegame");
        if( g1 == null) {
            throw new RuntimeException("Null game passed to trade activity.");
        }
        User tradingWith = (User)ObjParseSingleton.getInstance().popObject("tradeGameOwner");
        if(tradingWith == null){
            throw new RuntimeException("Trade activity was not passed a user.");
        }
        Trade currentTrade = new Trade(new Game(), new User(), new User());
        try {
            currentTrade.loadJson("currentTrade", getBaseContext());
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        if(currentTrade == null) {

        }

        //  Array reserved for storing names of game.
        mobileArray = new ArrayList<String>();
        // later add observer observing the inventory:
        mobileArray.clear();
        mobileArray.add(g1.getTitle());


        //  Array reserved for storing names of game.
        mobileArray1 = new ArrayList<String>();
        // later add observer observing the inventory:
        mobileArray1.clear();

        g2 = (Game) ObjParseSingleton.getInstance().popObject("offergame");
        if (g2 != null) {
            mobileArray1.add(g2.getTitle());
        }

        GameAskList=(ListView)findViewById(R.id.tradeFor);
        GameOfferList =(ListView)findViewById(R.id.tradeOffer);
        User mainUser = UserSingleton.getInstance().getUser();
        try {
            mainUser = (User) mainUser.loadJson("MainUserProfile", getApplicationContext());
            UserSingleton.getInstance().setUser(mainUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
        adapter=new ArrayAdapter<String>(this,R.layout.text_view,R.id.GameList,mobileArray);
        GameAskList.setAdapter(adapter);
        adapter1=new ArrayAdapter<String>(this,R.layout.text_view,R.id.GameList,mobileArray1);
        GameOfferList.setAdapter(adapter1);


        offerGameButton = (Button) findViewById(R.id.offerGame);
        offerGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditTradeActivity.this, InventoryListActivity.class);
                ObjParseSingleton.getInstance().addObject("User", UserSingleton.getInstance().getUser());
                startActivityForResult(intent, 2);
            }
        });


        offerTradeButton = (Button) findViewById(R.id.makeTrade);
        offerTradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditTradeActivity.this, TradeHistoryActivity.class);
                startActivity(intent);

            }
        });



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
            Intent intent = new Intent(EditTradeActivity.this, SettingActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart(){
        super.onStart();

    }




    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == InventoryItemActivity.offerItemSelected){
            Gson gson = new Gson();
            Game offer = gson.fromJson(data.getStringExtra("offeredItem"), Game.class);
            Log.d("trade", data.getStringExtra("offeredItem"));
            Log.d("title", offer.getTitle());
            mobileArray1.add(offer.getTitle());
            Log.d("arraysize", ""+mobileArray1.size());
            Log.d("array",mobileArray1.get(0));
            adapter1.notifyDataSetChanged();
            //TODO: not only add title, add game object to list
        }
    }

}