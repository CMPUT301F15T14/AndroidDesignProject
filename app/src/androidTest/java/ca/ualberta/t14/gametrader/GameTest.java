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
import android.graphics.BitmapFactory;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class GameTest extends ActivityInstrumentationTestCase2 {

    public GameTest() {
        super(ca.ualberta.t14.gametrader.MainActivity.class);
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


    }

    private Bitmap getBitmapFromJson(String json) {
        byte[] decodedString = Base64.decode(json, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    private Long getImageJpgSize(Bitmap image) {
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 85, byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        return new Long(b.length);
    }

    public void testGameImages() {
        // Including attaching a photo (4.1 AttachPhotographToItem)
        Game item = new Game();

        Activity activity = getActivity();

        int resizedMaxVal = 200;
        Long maxFileSize = new Long(65536);

        // Test an image that is too big and gets resized with width being longest edge
        Bitmap testImage1 = BitmapFactory.decodeResource(activity.getResources(), R.drawable.big_game);
        assertTrue(item.setPicture(testImage1));
        assertEquals(resizedMaxVal, item.getPicture().getWidth());
        // filesize check of testImage1
        assertTrue(maxFileSize > getImageJpgSize(item.getPicture()));

        // Test an image that has no resizing issue.
        Bitmap testImage2 = BitmapFactory.decodeResource(activity.getResources(), R.drawable.best_game_ok);
        assertTrue(item.setPicture(testImage2));
        testImage2 = item.getPicture();
        // filesize check of testImage2
        assertTrue(maxFileSize > getImageJpgSize(item.getPicture()));

        // Test image that is too big and gets resized with height being longest edge
        Bitmap testImage3 = BitmapFactory.decodeResource(activity.getResources(), R.drawable.game_goty);
        assertTrue(item.setPicture(testImage3));
        assertEquals(resizedMaxVal, item.getPicture().getHeight());
        // filesize check of testImage3
        assertTrue(maxFileSize > getImageJpgSize(item.getPicture()));

        // Images being actually changed
        assertFalse(testImage2.sameAs(item.getPicture()));
        assertTrue(item.setPicture(testImage2));
        assertTrue(testImage2.sameAs(item.getPicture()));

        // Test for JSON-able bitmap.
        String json = item.getPictureJson();
        Bitmap origImage = getBitmapFromJson(json);

        // make sure the pictures are not same initially
        item.setPicture(testImage3);
        assertFalse(origImage.sameAs(item.getPicture()));

        assertTrue(item.setPictureFromJson(json));

        // json and resulting image are same.
        assertSame(json, item.getPictureJson());
        assertTrue(origImage.sameAs(item.getPicture()));
    }

    // TODO: Test Cases testing the Game's observable and observing.

}
