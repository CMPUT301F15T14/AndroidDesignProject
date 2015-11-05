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

import android.graphics.Bitmap;
import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by satyabra on 11/1/15.
 */
public class GameControllerTest extends ActivityInstrumentationTestCase2 {

    public GameControllerTest() { super(ca.ualberta.t14.gametrader.MainActivity.class); }

    private void setupGame1(Game gameObj) {
        gameObj.setPlatform(Game.Platform.PLAYSTATION1);

        String gameName = "Crash Bandicoot Team Racing";
        gameObj.setTitle(gameName);

        gameObj.setCondition(Game.Condition.NEW);

        gameObj.setShared(Boolean.TRUE);

        String moreInfo = "Still in original package! You get the honour of opening it.";
        gameObj.setAdditionalInfo(moreInfo);

        gameObj.setQuantities(1);
    }

    public void testController() {
    /*
    UML stuff that connects to Model. +
     Create items that get added to your inventory (1.1), including attaching a photo (4.1) and being able to flag an item as not listed (1.4)
     RE: 4.1 for controller: http://stackoverflow.com/questions/15662258/how-to-save-a-bitmap-on-internal-storage
      the controller will actually get the location of the image. For possible test, create empty image like before, then save it on disk, get path and test put path.
      for testing use method File getAbsoluteFile ()
      after test, clean up your stuff using delete that file:  (File delete() method)
      http://developer.android.com/reference/java/io/File.html#delete%28%29
    */
        GameController gc = new GameController();

        // NOTICE: each method in GameController should have parameters, like here. UML has no parameters.

        // createGame() should return a Game type! Fix that later in UML. It will also automatically add it to user's inventory.
        // it then launches the activity's edit screen for it.
        Game game1 = gc.createGame();
        setupGame1(game1);

        // It will check for the game object in the user's inventory if it contains the object game then is owner, else not owner.
        assertTrue(gc.isOwner(game1));

        // should launch the activity's edit screen. Like ur looking at the normal game item's detail and with this BAM editable.
        gc.editGame(game1);

        // test with uri.... halp plz b0ss.
        //gc.addPhoto(imageLocation);

        Bitmap downloadedImage = gc.manualDownloadPhoto(game1);

        // removeGame will remove the entry from the inventory.
        gc.removeGame(game1);

    }

}
