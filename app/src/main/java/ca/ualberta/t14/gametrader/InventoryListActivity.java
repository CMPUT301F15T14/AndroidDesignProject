package ca.ualberta.t14.gametrader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class InventoryListActivity extends Activity {

    private ArrayList<String> mobileArray;
    private ListView GameList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_list);

        mobileArray = new ArrayList<String>();
        //later add observer observing the inventory:
        mobileArray.clear();
        for(Game each : UserSingleton.getInstance().getUser().getInventory().getAllGames()) {
            mobileArray.add(each.getTitle());
        }

        GameList=(ListView)findViewById(R.id.inventoryList);
        //Reference: http://stackoverflow.com/questions/9596663/how-to-make-items-clickable-in-list-view
        GameList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // Navigating to InventoryItemActivity.
            public void onItemClick(AdapterView <? > arg0, View view, int position, long id) {

                // assuming the adapter view order is same as the array game list order
                Game g = UserSingleton.getInstance().getUser().getInventory().getAllGames().get(position);
                ObjParseSingleton.getInstance().addObject("game", g);

                Intent myIntent = new Intent(InventoryListActivity.this, InventoryItemActivity.class);

                startActivity(myIntent);
            }
        });

        Button AddGame= (Button)findViewById(R.id.newInventoryItem);
        //SettingActivity the button helps navigating to AddInventory Activity.
        AddGame.setOnClickListener(new Button.OnClickListener() {
            // Navigating to another activity.
            public void onClick(View arg0) {

                GameController gc = new GameController();
                Game freshGame = gc.createGame(UserSingleton.getInstance().getUser());
                ObjParseSingleton.getInstance().addObject("game", freshGame);

                Intent myIntent = new Intent(InventoryListActivity.this, EditInventoryItemActivity.class);

                startActivity(myIntent);

            }
        });


    }

    @Override
    protected void onStart(){
        super.onStart();
        adapter=new ArrayAdapter<String>(this, R.layout.text_view,R.id.GameList,mobileArray);
        GameList.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mobileArray.clear();
        for(Game each : UserSingleton.getInstance().getUser().getInventory().getAllGames()) {
            mobileArray.add(each.getTitle());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inventory_list, menu);
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
