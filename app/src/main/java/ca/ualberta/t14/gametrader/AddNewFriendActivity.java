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
import android.widget.EditText;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by michaelximac on 2015-12-05.
 */
public class AddNewFriendActivity extends Activity implements AppObserver{
    private ArrayAdapter<String> adapter;
    private NetworkController network;
    private EditText searchText;
    private Button searchButton;
    private ListView UserList;
    private ArrayList<User> searchResults;

    private ArrayList<String> results = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        network=new NetworkController(getApplicationContext());
        network.addObserver(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_friend);

        searchText=(EditText)findViewById(R.id.searchKey);
        searchButton=(Button)findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchString = searchText.getText().toString();

                results.clear();
                searchResults = new ArrayList<User>();
                new searchFriends().execute(searchString);

            }
        });
        UserList=(ListView)findViewById(R.id.friendsList);

        UserList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                User u = searchResults.get(position);
                ObjParseSingleton.getInstance().addObject("userProfile", u);
                Intent myIntent = new Intent(AddNewFriendActivity.this, ProfileActivity.class);
                startActivity(myIntent);
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        UserList=(ListView)findViewById(R.id.friendsList);
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, results);
        UserList.setAdapter(adapter);
    }

    @Override
    public void onResume(){
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friends_list, menu);
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
            Intent intent = new Intent(AddNewFriendActivity.this, SettingActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void appNotify(AppObservable observable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    private class searchFriends extends AsyncTask<String, Integer, ArrayList<User>> {

        protected ArrayList<User> doInBackground(String... params) {

            String userName = params[0];

            try{
                ArrayList<User> results = network.searchByUserName(userName);

                if(!results.isEmpty()) {
                    return results;
                }
            }
            catch(IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(ArrayList<User> result) {
            super.onPostExecute(result);
            if(result != null) {
                searchResults=result;
            }
            for (User resultname : searchResults) {
                results.add(resultname.getUserName());
            }
            adapter.notifyDataSetChanged();
        }
    }

}
