package ca.ualberta.t14.gametrader;

import android.app.Activity;
import android.content.Context;

import java.lang.reflect.AccessibleObject;
import java.util.ArrayList;

/**
 * Created by sboulet on 11/19/15.
 */
public class TradingController {

    private Context context;
    private Activity activity;

    private Trade model;

    public TradingController(Trade trade, Context context, Activity activity) {
        model = trade;
        this.context = context;
        this.activity = activity;
    }

    public Boolean isOwner(User user) {
        //TODO: 1. Get the device id
        //TODO: 2. Find user associated with that device id
        return true; //deviceiduser == user
    }

    public void acceptOrDeny(Boolean yesno) {
        if (yesno) {
            model.setStatus(Trade.TradeStatus.COMPLETE);
        }
        else {
            deleteTrade(model);
        }
    }

    public void borrowerAddGame(Game game) {
        model.addBorrowerGame(game);
    }

    public void ownerAddGame(Game game) {
        model.addOwnerGame(game);
    }

    public void changeOwnersComment(String comment) {
        model.setOwnersComment(comment);
    }

    public void deleteTrade(Trade trade) {
        TradeNetworkerSingleton.getInstance().getTradeNetMangager().addTradeToRemoveList(trade, context);

    }
}
