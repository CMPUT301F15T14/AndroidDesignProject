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
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

public class CreateTradeActivity extends Activity {
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
    private Trade currentTrade;
    private CreateTradeController createTradeController;
    private User tradingWith;
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

        createTradeController = new CreateTradeController(getApplicationContext(), this);

        //  Array reserved for storing names of game.
        mobileArray = new ArrayList<String>();
        // later add observer observing the inventory:
        mobileArray.clear();

        //  Array reserved for storing names of game.
        mobileArray1 = new ArrayList<String>();
        // later add observer observing the inventory:
        mobileArray1.clear();

        Trade trade = (Trade) ObjParseSingleton.getInstance().popObject("trade");
        if (trade == null) {
            g1 = (Game) ObjParseSingleton.getInstance().popObject("tradegame");
            if (g1 == null) {
                throw new RuntimeException("Null game passed to trade activity.");
            }
            g2 = (Game) ObjParseSingleton.getInstance().popObject("offergame");
            tradingWith = (User) ObjParseSingleton.getInstance().popObject("tradeGameOwner");
            if (tradingWith == null) {
                throw new RuntimeException("Trade activity was not passed a user.");
            }
            currentTrade = new Trade(g1, new User(), new User());
            mobileArray.add(g1.getTitle());
            try {
                currentTrade.loadJson("currentTrade", getBaseContext());
            } catch (IOException e) {
                e.printStackTrace();
            }
            createTradeController.setOwnerToTrade(currentTrade, tradingWith);
            createTradeController.setBorrowerToTrade(currentTrade, UserSingleton.getInstance().getUser());
        }
        else {
            currentTrade = trade;
            // Get all Owners' offers
            for (Game each: trade.getOwnerOffers()) {
                mobileArray.add(each.getTitle());
            }
            // Get all Borrowser's offers
            for (Game each: trade.getBorrowerOffers()) {
                mobileArray1.add(each.getTitle());
            }

        }

        GameAskList=(ListView)findViewById(R.id.tradeFor);
        GameOfferList =(ListView)findViewById(R.id.tradeOffer);

        adapter=new ArrayAdapter<String>(this,R.layout.text_view,R.id.GameList,mobileArray);
        GameAskList.setAdapter(adapter);
        adapter1=new ArrayAdapter<String>(this,R.layout.text_view,R.id.GameList,mobileArray1);
        GameOfferList.setAdapter(adapter1);

        offerGameButton = (Button) findViewById(R.id.offerGame);
        offerGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateTradeActivity.this, InventoryListActivity.class);
                ObjParseSingleton.getInstance().addObject("User", UserSingleton.getInstance().getUser());
                ObjParseSingleton.getInstance().addObject("isInTrade", new Boolean(Boolean.TRUE));
                startActivityForResult(intent, 2);
            }
        });

        offerTradeButton = (Button) findViewById(R.id.makeTrade);
        offerTradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // push to network que
                TradeNetworkerSingleton.getInstance().getTradeNetMangager().addTradeToUploadList(currentTrade, UserSingleton.getInstance().getUser(), getApplicationContext());
                Toast.makeText(getApplicationContext(), "Trade submitted!", Toast.LENGTH_SHORT).show();
                finish();

            }
        });

        Button cancelTradeButton = (Button) findViewById(R.id.cancelTrade);
        cancelTradeButton.setOnClickListener( new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ObjParseSingleton.getInstance().keywordExists("isInTrade")) {
                    ObjParseSingleton.getInstance().popObject("isInTrade");
                }
                finish();
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
            Intent intent = new Intent(CreateTradeActivity.this, SettingActivity.class);
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
            createTradeController.borrowerAddGame(currentTrade, offer);

            Log.d("arraysize", ""+mobileArray1.size());
            Log.d("array",mobileArray1.get(0));
            adapter1.notifyDataSetChanged();
            //TODO: not only add title, add game object to list
        }
    }

}
