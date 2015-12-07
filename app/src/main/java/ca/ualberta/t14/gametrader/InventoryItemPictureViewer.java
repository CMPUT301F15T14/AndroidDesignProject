/*
 * Copyright (C) 2015  Aaron Arnason, Tianyu Hu, Michael Xi, Ryan Satyabrata, Joel Johnston, Suzanne Boulet, Ng Yuen Tung(Brigitte)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package ca.ualberta.t14.gametrader;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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
