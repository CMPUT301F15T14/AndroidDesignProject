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
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


public class FriendsListActivity extends Activity implements AppObserver {
    private ArrayAdapter<String> adapter;
    private ArrayList<String> friendsArrayList = new ArrayList<String>();
    private Button AddNewFriend;
    private ListView friendsListView;

    //private FriendsListController friendsListController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UserSingleton.getInstance().getUser().getFriends().addObserver(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        //friendsListController = new FriendsListController(getApplicationContext());

        AddNewFriend=(Button)findViewById(R.id.addFriendButton);
        AddNewFriend.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View arg0){
                Intent myIntent = new Intent(FriendsListActivity.this, AddNewFriendActivity.class);
                startActivity(myIntent);
            }
        });

        friendsListView = (ListView) findViewById(R.id.friendsList);

        adapter = new ArrayAdapter<String>(this, R.layout.list_item, friendsArrayList);
        friendsListView.setAdapter(adapter);

        friendsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // Navigating to InventoryItemActivity.
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {

                // assuming the adapter view order is same as the array game list order
                User u = UserSingleton.getInstance().getUser().getFriends().GetFriends().get(position);

                ObjParseSingleton.getInstance().addObject("userProfile", u);
                Intent myIntent = new Intent(FriendsListActivity.this, ProfileActivity.class);

                startActivityForResult(myIntent, 1);

            }
        });

        friendsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long id) {

                // assuming the adapter view order is same as the array game list order
                User u = UserSingleton.getInstance().getUser().getFriends().GetFriends().get(position);

                FriendsController fc = new FriendsController(UserSingleton.getInstance().getUser().getFriends(), getApplicationContext());
                fc.RemoveFriend(u);

                friendsArrayList.remove(position);
                adapter.notifyDataSetChanged();

                return true;
            }
        });

        friendsArrayList.clear();
        for(User friend : UserSingleton.getInstance().getUser().getFriends().GetFriends()){
            friendsArrayList.add(friend.getUserName());
            Log.d("friend", "" + friendsArrayList.size());

        }
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
            Intent intent = new Intent(FriendsListActivity.this, SettingActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void appNotify(AppObservable observable) {
        friendsArrayList.clear();

        for(User friend : UserSingleton.getInstance().getUser().getFriends().GetFriends()){
            friendsArrayList.add(friend.getUserName());
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }
}
