package ca.ualberta.t14.gametrader;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by Suzanne on 11/29/2015.
 */
public class TradeHistoryActivityTest extends ActivityInstrumentationTestCase2 {
    public TradeHistoryActivityTest() {
        super(ca.ualberta.t14.gametrader.MainActivity.class);
    }

    public void testStart() throws Exception{
        Activity tradeHistory = getActivity();
    }
}
