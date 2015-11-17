/*  Copyright (C) 2015  Aaron Arnason, Tianyu Hu, Michael Xi, Ryan Satyabrata, Joel Johnston, Suzanne Boulet, Ng Yuen Tung(Brigitte)

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*/

package ca.ualberta.t14.gametrader;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

/**
 * Created by jjohnsto on 11/1/15.
 *
 * Tests functionality related to the profile.
 */
public class UserTest extends ActivityInstrumentationTestCase2 {

    public UserTest() {
        super(EditProfileActivity.class);
    }

    public void testEditProfile() {
        EditProfileActivity activity = (EditProfileActivity) getActivity();

        final EditText profileText = activity.getProfileText();
        final EditText phoneText = activity.getPhoneText();
        final EditText emailText = activity.getEmailText();
        final EditText addressText = activity.getAddressText();
        final Button SaveButton = activity.getSaveButton();

        activity.runOnUiThread(new Runnable() {
            public void run() {
                profileText.setText("usr");
                phoneText.setText("587-877-2072");
                emailText.setText("snacks@food.co");
                addressText.setText("53 Spooky Street");
            }
        });
        getInstrumentation().waitForIdleSync();

        activity.runOnUiThread(new Runnable() {
            public void run() {
                SaveButton.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();

        assertEquals("usr", activity.getUser().getUserName());
        assertEquals("587-877-2072", activity.getUser().getPhoneNumber());
        assertEquals("snacks@food.co", activity.getUser().getEmail());
        assertEquals("53 Spooky Street", activity.getUser().getAddress());
    }
}
