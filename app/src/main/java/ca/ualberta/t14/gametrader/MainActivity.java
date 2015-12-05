package ca.ualberta.t14.gametrader;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends Activity {
    private ArrayAdapter<String> adapter;
    private ArrayList<String> updatesList = new ArrayList<String>();


    MainMenuController mainMenuController;
    private Button profileButton;

    public Button getProfileButton() {
        return profileButton;
    }

    private Button inventoryButton;

    public Button getInventoryButton() {
        return inventoryButton;
    }

    private Button friendsButton;

    public Button getFriendsButton() {
        return friendsButton;
    }

    private Button tradesButton;

    public Button getTradesButton() {
        return tradesButton;
    }

    private Button searchButton;

    public Button getSearchButton() {
        return searchButton;
    }

    private Button settingsButton;

    public Button getSettingsButton() {
        return settingsButton;
    }

    private ListView updates;

    Handler hand = new Handler();
    static NetworkConnectivity networkConnectivity;
    Boolean isInternetPresent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mainMenuController = new MainMenuController(getApplicationContext());

        mainMenuController.preLoadAllSingletons(getApplicationContext(), this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hand.postDelayed(run, 60000);
        /**
         * Check Internet status
         * Source code is from http://www.androidhive.info/2012/07/android-detect-internet-connection-status/
         * */

        // creating network connection detector instance
        networkConnectivity = new NetworkConnectivity(getApplicationContext());
        isInternetPresent = networkConnectivity.isConnectingToInternet();

        profileButton = (Button) findViewById(R.id.myProfile);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjParseSingleton.getInstance().addObject("userProfile", UserSingleton.getInstance().getUser());
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        inventoryButton = (Button) findViewById(R.id.myInventory);
        inventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjParseSingleton.getInstance().addObject("User", UserSingleton.getInstance().getUser());
                Intent intent = new Intent(MainActivity.this, InventoryListActivity.class);
                startActivity(intent);
            }
        });

        friendsButton = (Button) findViewById(R.id.friends);
        friendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check friends updates
                new checkForUpdates().execute("not much");
                Intent intent = new Intent(MainActivity.this, FriendsListActivity.class);
                startActivity(intent);
            }
        });

        tradesButton = (Button) findViewById(R.id.pendingTrades);
        tradesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TradeHistoryActivity.class);
                startActivity(intent);
            }
        });

        searchButton = (Button) findViewById(R.id.search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchPageActivity.class);
                startActivity(intent);
            }
        });

        settingsButton = (Button) findViewById(R.id.setting);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        updates = (ListView) findViewById(R.id.friendUpdates);

        new checkForUpdates().execute("GetFriendsUpdates");
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, updatesList);
        updates.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
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

    Runnable run = new Runnable() {
        @Override
        public void run() {
           // get Internet status
            isInternetPresent = networkConnectivity.isConnectingToInternet();
            new checkForUpdates().execute("GetFriendsUpdates");
            hand.postDelayed(run, 60000);
        }
    };

}