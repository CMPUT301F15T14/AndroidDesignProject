package ca.ualberta.t14.gametrader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;


public class SearchPageActivity extends Activity {

    private Spinner gameConsole;
    private Game g;

    private Button SearchButton;

    private ListView gamesList;

    private EditText searchField;

    private ArrayAdapter<String> adapter;
    private ArrayList<String> results = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        gameConsole = (Spinner) findViewById(R.id.gameConsole);

        g = (Game) ObjParseSingleton.getInstance().popObject("game");
        if( g == null) {
            g = new Game();
        }

        int i = 0;
        for(Game.Platform p: Game.Platform.values()) {
            if(p.equals(g.getPlatform())) {
                break;
            }
            i += 1;
        }
        gameConsole.setSelection(i);

        gamesList = (ListView) findViewById(R.id.searchGamesList);
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, results);
        gamesList.setAdapter(adapter);

        searchField = (EditText) findViewById(R.id.searchKey);

        SearchButton = (Button) findViewById(R.id.button);
        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchString = searchField.getText().toString();

                results.clear();

                for(User friend : UserSingleton.getInstance().getUser().getFriends().GetFriends()) {
                    ArrayList<Game> searchResults = new ArrayList<Game>();
                    searchResults.addAll(friend.getInventory().Search(searchString));
                    for(Game result : searchResults) {
                        results.add(result.getTitle() + " : " + friend.getUserName());
                    }
                }
                
                adapter.notifyDataSetChanged();
            }
        });
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
            Intent intent = new Intent(SearchPageActivity.this, SettingActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
