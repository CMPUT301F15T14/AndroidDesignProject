package ca.ualberta.t14.gametrader;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

/**
 * Created by sboulet on 11/17/15.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2 {

    private Button profileButton;
    private Button inventoryButton;
    private Button friendsButton;
    private Button searchButton;
    private Button tradesButton;
    private Button settingsButton;

    public MainActivityTest() {
        super(ca.ualberta.t14.gametrader.MainActivity.class);
    }

    public void testStart() throws Exception {
        Activity main = getActivity();
    }

    //test whether the profile button runs properly (brings you to ProfileActivity)
    public void testProfileButton() {
        MainActivity main = (MainActivity)getActivity();

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
        getInstrumentation().waitForIdleSync();

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
        getInstrumentation().removeMonitor(receiverActivityMonitor);

        receiverActivity.finish();
    }

    //test whether the inventory button runs properly (brings you to InventoryListActivity)
    public void testInventoryButton() {
        MainActivity main = (MainActivity)getActivity();

        // Set up an ActivityMonitor
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                getInstrumentation().addMonitor(InventoryListActivity.class.getName(),
                        null, false);

        inventoryButton = main.getInventoryButton();
        main.runOnUiThread(new Runnable() {
            public void run() {
                inventoryButton.performClick();
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

    //test whether the friends button runs properly (brings you to ProfileActivity)
    public void testFriendsButton() {
        MainActivity main = (MainActivity)getActivity();

        // Set up an ActivityMonitor
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                getInstrumentation().addMonitor(FriendsListActivity.class.getName(),
                        null, false);

        friendsButton = main.getFriendsButton();
        main.runOnUiThread(new Runnable() {
            public void run() {
                friendsButton.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();

        //Following was stolen from https://developer.android.com/training/activity-testing/activity-functional-testing.html

        // Validate that ReceiverActivity is started
        final FriendsListActivity receiverActivity = (FriendsListActivity)
                receiverActivityMonitor.waitForActivityWithTimeout(1000);
        assertNotNull("ReceiverActivity is null", receiverActivity);
        assertEquals("Monitor for ReceiverActivity has not been called",
                1, receiverActivityMonitor.getHits());
        assertEquals("Activity is of wrong type",
                FriendsListActivity.class, receiverActivity.getClass());

        // Remove the ActivityMonitor
        getInstrumentation().removeMonitor(receiverActivityMonitor);

        receiverActivity.finish();
    }

    //test whether the trades button runs properly (brings you to ...Activity)
/*    public void testTradesButton() {
        MainActivity main = (MainActivity)getActivity();


        getInstrumentation().waitForIdleSync();

        // Set up an ActivityMonitor
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                getInstrumentation().addMonitor(ProfileActivity.class.getName(),
                        null, false);

        inventoryButton = main.getInventoryButton();
        main.runOnUiThread(new Runnable() {
            public void run() {
                inventoryButton.performClick();
            }
        });

        //Following was stolen from https://developer.android.com/training/activity-testing/activity-functional-testing.html

        // Validate that ReceiverActivity is started
        final InventoryActivity receiverActivity = (InventoryActivity)
                receiverActivityMonitor.waitForActivityWithTimeout(1000);
        assertNotNull("ReceiverActivity is null", receiverActivity);
        assertEquals("Monitor for ReceiverActivity has not been called",
                1, receiverActivityMonitor.getHits());
        assertEquals("Activity is of wrong type",
                InventoryActivity.class, receiverActivity.getClass());

        // Remove the ActivityMonitor
        getInstrumentation().removeMonitor(receiverActivityMonitor);
    } */

    //test whether the search button runs properly (brings you to SearchPageActivity)
    public void testSearchButton() {
        MainActivity main = (MainActivity)getActivity();

        // Set up an ActivityMonitor
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                getInstrumentation().addMonitor(SearchPageActivity.class.getName(),
                        null, false);

        searchButton = main.getSearchButton();
        main.runOnUiThread(new Runnable() {
            public void run() {
                searchButton.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();

        //Following was stolen from https://developer.android.com/training/activity-testing/activity-functional-testing.html

        // Validate that ReceiverActivity is started
        final SearchPageActivity receiverActivity = (SearchPageActivity)
                receiverActivityMonitor.waitForActivityWithTimeout(1000);
        assertNotNull("ReceiverActivity is null", receiverActivity);
        assertEquals("Monitor for ReceiverActivity has not been called",
                1, receiverActivityMonitor.getHits());
        assertEquals("Activity is of wrong type",
                SearchPageActivity.class, receiverActivity.getClass());

        // Remove the ActivityMonitor
        getInstrumentation().removeMonitor(receiverActivityMonitor);

        receiverActivity.finish();
    }

    //test whether the settings button runs properly (brings you to SettingActivity)
    public void testSettingsButton() {
        MainActivity main = (MainActivity)getActivity();

        // Set up an ActivityMonitor
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                getInstrumentation().addMonitor(SettingActivity.class.getName(),
                        null, false);

        settingsButton = main.getSettingsButton();
        main.runOnUiThread(new Runnable() {
            public void run() {
                settingsButton.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();

        //Following was stolen from https://developer.android.com/training/activity-testing/activity-functional-testing.html

        // Validate that ReceiverActivity is started
        final SettingActivity receiverActivity = (SettingActivity)
                receiverActivityMonitor.waitForActivityWithTimeout(1000);
        assertNotNull("ReceiverActivity is null", receiverActivity);
        assertEquals("Monitor for ReceiverActivity has not been called",
                1, receiverActivityMonitor.getHits());
        assertEquals("Activity is of wrong type",
                SettingActivity.class, receiverActivity.getClass());

        // Remove the ActivityMonitor
        getInstrumentation().removeMonitor(receiverActivityMonitor);

        receiverActivity.finish();
    }
}
