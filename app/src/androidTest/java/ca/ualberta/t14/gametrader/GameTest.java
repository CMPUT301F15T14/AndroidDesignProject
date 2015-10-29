package ca.ualberta.t14.gametrader;

import android.graphics.Bitmap;
import android.test.ActivityInstrumentationTestCase2;
import android.util.DisplayMetrics;

public class GameTest extends ActivityInstrumentationTestCase2 {

    public GameTest() {
        super(ca.ualberta.t14.gametrader.MainActivity.class);
    }

    public void testGame() {
        // Create items that get added to your inventory (1.1 AddItemToInventory)
        // This is testing the model, not the controller.
        Game item = new Game();
        item.setPlatform(Game.Platform.PLAYSTATION1);
        item.setTitle("Crash Bandicoot Team Racing");
        item.setCondition(Game.Condition.NEW);
        // Being able to flag an item as not listed (1.4 FlagItemAsNotListed)
        item.setSharable(Boolean.TRUE);
        item.setAdditionalInfo("Still in original package! You get the honour of opening it.");
        item.setQuantities(1);

        // Including attaching a photo (4.1 AttachPhotographToItem)
        DisplayMetrics displayMetrics = new DisplayMetrics();
        displayMetrics.setToDefaults();

        Bitmap testImage = Bitmap.createBitmap(displayMetrics,120,120, Bitmap.Config.ARGB_8888);
        item.setPicture(testImage);
    }

}
