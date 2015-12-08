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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
// adding this comment to see if commits work

public class FriendUpdatesActivity extends Activity {
    private ArrayAdapter<String> adapter;
    private ArrayList<String> updatesList = new ArrayList<String>();

    private ListView updates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_updates);

        updates = (ListView) findViewById(R.id.friendUpdates);
        new checkForUpdates().execute("not much");
    }

    private class checkForUpdates extends AsyncTask<String, Integer, ArrayList<String>> {
        protected ArrayList<String> doInBackground(String... params) {
            FriendsController fc = new FriendsController(UserSingleton.getInstance().getUser().getFriends(), getApplicationContext());
            fc.UpdateFriends();

            ArrayList<String> foundUpdates = new ArrayList<String>();
            String newUpdate = fc.getMostRecentUpdates().poll();
            while(newUpdate != null) {
                foundUpdates.add(newUpdate);
                newUpdate = fc.getMostRecentUpdates().poll();
            }

            return foundUpdates;
        }

        protected void onPostExecute(ArrayList<String> result) {
            super.onPostExecute(result);
            updatesList.addAll(result);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, updatesList);
        updates.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friend_updates, menu);
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
