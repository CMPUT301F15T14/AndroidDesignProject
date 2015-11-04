package ca.ualberta.t14.gametrader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class GameTest extends ActivityInstrumentationTestCase2 {

    public GameTest() {
        super(ca.ualberta.t14.gametrader.MainActivity.class);
    }

    private Bitmap getBitmapFromJson(String json) {
        byte[] decodedString = Base64.decode(json, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    private Long getImageJpgSize(Bitmap image) {
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        // the following returns byte size of the uncompressed bitmap!
        // Note: As of KITKAT API 19+ the result of getByteCount method can no longer be used to determine memory usage of a bitmap. See getAllocationByteCount().
        //return new Long(image.getByteCount());
        return new Long(b.length);
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

        // Test an image that is too big, this jpeg is 219.8kb
        Bitmap testImage1 = BitmapFactory.decodeResource(activity.getResources(), R.drawable.big_game);
        assertFalse(item.setPicture(testImage1));

        // Test an image that is ok size, this jpeg is 12.7kb
        Bitmap testImage2 = BitmapFactory.decodeResource(activity.getResources(), R.drawable.best_game_ok);
        Log.d("sizeBytes--testImage2b", getImageJpgSize(testImage2).toString());

        // a different empty image
        Bitmap testImage3 = Bitmap.createBitmap(displayMetrics, 200, 200, Bitmap.Config.ARGB_8888);

        assertTrue(item.setPicture(testImage2));
        assertTrue(testImage2.sameAs(item.getPicture()));

        // Test for JSON-able bitmap.
        String json = item.getPictureJson();
        Bitmap origImage = getBitmapFromJson(json);

        // make sure the pictures are not same initially
        item.setPicture(testImage3);
        assertFalse(origImage.sameAs(item.getPicture()));

        Log.d("sizeBytes---testImage2", getImageJpgSize(testImage2).toString());

        Log.d("sizeBytes---testImage1", getImageJpgSize(testImage1).toString());
        Log.d("sizeBytes---testImage3", getImageJpgSize(testImage3).toString());

        assertTrue(item.setPictureFromJson(json));
        Log.d("sizeBytes---getPic", getImageJpgSize(item.getPicture()).toString());
        Log.d("sizeBytes---origImage", getImageJpgSize(origImage).toString());



        //Todo: resize feature instead of rejecting image

        assertTrue(origImage.sameAs(item.getPicture()));
        assertSame(json, item.getPictureJson());
    }

    // TODO: Test Cases testing the Game's observable and observing.

}
