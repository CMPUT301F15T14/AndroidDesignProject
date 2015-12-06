package ca.ualberta.t14.gametrader;

import android.app.Activity;
import android.content.Context;

/**
 * Created by Ryan on 2015-11-30.
 */
public class CreateTradeController {
    Context context;
    Activity activity;

    public CreateTradeController(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void setBorrowerToTrade(Trade trade, User borrower) {
        trade.setBorrower(borrower);
    }

    public void setOwnerToTrade(Trade trade, User owner) {
        trade.setOwner(owner);
    }

    public void borrowerAddGame(Trade trade, Game game) {
        trade.addBorrowerGame(game);
    }

    public void setOwnerComment(Trade trade, String comment) {
        trade.setOwnersComment(comment);
    }

}
