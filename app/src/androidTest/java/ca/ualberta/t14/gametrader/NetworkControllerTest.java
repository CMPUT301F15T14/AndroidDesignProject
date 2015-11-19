package ca.ualberta.t14.gametrader;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by jjohnsto on 11/19/15.
 */
public class NetworkControllerTest extends ActivityInstrumentationTestCase2 {
    public NetworkControllerTest(){
        super(MainActivity.class);
    }

    public void testInsertGame() {
        Game game = new Game();
        game.setTitle("Testgamepleaseignore");
        game.setAdditionalInfo("Some additional information would go here.");
        game.setCondition(Game.Condition.ACCEPTABLE);
        game.setPlatform(Game.Platform.OTHER);
        game.setShared(Boolean.FALSE);
        game.setQuantities(1);

        NetworkController net = new NetworkController();
        net.InsertGame(game);

        net.SearchGames("Testgame");
    }
}
