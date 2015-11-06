package ca.ualberta.t14.gametrader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class InventoryItemActivity extends Activity {

    Game game;
    TextView gameTitle;
    TextView platform;
    TextView condition;
    TextView owner;
    TextView additionalInfo;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_item);

        game = (Game) ObjParseSingleton.getInstance().popObject("game");

        gameTitle = (TextView) findViewById(R.id.gameInfoTitle);
        platform = (TextView) findViewById(R.id.gameInfoConsole);
        condition = (TextView) findViewById(R.id.gameInfoCondition);
        owner = (TextView) findViewById(R.id.gameInfoOwner);
        additionalInfo = (TextView) findViewById(R.id.additionalInfoText);
        imageView = (ImageView) findViewById(R.id.inventoryItemImage);


        gameTitle.setText(game.getTitle());
        platform.setText(game.getPlatform().toString());
        condition.setText(game.getCondition().toString());
        owner.setText(UserSingleton.getInstance().getUser().getUserName());
        additionalInfo.setText(game.getAdditionalInfo());
        imageView.setImageBitmap(game.getPicture());

        Button editGame = (Button)findViewById(R.id.buttonEditItem);
        editGame.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                ObjParseSingleton.getInstance().addObject("game", game);

                Intent myIntent = new Intent(InventoryItemActivity.this, EditInventoryItemActivity.class);

                startActivity(myIntent);

            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();

        gameTitle.setText(game.getTitle());
        platform.setText(game.getPlatform().toString());
        condition.setText(game.getCondition().toString());
        owner.setText(UserSingleton.getInstance().getUser().getUserName());
        additionalInfo.setText(game.getAdditionalInfo());
        imageView.setImageBitmap(game.getPicture());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inventory_item, menu);
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
