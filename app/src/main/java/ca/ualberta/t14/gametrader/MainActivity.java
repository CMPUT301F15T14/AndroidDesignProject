package ca.ualberta.t14.gametrader;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends Activity {
    User user = new User();

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

    NetworkController net;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            user = (User) user.loadJson("MainUserProfile", getApplicationContext());
            user.setAndroidID(Settings.Secure.getString(getContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID));
            UserSingleton.getInstance().setUser(user);
        } catch (IOException e) {
            e.printStackTrace();
        }

        UserSingleton.getInstance().getUser().addObserver(net);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                //ObjParseSingleton.getInstance().addObject("userProfile", UserSingleton.getInstance().getUser());
                Intent intent = new Intent(MainActivity.this, InventoryListActivity.class);
                startActivity(intent);
            }
        });

        friendsButton = (Button) findViewById(R.id.friends);
        friendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
