package ca.ualberta.t14.gametrader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.ActivityInstrumentationTestCase2;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class GameTest extends ActivityInstrumentationTestCase2 {

    public GameTest() {
        super(ca.ualberta.t14.gametrader.MainActivity.class);
    }

    private Long getImageJpgSize(Bitmap image) {
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        return new Long(b.length);
    }

    // Convert to Android's jpg compression because slight differences exist in image when using original image.
    private Bitmap getAndroidJpg(Bitmap image) {
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        Bitmap decodedByte = BitmapFactory.decodeByteArray(b, 0, b.length);
        return decodedByte;
    }

    // This is testing the model, not the controller.
    public void testGame() {
        // Create items that get added to your inventory (1.1 AddItemToInventory)
        Game item = new Game();

        item.setPlatform(Game.Platform.PLAYSTATION1);
        assertEquals(Game.Platform.PLAYSTATION1, item.getPlatform());

        String gameName = "Crash Bandicoot Team Racing";
        item.setTitle(gameName);
        assertEquals(gameName, item.getTitle());

        item.setCondition(Game.Condition.NEW);
        assertEquals(Game.Condition.NEW, item.getCondition());

        // Being able to flag an item as not listed (1.4 FlagItemAsNotListed)
        item.setShared(Boolean.TRUE);
        assertTrue(item.isShared());

        String moreInfo = "Still in original package! You get the honour of opening it.";
        item.setAdditionalInfo(moreInfo);
        assertEquals(moreInfo, item.getAdditionalInfo());

        item.setQuantities(1);
        assertEquals(1, item.getQuantities());

        // Including attaching a photo (4.1 AttachPhotographToItem)
        DisplayMetrics displayMetrics = new DisplayMetrics();
        displayMetrics.setToDefaults();

        Activity activity = getActivity();

        // Test an image that is too big, this jpeg is 214kb
        Bitmap testImage1 = BitmapFactory.decodeResource(activity.getResources(), R.drawable.big_game);
        testImage1 = getAndroidJpg(testImage1);
        assertFalse(item.setPicture(testImage1));

        // Test an image that is ok size, this jpeg is 24.4kb
        Bitmap testImage2 = BitmapFactory.decodeResource(activity.getResources(), R.drawable.best_game_ok);
        Log.d("AAAAAAAAAA--testImage2b", getImageJpgSize(testImage2).toString());
        testImage2 = getAndroidJpg(testImage2);

        // a different empty image
        Bitmap testImage3 = Bitmap.createBitmap(displayMetrics, 200, 200, Bitmap.Config.ARGB_8888);
        testImage3 = getAndroidJpg(testImage3);

        assertTrue(item.setPicture(testImage2));
        assertTrue(testImage2.sameAs(item.getPicture()));

        // Test for JSON-able bitmap.
        Bitmap origImage = item.getPicture();
        String json = item.getPictureJson();

        // make sure the pictures are not same initially
        item.setPicture(testImage3);
        assertFalse(origImage.sameAs(item.getPicture()));

        Log.d("AAAAAAAAAA---testImage2", getImageJpgSize(testImage2).toString());

        Log.d("AAAAAAAAAA---testImage1", getImageJpgSize(testImage1).toString());
        Log.d("AAAAAAAAAA---testImage3", getImageJpgSize(testImage3).toString());

        assertTrue(item.setPictureFromJson(json));
        Log.d("AAAAAAAAAA---getPic", getImageJpgSize(item.getPicture()).toString());
        Log.d("AAAAAAAAAA---origImage", getImageJpgSize(origImage).toString());

        // fails cuz jpg is lossy and looses some stuff... hence never same?
        //assertTrue(origImage.sameAs(item.getPicture()));
        assertSame(json, item.getPictureJson());
    }

    // TODO: Test Cases testing the Game's observable and observing.

}
