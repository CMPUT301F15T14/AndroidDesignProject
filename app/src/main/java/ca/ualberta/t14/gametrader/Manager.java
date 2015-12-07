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
 * This class is the basic functionality for the managers, manager classes should use this class
 * to get help with assigning and keeping track of unique id
 * and have a list array of object or string.....
 * @author Ryan Satyabrata
 */
public class Manager {

    Integer managerAdderIdCount;

    /**
     * The manager's constructor. It initialized the internal filecount to 0.
     */
    public Manager() {
        managerAdderIdCount = 0;
    }

    /**
     * This method will create an ID for the file. With given prefix it will become that string's prefix,
     * and given user it will get the user's AndroidID and use that as well. At the very end of the string
     * it will add the counter. Then increases this object's internal counter.
     * The resulting string is something like: <prefix>_<InstId>_<itemId>  without the "<" ">" symbols.
     * This resulting string will then be returned.
     * @param userForId
     * @param prefixName
     * @return the resulting string in format of <prefix>_<InstId>_<itemId>
     */
    String addItemToTrack(User userForId, String prefixName) {
        // Name Format: <prefix>_<InstId>_<itemId>
        String id = prefixName + "_" + userForId.getAndroidID() + "_" + managerAdderIdCount.toString();
        managerAdderIdCount += 1;
        return id;
    }

}
