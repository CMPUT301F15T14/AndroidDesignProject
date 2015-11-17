package ca.ualberta.t14.gametrader;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by michaelximac on 2015-11-01.
 */
public class InventoryTest extends ActivityInstrumentationTestCase2{
    public InventoryTest() {
        super(ca.ualberta.t14.gametrader.InventoryListActivity.class);
    }

    public void testViewInventory(){
        InventoryListActivity listActivity= (InventoryListActivity) getActivity();
        final Button AddGame=listActivity.getAddGameButton();

        // Set up an ActivityMonitor
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                getInstrumentation().addMonitor(EditInventoryItemActivity.class.getName(),
                        null, false);

        listActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
               AddGame.performClick();
            }
        });

        getInstrumentation().waitForIdleSync();

        // Validate that ReceiverActivity is started
        EditInventoryItemActivity addItemActivity = (EditInventoryItemActivity)
                receiverActivityMonitor.waitForActivityWithTimeout(1000);
        assertNotNull("ReceiverActivity is null", addItemActivity);
        assertEquals("Monitor for ReceiverActivity has not been called",
                1, receiverActivityMonitor.getHits());
        assertEquals("Activity is of wrong type",
                EditInventoryItemActivity.class, addItemActivity.getClass());

        final EditText gameTitle=addItemActivity.getGameTitle();
        final Button save=addItemActivity.getSaveButton();

        addItemActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gameTitle.setText("GTA V");
                save.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();
        assertTrue(listActivity.containGameTitle("GTA V"));
        
        // Remove the ActivityMonitor
        getInstrumentation().removeMonitor(receiverActivityMonitor);

    }

}
