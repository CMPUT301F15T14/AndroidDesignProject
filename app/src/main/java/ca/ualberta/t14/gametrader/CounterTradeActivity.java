package ca.ualberta.t14.gametrader;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class CounterTradeActivity extends Activity {

    Trade trade;
    User user;
    Button offerGameButton;
    private TradingEditController tradingEditController;
    private ListView tradeFor;
    private ArrayAdapter<String> tradeForAdapter;
    private ListView tradeOffer;
    private ArrayAdapter<String> tradeOfferAdapter;
    private ArrayList<String> ownerGame;
    private ArrayList<String> borrowerGame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter_trade);

        trade = (Trade) ObjParseSingleton.getInstance().popObject("trade");
        tradingEditController = new TradingEditController(trade, getApplicationContext(), this);

        tradeFor = (ListView)findViewById(R.id.tradeFor);
        tradeOffer = (ListView)findViewById(R.id.tradeOffer);

        ownerGame = new ArrayList<String>();
        ownerGame.clear();
        for (Game each: trade.getOwnerOffers()) {
            ownerGame.add(each.getTitle());
        }

        borrowerGame = new ArrayList<String>();
        borrowerGame.clear();
        for (Game each: trade.getBorrowerOffers()) {
            borrowerGame.add(each.getTitle());
        }

        offerGameButton = (Button) findViewById(R.id.offerGame);
        offerGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User saveThisUser = null;
                Intent intent = new Intent(CounterTradeActivity.this, InventoryListActivity.class);
                for (User user : UserSingleton.getInstance().getUser().getFriends().GetFriends()){
                    if(user.getAndroidID().compareTo(trade.getBorrower()) == 0){
                        saveThisUser = user;
                    }
                }
                ObjParseSingleton.getInstance().addObject("User", saveThisUser);
                ObjParseSingleton.getInstance().addObject("isInTrade", new Boolean(Boolean.TRUE));
                startActivityForResult(intent, 2);
            }

        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trade, menu);
        return true;
    }
    @Override
    public void onStart() {
        super.onStart();
        tradeForAdapter = new ArrayAdapter<String>(this, R.layout.text_view,R.id.GameList,ownerGame);
        tradeFor.setAdapter(tradeForAdapter);

        tradeOfferAdapter = new ArrayAdapter<String>(this, R.layout.text_view,R.id.GameList,borrowerGame);
        tradeOffer.setAdapter(tradeOfferAdapter);
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
