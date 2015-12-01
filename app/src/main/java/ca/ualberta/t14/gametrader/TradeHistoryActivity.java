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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

public class TradeHistoryActivity extends Activity {

    private ArrayList<String> tradeName = new ArrayList<String>();
    private ArrayList<Trade> myTrades;
    private ListView tradePendingList;
    private ArrayAdapter<String> adapter;
    private NetworkController controller;
    private User user1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_history);

        user1 = new User();

        User user2 = new User();

        Game mario = new Game();
        mario.setTitle("Mario Kart 64");

        Game sonic = new Game();
        sonic.setTitle("Sonic Genesis");

        final Trade trade1 = new Trade(mario, user1, user2);
        trade1.addBorrowerGame(sonic);

        myTrades = new ArrayList<Trade>();
        myTrades.add(trade1);

        tradeName.clear();
        for(Trade each : myTrades) {
            tradeName.add(each.getTradeName());
        }

//        controller = new NetworkController();
//        myTrades = controller.GetMyTrades(user.getAndroidID());
//        new GetTrades().execute(user.getAndroidID());
        tradePendingList = (ListView)findViewById(R.id.tradePendingList);
        //Reference: http://stackoverflow.com/questions/9596663/how-to-make-items-clickable-in-list-view
        tradePendingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // Navigating to InventoryItemActivity.
            public void onItemClick(AdapterView <? > arg0, View view, int position, long id) {

                // assuming the adapter view order is same as the array game list order
                Trade trade = myTrades.get(position);
                ObjParseSingleton.getInstance().addObject("trade", trade);

                Intent myIntent = new Intent(TradeHistoryActivity.this, TradeActivity.class);

                startActivity(myIntent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter = new ArrayAdapter<String>(this, R.layout.list_item,R.id.tradePendingList,tradeName);
        tradePendingList.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trade_history, menu);
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
            Intent intent = new Intent(TradeHistoryActivity.this, SettingActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

/*    private class GetTrades extends AsyncTask<String, Integer, ArrayList<Trade>> {
        @Override
        protected ArrayList<Trade> doInBackground(String... params) {
            NetworkController nc = new NetworkController();
            String userid = params[0];

            ArrayList<Trade> results = nc.GetMyTrades(userid);

            return results;
        }

        protected void onPostExecute(ArrayList<Trade> result) {
            super.onPostExecute(result);
            myTrades = result;
        }
    } */
}

