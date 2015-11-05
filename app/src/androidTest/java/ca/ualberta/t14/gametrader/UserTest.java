package ca.ualberta.t14.gametrader;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;

/**
 * Created by jjohnsto on 11/1/15.
 *
 * Tests functionality related to the profile.
 */
public class UserTest extends ActivityInstrumentationTestCase2 {

    public UserTest() {
        super(MainActivity.class);
    }

    public void testEditProfile() {
        //EditProfileActivity activity = (EditProfileActivity) getActivity();

        // Set up an ActivityMonitor
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                getInstrumentation().addMonitor(EditProfileActivity.class.getName(),
                        null, false);
        getInstrumentation().waitForIdleSync();

        EditProfileActivity receiverActivity = (EditProfileActivity)
                receiverActivityMonitor.waitForActivityWithTimeout(1000);
        assertNotNull("ReceiverActivity is null", receiverActivity);
        assertEquals("Monitor for ReceiverActivity has not been called",
                1, receiverActivityMonitor.getHits());
        assertEquals("Activity is of wrong type",
                EditProfileActivity.class, receiverActivity.getClass());

        // Remove the ActivityMonitor
        getInstrumentation().removeMonitor(receiverActivityMonitor);

        final EditText profileText = receiverActivity.getProfileText();
        final EditText phoneText = receiverActivity.getPhoneText();
        final EditText emailText = receiverActivity.getEmailText();
        final EditText addressText = receiverActivity.getAddressText();

        receiverActivity.runOnUiThread(new Runnable() {
            public void run() {
                profileText.setText("Poopsie");
                phoneText.setText("463-7373");
                emailText.setText("browncow@hownow.com");
                addressText.setText("5573 Pizza Street");
            }
        });

    }
}
