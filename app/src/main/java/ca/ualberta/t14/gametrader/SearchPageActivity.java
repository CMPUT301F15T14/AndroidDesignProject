package ca.ualberta.t14.gametrader;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class SearchPageActivity extends Activity {
    private ArrayList<String> mobileArray;

    private Button searchButton;
    private EditText searchText;
    private ListView resultsList;
    private ArrayAdapter<String> adapter;

    NetworkController net = new NetworkController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        searchText = (EditText)findViewById(R.id.searchKey);
        resultsList = (ListView)findViewById(R.id.searchList);

        mobileArray = new ArrayList<String>();
        mobileArray.clear();

        searchButton = (Button)findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                String qry = searchText.getText().toString();
                ArrayList<Game> results = net.SearchGames(qry);

                for(Game result: results){
                    mobileArray.add(result.getTitle());
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter=new ArrayAdapter<String>(this, R.layout.activity_inventory_list,R.id.inventoryList,mobileArray);
        resultsList.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_page, menu);
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
