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
        User mike=new User();
        Game game1=new Game();
        itemlist.add(game1);
        assertTrue(itemlist.contains(game1));
        itemlist.remove(game1);
        assertFalse(itemlist.contains(game1));
        itemlist.setOwner(mike);
        assertEquals(mike,itemlist.getOwner());
    }
    public void ViewEachItem(){
        //Item item1 = new Item();
        //ItemList itemList = new ItemList();
        //itemList.addItem(item1);
    }
}
