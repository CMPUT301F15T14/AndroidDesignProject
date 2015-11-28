package ca.ualberta.t14.gametrader;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class InventoryItemPictureViewer extends Activity {

    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_picture_viewer);
        game = (Game) ObjParseSingleton.getInstance().popObject("game");
        if( game == null) {
            game = new Game();
        }
        getActionBar().setTitle(game.getTitle() + " Photos");

        LinearLayout photoContainer = (LinearLayout) findViewById(R.id.viewPhotoScroller);

        ImageView iv = new ImageView(getApplicationContext());
        if(!game.getPictureId().isEmpty()) {
            game.setPictureFromJson(UserSingleton.getInstance().getUser().getPictureManager().loadImageJsonFromJsonFile(game.getPictureId(), getApplicationContext()));
            iv.setImageBitmap(game.getPicture());
            iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            iv.setPadding(10, 10, 10, 10);
        }
        // -1 add with largest index in other words, last.
        photoContainer.addView(iv, -1);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item_picture_viewer, menu);
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
