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
        Inventory itemlist = new Inventory();
        InventoryController control=new InventoryController();
        Game game1=new Game();
        control.addItem(game1);
        assertTrue(itemlist.gameCollections.contains(game1));
    }
    public void ViewEachItem(){
        //Item item1 = new Item();
        //ItemList itemList = new ItemList();
        //itemList.addItem(item1);
    }
}
