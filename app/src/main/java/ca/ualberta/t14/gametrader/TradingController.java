package ca.ualberta.t14.gametrader;

import java.util.ArrayList;

/**
 * Created by sboulet on 11/19/15.
 */
public class TradingController {

    private Trade model;

    public TradingController(Trade trade) {
        model = trade;
    }

    public Boolean isOwner(User user) {
        //1. Get the device id
        //2. Find user associated with that device id
        return true; //deviceiduser == user
    }

    public void offerTrade(ArrayList<Game> offering, ArrayList<Game> want) {

    }

    public void acceptOrDeny(Boolean yesno) {
        if (yesno) {

        }
        else {
            deleteTrade(model);
        }
    }

    public void editTrade(Trade trade) {

    }

    public void deleteTrade(Trade trade) {

    }
}
