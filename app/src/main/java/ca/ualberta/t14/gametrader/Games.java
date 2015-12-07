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
 * Created by michaelximac on 2015-11-25.
 */
public class Games extends ArrayList<Game> implements AppObservable{
    private volatile ArrayList<AppObserver> observers = new ArrayList<AppObserver>();
    private static final String RESOURCE_URL = "http://cmput301.softwareprocess.es:8080/cmput301f15t14/game";
    private static final String SEARCH_URL = "http://cmput301.softwareprocess.es:8080/cmput301f15t14/game/_search";

    @Override
    public void addObserver(AppObserver o) {
        observers.add(o);
    }

    @Override
    public void deleteObserver(AppObserver o) {
        observers.remove(o);
    }

    @Override
    public void notifyAllObservers() {
        for (AppObserver o : observers) {
            o.appNotify(this);
        }
    }

    public String getResourceUrl() {
        return RESOURCE_URL;
    }

    public String getSearchUrl() {
        return SEARCH_URL;
    }

    /**
     * Java wants this, we don't need it for Gson/Json
     */
    private static final long serialVersionUID = 3199561696102797345L;
}
