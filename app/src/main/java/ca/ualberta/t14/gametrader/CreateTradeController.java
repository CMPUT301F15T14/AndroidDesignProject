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

import android.app.Activity;
import android.content.Context;

/**
 * The controller for trades, it changes the given trade objects.
 * @author Ryan Satyabrata
 */
public class CreateTradeController {
    Context context;
    Activity activity;

    /**
     * The constructor for this class
     * @param context
     * @param activity
     */
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
