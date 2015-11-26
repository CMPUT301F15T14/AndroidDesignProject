package ca.ualberta.t14.gametrader;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

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

        listActivity.getMobileArray().clear();
        addItemActivity.finish();

    }

    public void testSearch() {
        Inventory testInv = new Inventory();
        InventoryController testInvCnt = new InventoryController(testInv, new User());

        Game g1 = new Game("Halo: Combat Evolved");
        g1.setPlatform(Game.Platform.PC);
        Game g2 = new Game("Halo 2");
        g2.setPlatform(Game.Platform.XBOX);
        Game g3 = new Game("Halo 3");
        g3.setPlatform(Game.Platform.XBOX360);
        Game g4 = new Game("Teletubies Invade Earth");
        g3.setPlatform(Game.Platform.PLAYSTATION1);

        ArrayList<Game> result = testInvCnt.Search("Halo");
        assertTrue(result.isEmpty());

        testInvCnt.addItem(g1);
        testInvCnt.addItem(g2);
        testInvCnt.addItem(g3);
        testInvCnt.addItem(g4);

        ArrayList<Game> expectedResult = new ArrayList<Game>();
        expectedResult.add(g1);
        expectedResult.add(g2);
        expectedResult.add(g3);

        result = testInvCnt.Search("Halo");

        assertTrue(result.containsAll(expectedResult));
        assertFalse(result.contains(g4));

        result = testInvCnt.Search("Halo", Game.Platform.PC);
        assertTrue(result.contains(g1));
        assertFalse(result.contains(g2));
        assertFalse(result.contains(g3));
        assertFalse(result.contains(g4));
    }
}
