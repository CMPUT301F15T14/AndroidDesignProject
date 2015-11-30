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


public class FriendsListActivity extends Activity {
    private ArrayAdapter<String> adapter;
    private ArrayList<String> friendsArrayList = new ArrayList<String>();

    private ListView friendsListView;

    private FriendsListController friendsListController = new FriendsListController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        friendsListController.initButonOnClickListeners(this, getApplicationContext());

        friendsListView = (ListView) findViewById(R.id.friendsList);


        friendsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // Navigating to InventoryItemActivity.
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {

                // assuming the adapter view order is same as the array game list order
                User u = UserSingleton.getInstance().getUser().getFriends().GetFriends().get(position);

                ObjParseSingleton.getInstance().addObject("userProfile", u); // TODO: pass the right user, not just phone owner!
                Intent myIntent = new Intent(FriendsListActivity.this, ProfileActivity.class);

                startActivityForResult(myIntent, 1);

            }
        });

    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, friendsArrayList);
        friendsListView.setAdapter(adapter);

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
}
