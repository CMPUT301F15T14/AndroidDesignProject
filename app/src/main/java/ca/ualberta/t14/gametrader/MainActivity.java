package ca.ualberta.t14.gametrader;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    User user = new User(); // the constructor automatically handles loading of saved data

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProfileController profCon = new ProfileController(user);
        profCon.SaveProfileEdits("n00bpwnr", "pwnsnoobs@gmail.com", "603 30 Carleton Avenue", "587-877-2072");

        profileButton = (Button) findViewById(R.id.myProfile);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                //intent.putExtra("User", user);
                startActivity(intent);
            }
        });

        inventoryButton = (Button) findViewById(R.id.myInventory);
        inventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                Intent intent = new Intent(MainActivity.this, InventoryListActivity.class);
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
