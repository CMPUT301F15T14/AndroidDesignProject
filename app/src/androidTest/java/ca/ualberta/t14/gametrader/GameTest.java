package ca.ualberta.t14.gametrader;

import android.test.ActivityInstrumentationTestCase2;

public class GameTest extends ActivityInstrumentationTestCase2 {

    public GameTest() {
        super(ca.ualberta.t14.gametrader.MainActivity.class);
    }

    public void testGame() {
        Game item = new Game();
        item.setPlatform(Game.Platform.PLAYSTATION1);
        item.setTitle("Crash Bandicoot Team Racing");
        item.setCondition(Game.Condition.NEW);
        item.setSharable(Boolean.TRUE);
        item.setAdditionalInfo("Still in original package!");
        item.setQuantities(1);

        Image pic = new Image();
        item.addPhoto(pic);
    }

}
