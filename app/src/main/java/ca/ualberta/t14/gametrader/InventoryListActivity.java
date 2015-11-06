package ca.ualberta.t14.gametrader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


public class InventoryListActivity extends Activity {

    private String mobileArray[]={"Game1","Game2","Game3"};
    private ListView GameList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_list);

        GameList=(ListView)findViewById(R.id.inventoryList);

        Button AddGame= (Button)findViewById(R.id.newInventoryItem);
        //Setting the button helps navigating to AddInventory Activity.
        AddGame.setOnClickListener(new Button.OnClickListener() {
            // Navigating to another activity.
            public void onClick(View arg0) {
                Intent myIntent = new Intent(InventoryListActivity.this, AddInventory.class);
                startActivity(myIntent);
            }
        });

        Button Itemdetail= (Button)findViewById(R.id.GameList);
        //Setting the button helps navigating to InventoryItemActivity.
        Itemdetail.setOnClickListener(new Button.OnClickListener() {
            // Navigating to another activity.
            public void onClick(View arg0) {
                Intent myIntent = new Intent(InventoryListActivity.this, InventoryItemActivity.class);
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
