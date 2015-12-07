package ca.ualberta.t14.gametrader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by michaelximac on 2015-12-05.
 */
public class AddNewFriendActivity extends Activity implements AppObserver{
    private ArrayAdapter<String> adapter;
    private ArrayList<String> friendsArrayList = new ArrayList<String>();
    private NetworkController network;
    private EditText searchString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UserSingleton.getInstance().getUser().getFriends().addObserver(this);
        network=new NetworkController(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_friend);
        //adapter.notifyDataSetChanged();
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
