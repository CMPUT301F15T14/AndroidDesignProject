package ca.ualberta.t14.gametrader;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by michaelximac on 2015-11-03.
 */
public class InventoryActivityTest extends ActivityInstrumentationTestCase2{
    public InventoryActivityTest(){
        super(ca.ualberta.t14.gametrader.MainActivity.class);
    }
    public void testStart() throws Exception{
        Activity InventoryActivity=getActivity();
    }

    
}
