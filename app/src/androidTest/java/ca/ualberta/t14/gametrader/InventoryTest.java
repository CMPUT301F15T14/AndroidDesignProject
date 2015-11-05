package ca.ualberta.t14.gametrader;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by michaelximac on 2015-11-01.
 */
public class InventoryTest extends ActivityInstrumentationTestCase2{
    public InventoryTest() {
        super(ca.ualberta.t14.gametrader.MainActivity.class);
    }

    public void testViewInventory(){
        User mike=new User();
        Inventory itemlist=new Inventory();
        InventoryController itemlistControl=new InventoryController(itemlist,mike);
        itemlistControl.clearInventory();
        Game game1=new Game();
        itemlistControl.addItem(game1);
        assertTrue(itemlistControl.contains(game1));
        itemlistControl.removeItem(game1);
        assertFalse(itemlistControl.contains(game1));
        assertEquals(mike, itemlistControl.identifyOwner());
    }
    public void ViewEachItem(){
        //Item item1 = new Item();
        //ItemList itemList = new ItemList();
        //itemList.addItem(item1);
    }
}
