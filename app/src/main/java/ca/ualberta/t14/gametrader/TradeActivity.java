package ca.ualberta.t14.gametrader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class TradeActivity extends Activity {

    private TradingController tradingController;

    private ListView tradeFor;

    private ArrayList<String> ownerGame;
    private ArrayList<String> borrowerGame;
    private Button editTrade;
    private Button deleteTrade;

    private Trade trade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade2);

        trade = (Trade) ObjParseSingleton.getInstance().popObject("trade");
        tradingController = new TradingController(trade);

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

        editTrade = (Button)findViewById(R.id.editTrade);


        deleteTrade = (Button)findViewById(R.id.deleteTrade);
        deleteTrade.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                tradingController.deleteTrade(trade);
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
