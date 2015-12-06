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

/**
 * This singleton provides global access to one user object that can be manipulated.
 * It can be used everywhere in the app to provide global access to the current user.
 * @author  Ryan Satyabrata
 */
public class TradeNetworkerSingleton {
    //Lazy init taken from https://en.wikipedia.org/wiki/Singleton_pattern#Lazy_initialization
    private static transient TradeNetworkerSingleton instance;
    private TradeNetworker tn;
    private TradeNetworkerSingleton() { tn = new TradeNetworker(); }

    /**
     * Gets the instance of this singleton.
     * @return a reference to the instance of this class.
     */
    public static TradeNetworkerSingleton getInstance() {
        if(instance == null) {
            synchronized (UserSingleton.class) {
                if(instance == null) {
                    instance = new TradeNetworkerSingleton();
                }
            }
        }
        return instance;
    }

    /**
     * Allows to set the tradeNetworker to a different tradeNetworker in this singleton.
     * @param tradeNetMang the new tradeNetworker to replace the old tradeNetworker with.
     */
    public void setTradeNetMangager(TradeNetworker tradeNetMang) { tn = tradeNetMang; }

    /**
     * Gets the user object inside the singleton.
     * @return returns the reference of the singleton's User object.
     */
    public TradeNetworker getTradeNetMangager() { return tn; }

}
