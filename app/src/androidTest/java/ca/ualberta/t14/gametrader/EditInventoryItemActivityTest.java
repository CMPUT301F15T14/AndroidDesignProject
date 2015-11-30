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
import android.graphics.Bitmap;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Created by satyabra on 11/1/15.
 */
public class EditInventoryItemActivityTest extends ActivityInstrumentationTestCase2 {

    GameController gc = new GameController();
    ArrayList<Game> gameList;
    Game freshGame;

    public EditInventoryItemActivityTest() { super(ca.ualberta.t14.gametrader.EditInventoryItemActivity.class);

        gameList = UserSingleton.getInstance().getUser().getInventory().getAllGames();
        // Insert new game
        freshGame = gc.createGame(UserSingleton.getInstance().getUser());
        ObjParseSingleton.getInstance().addObject("game", freshGame);
    }

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

        // NOTICE: each method in GameController should have parameters, like here. UML has no parameters.

        // createGame() should return a Game type! Fix that later in UML. It will also automatically add it to user's inventory.
        // it then launches the activity's edit screen for it.

        Activity activity = getActivity();

        // Set and Check the Title
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((EditText) getActivity().findViewById(R.id.inventoryItemTitle)).setText("Test 4 Gamez!");
            }
        });
        getInstrumentation().waitForIdleSync();
        String titleText = ((EditText) activity.findViewById(R.id.inventoryItemTitle)).getText().toString();
        assertEquals("Test 4 Gamez!", titleText);

        // set and check game console to Wii U
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((Spinner) getActivity().findViewById(R.id.gameConsole)).setSelection(9);
            }
        });
        getInstrumentation().waitForIdleSync();
        String gameConsoleStr = ((Spinner) activity.findViewById(R.id.gameConsole)).getSelectedItem().toString().toUpperCase().replace(" ", "");
        assertEquals(Game.Platform.WIIU.toString(), gameConsoleStr);

        // set and check game condition to Like New
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((Spinner) getActivity().findViewById(R.id.gameCondition)).setSelection(1);
            }
        });
        getInstrumentation().waitForIdleSync();
        String gameConditionStr = ((Spinner) activity.findViewById(R.id.gameCondition)).getSelectedItem().toString().toUpperCase().replace(" ", "");
        assertEquals(Game.Condition.LIKENEW.toString(), gameConditionStr);

        // set and check game sharing to private
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((RadioButton) getActivity().findViewById(R.id.sharePublic)).setChecked(Boolean.TRUE);
            }
        });
        getInstrumentation().waitForIdleSync();
        Boolean sharedIsPublic = ((RadioButton) activity.findViewById(R.id.sharePublic)).isChecked();
        Boolean sharedIsPrivate = ((RadioButton) activity.findViewById(R.id.sharePrivate)).isChecked();
        assertEquals(Boolean.TRUE, sharedIsPublic);
        assertEquals(Boolean.FALSE, sharedIsPrivate);

        // Set and Check additional info
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((EditText) getActivity().findViewById(R.id.AddInfoText)).setText("This game is magical.\nNo where!");
            }
        });
        getInstrumentation().waitForIdleSync();
        String extraInfoStr = ((EditText) activity.findViewById(R.id.AddInfoText)).getText().toString();
        assertEquals("This game is magical.\nNo where!", extraInfoStr);

        // save test game to inventory
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((Button) getActivity().findViewById(R.id.saveInventory)).performClick();
            }
        });
        getInstrumentation().waitForIdleSync();

        gameList = UserSingleton.getInstance().getUser().getInventory().getAllGames();
        assertTrue(gameList.contains(freshGame));
        Game gameSaved = gameList.get(0);

        assertEquals(titleText, gameSaved.getTitle());
        assertEquals(gameConsoleStr, gameSaved.getPlatform().toString());
        assertEquals(gameConditionStr, gameSaved.getCondition().toString());
        assertEquals(sharedIsPublic, gameSaved.isShared());
        assertEquals(extraInfoStr, gameSaved.getAdditionalInfo());

        // test with image retrieving and URI. How?
        //gc.addPhoto(imageLocation);


    }

}
