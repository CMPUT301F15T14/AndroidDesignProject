package ca.ualberta.t14.gametrader;

import android.test.ActivityInstrumentationTestCase2;
import java.util.ArrayList;

/**
 * Created by sboulet on 11/19/15.
 */
public class TradeTest extends ActivityInstrumentationTestCase2 {
    public TradeTest() {
        super(ca.ualberta.t14.gametrader.MainActivity.class);
    }

    public void testTradeStatus() {

        User owner = new User();
        User borrower = new User();

        Game game = new Game();
        Trade trade = new Trade(game, owner, borrower);
        String status = trade.getStatus().toString();
        assertEquals(status, "OWNERAPPROVAL");

        trade.setStatus(Trade.TradeStatus.BORROWERAPPROVAL);
        status = trade.getStatus().toString();
        assertEquals(status, "BORROWERAPPROVAL");
    }

    public void testOffers() {

        User owner = new User();
        User borrower = new User();

        Game game = new Game();
        Trade trade = new Trade(game, owner, borrower);
        ArrayList<Game> borrowerGames = new ArrayList<Game>();
        ArrayList<Game> ownerGames = new ArrayList<Game>();
        borrowerGames = trade.getBorrowerOffers();
        assert(borrowerGames.size() == 0);
        ownerGames = trade.getOwnerOffers();
        assert(ownerGames.size() == 1);

        Game game2 = new Game();
        trade.addBorrowerGame(game2);
        borrowerGames = trade.getBorrowerOffers();
        assert(borrowerGames.size() == 1);

        trade.removeBorrowerGame(game2);
        borrowerGames = trade.getBorrowerOffers();
        assert(borrowerGames.size() == 0);

        trade.addOwnerGame(game2);
        ownerGames = trade.getOwnerOffers();
        assert(ownerGames.size() == 2);

        trade.removeOwnerGame(game2);
        ownerGames = trade.getOwnerOffers();
        assert(ownerGames.size() == 1);
    }

    public void testComment() {
        User owner = new User();
        User borrower = new User();
        Game game = new Game();
        Trade trade = new Trade(game, owner, borrower);

        trade.setOwnersComment("This is a very fine trade.");
        String comment = trade.getOwnersComment();
        assertEquals("This is a very fine trade.", comment);
    }
}
