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

import java.util.ArrayList;

public class TradeEditActivity extends Activity {

    private TradingEditController tradingEditController;

    private ListView tradeFor;
    private ArrayAdapter<String> tradeForAdapter;

    private ListView tradeOffer;
    private ArrayAdapter<String> tradeOfferAdapter;

    private ArrayList<String> ownerGame;
    private ArrayList<String> borrowerGame;
    private Button editTrade;
    private Button deleteTrade;

    private Trade trade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade2);

        tradeFor = (ListView)findViewById(R.id.tradeFor);
        tradeOffer = (ListView)findViewById(R.id.tradeOffer);

        trade = (Trade) ObjParseSingleton.getInstance().popObject("trade");
        tradingEditController = new TradingEditController(trade, getApplicationContext(), this);

        ownerGame = new ArrayList<String>();
        ownerGame.clear();
        for (Game each: trade.getOwnerOffers()) {
            ownerGame.add(each.getTitle());
        }

        borrowerGame = new ArrayList<String>();
        borrowerGame.clear();
        for (Game each: trade.getBorrowerOffers()) {
            borrowerGame.add(each.getTitle());
        }

        editTrade = (Button)findViewById(R.id.editTrade);
        editTrade.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                ObjParseSingleton.getInstance().addObject("trade", trade);
                Intent myIntent = new Intent(TradeEditActivity.this, CreateTradeActivity.class);
                finish();
                startActivity(myIntent);
            }
        });

        deleteTrade = (Button)findViewById(R.id.deleteTrade);
        deleteTrade.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                tradingEditController.deleteTrade(trade);
                finish();
            }
        });

        // show buttons for approriate users only
        // US04.06.01 As a borrower, when composing a trade or counter-trade I can delete the trade
        // US04.05.01 As a borrower or owner, when composing a trade or counter-trade I can edit the trade.
        if(trade.getBorrower().compareTo(UserSingleton.getInstance().getUser().getAndroidID()) == 0) {
            editTrade.setVisibility(View.VISIBLE);
            deleteTrade.setVisibility(View.VISIBLE);
        } else {
            editTrade.setVisibility(View.INVISIBLE);
            deleteTrade.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trade, menu);
        return true;
    }
    @Override
    public void onStart() {
        super.onStart();
        tradeForAdapter = new ArrayAdapter<String>(this, R.layout.text_view,R.id.GameList,ownerGame);
        tradeFor.setAdapter(tradeForAdapter);

        tradeOfferAdapter = new ArrayAdapter<String>(this, R.layout.text_view,R.id.GameList,borrowerGame);
        tradeOffer.setAdapter(tradeOfferAdapter);
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
