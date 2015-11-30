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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class TradeHistoryActivity extends Activity {

    private ArrayList<String> tradeName;
    private ListView tradePendingList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_history);

        tradeName = new ArrayList<String>();
        //TODO: to fill tradeName, would you search through the database for trades that contain the user id?
/*       tradeName.clear();
        for(Game each : UserSingleton.getInstance().getUser().getInventory().getAllGames()) {
            tradeName.add(each.getTitle());
        } */

        tradePendingList = (ListView)findViewById(R.id.tradePendingList);
        //Reference: http://stackoverflow.com/questions/9596663/how-to-make-items-clickable-in-list-view
        tradePendingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // Navigating to InventoryItemActivity.
            public void onItemClick(AdapterView <? > arg0, View view, int position, long id) {

                // assuming the adapter view order is same as the array game list order
//                Game g = UserSingleton.getInstance().getUser().getInventory().getAllGames().get(position);
//                ObjParseSingleton.getInstance().addObject("game", g);

                Intent myIntent = new Intent(TradeHistoryActivity.this, InventoryItemActivity.class);

                startActivity(myIntent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter = new ArrayAdapter<String>(this, R.layout.text_view,R.id.tradePendingList,tradeName);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
