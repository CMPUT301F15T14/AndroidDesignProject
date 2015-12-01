package ca.ualberta.t14.gametrader;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Created by michaelximac on 2015-11-01.
 */
public class InventoryTest extends ActivityInstrumentationTestCase2{
    public InventoryTest() {
        super(ca.ualberta.t14.gametrader.InventoryListActivity.class);
    }
    
    public void testSearch() {
        Inventory testInv = new Inventory();
        InventoryController testInvCnt = new InventoryController(testInv);

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
