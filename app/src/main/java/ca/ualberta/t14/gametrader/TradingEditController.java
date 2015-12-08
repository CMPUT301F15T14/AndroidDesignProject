package ca.ualberta.t14.gametrader;

import android.app.Activity;
import android.content.Context;

import java.lang.reflect.AccessibleObject;
import java.util.ArrayList;

/**
 * This controller provides an interface between TradeEditActivity and the Trade model.
 * It mostly calls on methods in Trade model but has some unique functions as well,
 * such as finding if a passed User is the owner or deleting the trade.
 *
 * @author Suzanne Boulet
 */
public class TradingEditController {

    private Context context;
    private Activity activity;

    private Trade model;


    /**
     * The constructor for TradingEditController
     * The passed Trade becomes the model that the controller and view will operate on
     * The passed Context stores a context that will later be used by other methods in the controller
     * and the passed Activity is stored for convenience.
     *
     * @param trade
     * @param context
     * @param activity
     */
    public TradingEditController(Trade trade, Context context, Activity activity) {
        model = trade;
        this.context = context;
        this.activity = activity;
    }

    /**
     * By taking the passed User and comparing it with the device's id,
     * this method returns whether the User is owner of the trade or not.
     *
     * @param user
     * @return true/false, whether user is owner
     */
    public Boolean isOwner(User user) {
        //TODO: 1. Get the device id
        //TODO: 2. Find user associated with that device id
        return true; //deviceiduser == user
    }

    /**
     * Takes a passed boolean and uses it to check what to do via if statement:
     * if true, the trade is considered accepted and gets its status changed to COMPLETE
     * else the trade is deleted by calling the deleteTrade method in this controller
     *
     * @param yesno
     */
    public void acceptOrDeny(Boolean yesno) {
        if (yesno) {
            model.setStatus(Trade.TradeStatus.COMPLETE);
        }
        else {
            deleteTrade(model);
        }
    }

    /**
     * A passed Game is used to call the addBorrowerGame method in the model
     * and the Game is added to the ArrayList borrowerOffers in the model
     *
     * @param game
     */
    public void borrowerAddGame(Game game) {
        model.addBorrowerGame(game);
    }

    /**
     * A passed Game is used to call the addOwnerGame method in the model
     * and the Game is added to the ArrayList borrowerOffers in the model
     *
     * @param game
     */
    public void ownerAddGame(Game game) {
        model.addOwnerGame(game);
    }

    /**
     * A passed String is used to call the setOwnersComment method in the mdoel
     * and the String replaces ownersComment in the model
     *
     * @param comment
     */
    public void changeOwnersComment(String comment) {
        model.setOwnersComment(comment);
    }

    /**
     * A passed trade, usually the controller's model, is used to call the addTradeToRemoveList method
     * in the TradeNetworker class, which was gotten by the getTradeNetManager method
     * of the TradeNetworkerSingleton class instance
     *
     * @param trade
     */
    public void deleteTrade(Trade trade) {
        TradeNetworkerSingleton.getInstance().getTradeNetMangager().addTradeToRemoveList(trade, context);

    }
}
