package ca.ualberta.t14.gametrader;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by michaelximac on 2015-11-25.
 */
public class SearchTest extends ActivityInstrumentationTestCase2 {
    public SearchTest(){
        super(ca.ualberta.t14.gametrader.SearchPageActivity.class);
    }

    public void testSearch(){
        SearchController sc = new SearchController();
        Games gs = sc.searchGames("*", null);

        assertNotNull(gs);
        assertTrue(gs.size() != 0);

        //Game g=sc.getGame(0);
        //assertNotNull(g);

    }
}
