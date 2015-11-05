package ca.ualberta.t14.gametrader;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by jjohnsto on 11/1/15.
 *
 * Tests functionality related to the profile.
 */
public class UserTest extends ActivityInstrumentationTestCase2 {

    /*public UserTest() {
        super(EditProfileActivity.class);
    }

    public void testEditProfile() {
        EditProfileActivity activity = (EditProfileActivity) getActivity();

        final EditText profileText = activity.getProfileText();
        final EditText phoneText = activity.getPhoneText();
        final EditText emailText = activity.getEmailText();
        final EditText addressText = activity.getAddressText();
        final Button SaveButton = activity.getSaveButton();

        /*User user = new User();
        activity.setUser(user);

        activity.runOnUiThread(new Runnable() {
            public void run() {
                profileText.setText("usr");
            }
        });
        getInstrumentation().waitForIdleSync();

        activity.runOnUiThread(new Runnable() {
            public void run() {
                SaveButton.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();

        assertEquals("this is not correct", activity.getUser().getUserName());
        //assertEquals("one", "two");
    }*/
}
