package ca.ualberta.t14.gametrader;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

/**
 * Created by sboulet on 11/17/15.
 */
public class ProfileActivityTest extends ActivityInstrumentationTestCase2 {

    private Button editprof;
    private Button inventory;

    public ProfileActivityTest() {
        super(ca.ualberta.t14.gametrader.MainActivity.class);
    }

    public void testStart() throws Exception {
        Activity profile = getActivity();
    }

    public void testEditprofButton() {
 /*       ProfileActivity profile = (ProfileActivity)getActivity();


        getInstrumentation().waitForIdleSync();

        // Set up an ActivityMonitor
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                getInstrumentation().addMonitor(ProfileActivity.class.getName(),
                        null, false);

        profileButton = main.getProfileButton();
        main.runOnUiThread(new Runnable() {
            public void run() {
                profileButton.performClick();
            }
        });

        //Following was stolen from https://developer.android.com/training/activity-testing/activity-functional-testing.html

        // Validate that ReceiverActivity is started
        final ProfileActivity receiverActivity = (ProfileActivity)
                receiverActivityMonitor.waitForActivityWithTimeout(1000);
        assertNotNull("ReceiverActivity is null", receiverActivity);
        assertEquals("Monitor for ReceiverActivity has not been called",
                1, receiverActivityMonitor.getHits());
        assertEquals("Activity is of wrong type",
                ProfileActivity.class, receiverActivity.getClass());

        // Remove the ActivityMonitor
        getInstrumentation().removeMonitor(receiverActivityMonitor); */
    }

    public void testInventoryButton() {

    }
}
