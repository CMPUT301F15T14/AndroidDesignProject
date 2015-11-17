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
import android.test.ActivityInstrumentationTestCase2;

import java.io.File;
import java.io.IOException;

/**
 * Created by satyabra on 11/17/15.
 */
public class FileIOTest extends ActivityInstrumentationTestCase2 {


    public FileIOTest() { super(ca.ualberta.t14.gametrader.MainActivity.class);
    }

    private class TestFile extends FileIO {
        private String testText = "";

        public TestFile() {
        }

        public void setTestText(String text) {
            testText = text;
        }

        public String getTestText() {
            return testText;
        }

    }

    public void testFileIo() {

        String theId = "testSaveFiles";
        String testText = "This is just a text 42?";
        Activity a = getActivity();

        // Load user from JSON. The user contains Inventory.

        TestFile fio = new TestFile();

        assertEquals("", fio.getTestText());
        assertFalse(fio.doesFileExist(theId, a.getApplicationContext()));

        fio.setTestText(testText);

        assertEquals(testText, fio.getTestText());

        fio.saveJson(theId, a.getApplicationContext());

        assertTrue(fio.doesFileExist(theId, a.getApplicationContext()));

        TestFile fio2 = null;
        try {
            fio2 = (TestFile) fio.loadJson(theId, a.getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertNotNull(fio2);

        assertEquals(testText, fio2.getTestText());

        // delete the file
        // Taken from http://stackoverflow.com/questions/14737996/android-deleting-a-file-from-internal-storage
        File dir = a.getFilesDir();
        File file = new File(dir, theId);
        file.delete();
    }

}
