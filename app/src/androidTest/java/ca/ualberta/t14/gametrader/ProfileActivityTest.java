package ca.ualberta.t14.gametrader;

import android.app.Activity;
import android.app.Instrumentation;
import android.os.RecoverySystem;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

/**
 * Created by sboulet on 11/17/15.
 */
public class ProfileActivityTest extends ActivityInstrumentationTestCase2 {

    private Button editprof;
    private Button inventory;

    public ProfileActivityTest() {
        super(ca.ualberta.t14.gametrader.ProfileActivity.class);
    }

    public void testStart() throws Exception {
        Activity profile = getActivity();
    }

    public void testEditprofButton() {
        ProfileActivity profile = (ProfileActivity)getActivity();

        // Set up an ActivityMonitor
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                getInstrumentation().addMonitor(EditProfileActivity.class.getName(),
                        null, false);

        editprof = profile.getEditprofButton();
        profile.runOnUiThread(new Runnable() {
            public void run() {
                editprof.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();

        //Following was stolen from https://developer.android.com/training/activity-testing/activity-functional-testing.html

        // Validate that ReceiverActivity is started
        final EditProfileActivity receiverActivity = (EditProfileActivity)
                receiverActivityMonitor.waitForActivityWithTimeout(1000);
        assertNotNull("ReceiverActivity is null", receiverActivity);
        assertEquals("Monitor for ReceiverActivity has not been called",
                1, receiverActivityMonitor.getHits());
        assertEquals("Activity is of wrong type",
                EditProfileActivity.class, receiverActivity.getClass());

        // Remove the ActivityMonitor
        getInstrumentation().removeMonitor(receiverActivityMonitor);

        receiverActivity.finish();
    }

    public void testInventoryButton() {
        ProfileActivity profile = (ProfileActivity)getActivity();

        // Set up an ActivityMonitor
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                getInstrumentation().addMonitor(InventoryListActivity.class.getName(),
                        null, false);

        inventory = profile.getInventoryButton();
        profile.runOnUiThread(new Runnable() {
            public void run() {
                inventory.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();

        //Following was stolen from https://developer.android.com/training/activity-testing/activity-functional-testing.html

        // Validate that ReceiverActivity is started
        final InventoryListActivity receiverActivity = (InventoryListActivity)
                receiverActivityMonitor.waitForActivityWithTimeout(1000);
        assertNotNull("ReceiverActivity is null", receiverActivity);
        assertEquals("Monitor for ReceiverActivity has not been called",
                1, receiverActivityMonitor.getHits());
        assertEquals("Activity is of wrong type",
                InventoryListActivity.class, receiverActivity.getClass());

        // Remove the ActivityMonitor
        getInstrumentation().removeMonitor(receiverActivityMonitor);

        receiverActivity.finish();
    }
}
