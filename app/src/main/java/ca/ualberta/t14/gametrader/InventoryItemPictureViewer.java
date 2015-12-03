package ca.ualberta.t14.gametrader;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class InventoryItemPictureViewer extends Activity implements AppObserver {

    PictureViewerController pictureViewerController;
    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_picture_viewer);

        game = new Game();
        game = (Game) ObjParseSingleton.getInstance().popObject("game");

        game.addObserver(this);
        PictureNetworkerSingleton.getInstance().getPicNetMangager().addObserver(this);

        getActionBar().setTitle(game.getTitle() + " Photos");

        (findViewById(R.id.horizontalScrollView)).setVerticalFadingEdgeEnabled(Boolean.TRUE);

        pictureViewerController = new PictureViewerController(getApplicationContext(), this);


    }

    @Override
    public void onStart() {
        super.onStart();

        pictureViewerController.putImages(game, Boolean.TRUE);
        pictureViewerController.setButtonClickers();

    }

    public void appNotify(AppObservable observable) {
        pictureViewerController.clearAllImages();
        pictureViewerController.putImages(game, Boolean.FALSE);
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
