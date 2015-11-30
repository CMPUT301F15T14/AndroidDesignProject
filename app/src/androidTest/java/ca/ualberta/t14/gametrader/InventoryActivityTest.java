package ca.ualberta.t14.gametrader;

import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

/**
 * Created by michaelximac on 2015-11-30.
 */
public class InventoryActivityTest extends ActivityInstrumentationTestCase2 {
    public InventoryActivityTest(){
        super(ca.ualberta.t14.gametrader.MainActivity.class);
    }

    public void testViewInventory(){
        MainActivity MainMenu=(MainActivity)getActivity();
        final Button inventory=MainMenu.getInventoryButton();
        // Set up a new ActivityMonitor
        Instrumentation.ActivityMonitor listActivityMonitor =
                getInstrumentation().addMonitor(InventoryListActivity.class.getName(),
                        null, false);
        MainMenu.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                User testUser=new User();
                UserSingleton.getInstance().setUser(testUser);
                testUser.getInventory().clear();
                inventory.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();

        // Validate that ReceiverActivity is started
        InventoryListActivity listActivity = (InventoryListActivity)
                listActivityMonitor.waitForActivityWithTimeout(1000);
        assertNotNull("ReceiverActivity is null", listActivity);
        assertEquals("Monitor for ReceiverActivity has not been called",
                1, listActivityMonitor.getHits());
        assertEquals("Activity is of wrong type",
                InventoryListActivity.class, listActivity.getClass());
        //InventoryListActivity listActivity= (InventoryListActivity) getActivity();

        final ListView GameList=listActivity.getGameList();

        addGameTest(listActivity, GameList);
        // Set up a new ActivityMonitor
        Instrumentation.ActivityMonitor inventoryItemMonitor =
                getInstrumentation().addMonitor(InventoryItemActivity.class.getName(),
                        null, false);

        InventoryItemActivity inventoryItem=viewGameTest(listActivity, inventoryItemMonitor, GameList);

        deleteTest(inventoryItem,GameList);

        // Remove the ActivityMonitor
        getInstrumentation().removeMonitor(inventoryItemMonitor);

        listActivity.getMobileArray().clear();

    }

    public void addGameTest(InventoryListActivity listActivity,final ListView GameList){
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
        final Spinner selectConsole=addItemActivity.getSpinConsole();

        final int PC_POSITION=0;
        final int XBOX_ONE_POSITION=7;

        addItemActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gameTitle.setText("GTA V");
                selectConsole.requestFocus();
                selectConsole.setSelection(PC_POSITION);
                save.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();
        assertTrue(listActivity.containGameTitle("GTA V"));

        // Remove the ActivityMonitor
        getInstrumentation().removeMonitor(receiverActivityMonitor);

        int expectedCount = 1;
        int actualCount =GameList.getAdapter().getCount();
        assertEquals(expectedCount, actualCount);
    }

    public InventoryItemActivity viewGameTest(InventoryListActivity listActivity,Instrumentation.ActivityMonitor inventoryItemMonitor,final ListView GameList){
        listActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GameList.performItemClick(GameList, 0, GameList.getItemIdAtPosition(0));
            }
        });

        getInstrumentation().waitForIdleSync();

        // Validate that ReceiverActivity is started
        InventoryItemActivity inventoryItem = (InventoryItemActivity)
                inventoryItemMonitor.waitForActivityWithTimeout(1000);
        assertNotNull("ReceiverActivity is null", inventoryItem);
        assertEquals("Monitor for ReceiverActivity has not been called",
                1, inventoryItemMonitor.getHits());
        assertEquals("Activity is of wrong type",
                InventoryItemActivity.class, inventoryItem.getClass());
        assertEquals("GTA V", inventoryItem.getGameTitle());
        assertEquals("PC", inventoryItem.getPlatform());

        return inventoryItem;
    }

    public void deleteTest(InventoryItemActivity inventoryItem,ListView GameList){
        // Set up a new ActivityMonitor
        Instrumentation.ActivityMonitor deleteItemMonitor =
                getInstrumentation().addMonitor(EditInventoryItemActivity.class.getName(),
                        null, false);

        final Button edit=inventoryItem.getEditGame();

        inventoryItem.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                edit.performClick();
            }
        });

        getInstrumentation().waitForIdleSync();
        // Validate that ReceiverActivity is started
        EditInventoryItemActivity deleteItem = (EditInventoryItemActivity)
                deleteItemMonitor.waitForActivityWithTimeout(1000);
        assertNotNull("ReceiverActivity is null", deleteItem);
        assertEquals("Monitor for ReceiverActivity has not been called",
                1, deleteItemMonitor.getHits());
        assertEquals("Activity is of wrong type",
                EditInventoryItemActivity.class, deleteItem.getClass());

        final Button delete=deleteItem.getDelete();
        deleteItem.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                delete.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();

        final Button YesButton=((AlertDialog) (deleteItem.getDeletDialogue())).getButton(
                DialogInterface.BUTTON_POSITIVE);
        deleteItem.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                YesButton.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();
        int expectedCount = 0;
        int actualCount =GameList.getAdapter().getCount();
        assertEquals(expectedCount, actualCount);
        // Remove the ActivityMonitor
        getInstrumentation().removeMonitor(deleteItemMonitor);
    }
}
