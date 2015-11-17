package ca.ualberta.t14.gametrader;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

/**
 * Created by michaelximac on 2015-11-01.
 */
public class InventoryTest extends ActivityInstrumentationTestCase2{
    public InventoryTest() {
        super(ca.ualberta.t14.gametrader.MainActivity.class);
    }

    public void testViewInventory(){
        InventoryListActivity listActivity= (InventoryListActivity) getActivity();
        Button AddGame=listActivity.getAddGameButton();

        InventoryItemActivity itemActivity= (InventoryItemActivity) getActivity();


    }

}
