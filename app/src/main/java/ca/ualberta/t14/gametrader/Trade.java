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

import java.util.ArrayList;

/**
 * This class stores information on a trade between two users:
 * the borrower, who initiates the trade, and the
 * owner, who the borrower wishes to trade with.
 * The borrower can ask for 1 to many games from the owner's inventory
 * and can offer 0 to many games in return.
 *
 * The owner can also offer a countertrade back to the borrower.
 *
 * @author Suzanne Boulet
 */
public class Trade extends FileIO implements AppObservable{

    private volatile transient ArrayList<AppObserver> observers;

    @Override
    public void addObserver(AppObserver observer) {
        observers.add(observer);
    }

    @Override
    public void deleteObserver(AppObserver o) {
        observers.remove(o);
    }


    public void notifyAllObservers() {
        // Since it's stored somewhere, and observers is transient,
        // the constructor might not be called, resulting observers to be null.
        if(observers == null) {
            observers = new ArrayList<AppObserver>();
        }
        for(AppObserver obs : observers) {
            obs.appNotify(this);
        }
    }

    /**
     * An enumeration of the status of the trade.
     * OWNERAPPROVAL: when the trade is first offered to the owner,
     * or when the borrower sends a countertrade
     * BORROWERAPPOVAL: when the owner countertrades
     * COMPLETE: when a trade is accepted on either end
     */
    public enum TradeStatus {OWNERAPPROVAL,BORROWERAPPROVAL,COMPLETE}

    private String borrowerAndroidId;
    private ArrayList<Game> borrowerOffers;
    private String ownerAndroidId;
    private ArrayList<Game> ownerOffers;

    private String borrowerID;

    private String ownersComment;
    private TradeStatus status;

    private String tradeId;

    /**
     * creates a trade using a Game passed by the activity, adding it to ownerOffers.
     *
     * Initializes the trade. Creates an empty ArrayList for borrowerOffers,
     * initializes borrower based on user id and owner based on game owner's id
     * creates a blank comment for trade and set tradeStatus to OWNERAPPROVAL
     * initializes an empty list of observers
     *
     * @param game
     */
    public Trade(Game game, User owner, User borrower) {
        this.ownerAndroidId = owner.getAndroidID();
        this.borrowerAndroidId = borrower.getAndroidID();
        borrowerOffers = new ArrayList<Game>();
        ownerOffers = new ArrayList<Game>();
        ownerOffers.add(game);
        ownersComment = "";
        status = TradeStatus.OWNERAPPROVAL;
        observers = new ArrayList<AppObserver>();

        borrowerID = borrower.getAndroidID();
        //or just set the tradeId = 0; and generate it from later function
    }

    public String getTradeName() {
        String tradeName = "Trade " + ownerOffers.get(0).getTitle();
        if(!borrowerOffers.isEmpty()) {
           tradeName += " for " + borrowerOffers.get(0).getTitle();
        }
        return tradeName;
    }

    /**
     * A getter of the creator of the trade.
     *
     * @return User's AndroidId who first offered the trade (borrower)
     */
    public String getBorrower() {
        return borrowerAndroidId;
    }

    /**
     * A setter to set a passed User as the borrower (initiator) of the trade
     * The borrower will be set in initialization, so only to be used if an error occured.
     *
     * @param borrower
     */
    public void setBorrower(User borrower) {
        this.borrowerAndroidId = borrower.getAndroidID();
        notifyAllObservers();
    }

    /**
     * A getter that returns the list of games offered by the borrower in the trade
     *
     * @return ArrayList of the Games offered by the borrower (borrowerOffers)
     */
    public ArrayList<Game> getBorrowerOffers() {
        return borrowerOffers;
    }

    /**
     * A setter that adds the passed Game to the ArrayList borrowerOffers
     *
     * @param game
     */
    public void addBorrowerGame(Game game) {
        borrowerOffers.add(game);
        notifyAllObservers();
    }

    /**
     * Removes a game from the list of games the borrower is offering.
     *
     * @param game
     */
    public void removeBorrowerGame(Game game) {
        borrowerOffers.remove(game);
    }

    /**
     * A getter of the receiver of the trade offer
     *
     * @return the User's AndroidId who gets offered the trade (owner)
     */
    public String getOwner() {
        return ownerAndroidId;
    }

    /**
     * A setter that takes the passed User and sets it as owner
     * The owner will be set in initialization, so only to be used if an error occured.
     *
     * @param owner
     */
    public void setOwner(User owner) {
        this.ownerAndroidId = owner.getAndroidID();
        notifyAllObservers();
    }

    /**
     * A getter that returns the list of games offered by the owner in the trade
     *
     * @return the ArrayList of Games offered by the owner (ownerOffers)
     */
    public ArrayList<Game> getOwnerOffers() {
        return ownerOffers;
    }

    /**
     * A setter that adds the passed Game to the ArrayList ownerOffers
     *
     * @param game
     */
    public void addOwnerGame(Game game) {
        ownerOffers.add(game);
        notifyAllObservers();
    }

    /**
     * Removes a game from the list of games the owner is offering.
     *
     * @param game
     */
    public void removeOwnerGame(Game game) {
        ownerOffers.remove(game);
    }

    /**
     * A getter that returns the comment attached to the trade.
     *
     * @return String ownerComment
     */
    public String getOwnersComment() {
        return ownersComment;
    }

    /**
     * A setter that takes String comment and sets it as ownersComment
     *
     * @param comment
     */
    public void setOwnersComment(String comment) {
        ownersComment = comment;
        notifyAllObservers();
    }

    /**
     * The tag of the status of the trade
     * Used to classify who has the choice to accept the trade or not
     *
     * @return enumeration of the trade's status
     */
    public TradeStatus getStatus() {
        return status;
    }

    /**
     * The tag of the status of the trade
     * Used to change which user has the choice to accept the trade
     *
     * @param status
     */
    public void setStatus(TradeStatus status) {
        this.status = status;
        notifyAllObservers();
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }
}
