/*
 * Copyright (C) 2015  Aaron Arnason, Tianyu Hu, Michael Xi, Ryan Satyabrata, Joel Johnston, Suzanne Boulet, Ng Yuen Tung(Brigitte)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package ca.ualberta.t14.gametrader;

import android.content.Context;

import java.util.ArrayList;

/**
 * TradeNetworker class handles the Trades that are to be posted online and to be fetched.
 * It like an offline storage for all the trades online, and keeps track of which trades
 * to be removed or added from this user's device.
 * @author Ryan Satyabrata
 */
public class TradeNetworker extends FileIO implements AppObservable, NetworkerCommander {
    public static final String TradeNetworkId = "TradeManagerAndNetworker";

    // Local copy of database.
    private ArrayList<Trade> allTradesOnNet;

    private ArrayList<Trade> tradeIdToUpload;
    private ArrayList<Trade> tradeIdToRemove;
    private Manager manager;
    private transient Context context;
    private transient ArrayList<NetworkerListener> listeners;
    private transient ArrayList<AppObserver> observers;
    public static final int PULL_TRADES = 123;
    public static final int PUSH_TRADES = 124;
    public static final int PUSH_TRADES_TO_DELETE = 126;

    /**
     * Constructor for TradeNetworker
     */
    public TradeNetworker() {
        tradeIdToUpload = new ArrayList<Trade>();
        tradeIdToRemove = new ArrayList<Trade>();
        allTradesOnNet = new ArrayList<Trade>();
        observers = new ArrayList<AppObserver>();
        manager = new Manager();
    }

    /**
     * To set the application context of the TradeNetworker
     * @param context
     */
    public  void setContext(Context context) {
        this.context = context;
    }

    /**
     * retrieves the list of all trades that are waiting to be uploaded.
     * @return
     */
    public ArrayList<Trade> getTradeToUpload() {
        return tradeIdToUpload;
    }

    /**
     * Retrieves all the trades from online only if the parameter updateLocalList is True, and the
     * device has internet access. Otherwise it just acts a getter for the local items that
     * it stored from the previous fetch of the elastic search server.
     * @param updateLocalList
     * @return
     */
    public ArrayList<Trade> getAllTradesOnNet(Boolean updateLocalList) {
        if(allTradesOnNet == null) {
            allTradesOnNet = new ArrayList<Trade>();
        }
        if(updateLocalList
                && ObjParseSingleton.getInstance().keywordExists(NetworkConnectivity.IS_NETWORK_ONLINE)
                && ((Boolean)ObjParseSingleton.getInstance().getObject(NetworkConnectivity.IS_NETWORK_ONLINE))) {
            notifyAllListeners(PULL_TRADES);
            saveJson(TradeNetworkId, context);
        }
        return allTradesOnNet;
    }

    /**
     * Sets the local representation of all trades on elastic search server to a given array list.
     * @param allTradesGot
     */
    public void setAllTradesOnNet(ArrayList<Trade> allTradesGot) {
        this.allTradesOnNet = allTradesGot;
    }

    /**
     * Retrieves the local representation of all trades on the elastic search server.
     * @return
     */
    public ArrayList<Trade> getAllTradesOnNetLocalArray() {
        if(allTradesOnNet == null) {
            allTradesOnNet = new ArrayList<Trade>();
        }return allTradesOnNet;
    }

    /**
     * Adds a given trade to the to-upload list that will eventually be uploaded to the elastic search server.
     * @param trade
     * @param context
     */
    public void addTradeToUploadList(Trade trade, Context context) {

        replaceSame(trade, tradeIdToUpload);

        // now add the trade
        this.tradeIdToUpload.add(trade);
        saveJson(TradeNetworkId, context);
        if(ObjParseSingleton.getInstance().keywordExists(NetworkConnectivity.IS_NETWORK_ONLINE)
                && ((Boolean)ObjParseSingleton.getInstance().getObject(NetworkConnectivity.IS_NETWORK_ONLINE)))
            notifyAllListeners(PUSH_TRADES);
    }

    /**
     * Retrieve all the trades that are waiting to be removed from the server.
     * @return
     */
    public ArrayList<Trade> getTradeToRemove() {
        return tradeIdToRemove;
    }

    /**
     * Adds a given trade to the to-remove list that eventually causes the trade to be removed from
     * the elastic search server.
     * @param trade
     * @param context
     */
    public void addTradeToRemoveList(Trade trade, Context context) {
        replaceSame(trade, tradeIdToRemove);
        this.tradeIdToRemove.add(trade);
        saveJson(TradeNetworkId, context);
        if(ObjParseSingleton.getInstance().keywordExists(NetworkConnectivity.IS_NETWORK_ONLINE)
                && ((Boolean)ObjParseSingleton.getInstance().getObject(NetworkConnectivity.IS_NETWORK_ONLINE)))
            notifyAllListeners(PUSH_TRADES_TO_DELETE);

    }

    // check and remove trade with same tradeID because this would be the update version
    private void replaceSame(Trade trd, ArrayList<Trade> trds) {
        Trade replace = null;
        for(Trade each : trds) {
            if(each.getTradeId().compareTo(trd.getTradeId()) == 0) {
                replace = each;
                break;
            }
        }
        if(replace != null) {
            trds.remove(replace);
        }
    }

    /**
     * Saves the TradeNetworker as a json into a presistent file on the user's android device.
     */
    public void saveTradeNetworker() {
        saveJson(TradeNetworkId, context);
    }

    /**
     * Enables to subscribe to the Networker class.
     * @param listener
     */
    public void addListener(NetworkerListener listener) {
        if(listeners == null) {
            listeners = new ArrayList<NetworkerListener>();
        }
        listeners.add(listener);
    }

    /**
     * Enables removing listeners from the Networker class.
     * @param listener
     */
    public void deletelisteners(NetworkerListener listener) {
        if(listeners != null) {
            listeners.remove(listener);
        }
    }

    /**
     * A method to generate a unique tradeID for a trade using the creator's Android_ID.
     * It does not assign this generated tradeID to the trade.
     * @param tradeCreator
     * @return the string of the tradeID
     */
    public String getTradeId(User tradeCreator) {
        // stamp the trade with an id.
        return manager.addItemToTrack(tradeCreator, "trd");
    }

    public void addObserver(AppObserver observer) {
        if(observers == null) {
            observers = new ArrayList<AppObserver>();
        }
        this.observers.add(observer);
    }
    public void deleteObserver(AppObserver observer) {
        if(observer != null) {
            observers.remove(observer);
        }
    }

    /**
     * Called to notify all observers that the model has been updated.
     */
    public void notifyAllObservers() {
        if(observers != null) {
            for(AppObserver each : observers) {
                each.appNotify(this);
            }
        }
    }

    /**
     * The method that calls all listeners/subscribers and give them a command code.
     * @param commandCode
     */
    public void notifyAllListeners(int commandCode) {
        if(listeners != null) {
            for (NetworkerListener obs : listeners) {
                obs.netListenerNotify(commandCode);
            }
        }
    }

}
