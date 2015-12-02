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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class TradeHistoryActivity extends Activity implements AppObserver {

    private ArrayList<String> tradeName;
    private ArrayList<Trade> myTrades;
    private ListView tradePendingList;
    private ArrayAdapter<String> adapter;
    private NetworkController controller;
    private User user1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_history);

        tradeName = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(this, R.layout.list_item,R.id.listText,tradeName);

        TradeNetworkerSingleton.getInstance().getTradeNetMangager().addObserver(this);

        updateAdapterLists(Boolean.TRUE);

//        controller = new NetworkController();
//        myTrades = controller.GetMyTrades(user.getAndroidID());
//        new GetTrades().execute(user.getAndroidID());
        tradePendingList = (ListView)findViewById(R.id.tradePendingList);
        //Reference: http://stackoverflow.com/questions/9596663/how-to-make-items-clickable-in-list-view
        tradePendingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // Navigating to InventoryItemActivity.
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {

                // assuming the adapter view order is same as the array game list order
                Trade trade = myTrades.get(position);
                ObjParseSingleton.getInstance().addObject("trade", trade);

                Intent myIntent = new Intent(TradeHistoryActivity.this, TradeEditActivity.class);

                startActivity(myIntent);
            }
        });
        tradePendingList.setAdapter(adapter);
    }

    private void updateAdapterLists(Boolean updateLocalList) {
        myTrades = new ArrayList<Trade>();

        // first list all trades that have yet to be pushed to the network
        ArrayList<Trade> toPushTrades = TradeNetworkerSingleton.getInstance().getTradeNetMangager().getTradeToUpload();
        for(Trade each : toPushTrades) {
            myTrades.add(each);
        }

        ArrayList<Trade> tradesOnline = TradeNetworkerSingleton.getInstance().getTradeNetMangager().getAllTradesOnNet(updateLocalList);
        if(tradesOnline != null) {
            myTrades.addAll(tradesOnline);
        }

        tradeName.clear();
        for(Trade each : myTrades) {
            tradeName.add(each.getTradeName());
        }

        // TODO: adapter in trade history crashes too for some reason. If this commented out, then to update it is u have to go back and open the activity again..
        if(adapter != null) {
            adapter.notifyDataSetChanged();
        }

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

    @Override
    public void appNotify(AppObservable observable) {
        // This appNotify gets called by an async elastic search query.
        // Adapter notifyDataSetChanged Has to be run on ui activity! Else errors.
        Runnable r = new Runnable() {
            @Override
            public void run() {
                updateAdapterLists(Boolean.FALSE);
            }
        };
        runOnUiThread(r);
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

