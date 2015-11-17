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

import java.util.HashMap;

/**
 * This optimistic singleton provides global access to a object holder that can accept and hold objects,
 * and give them back at a later time given a keyword.
 * Intended to be used to pass along objects from one Activity to another.
 * @author  Ryan Satyabrata
 */
public class ObjParseSingleton {
    //Lazy init taken from https://en.wikipedia.org/wiki/Singleton_pattern#Lazy_initialization
    private static volatile ObjParseSingleton instance;
    private volatile HashMap<String, Object> objectStorage;
    private ObjParseSingleton() { objectStorage = new HashMap<String, Object>(); }

    /**
     * Gets the instance of this singleton.
     * @return a reference to the instance of this class.
     */
    public static ObjParseSingleton getInstance() {
        if(instance == null) {
            synchronized (ObjParseSingleton.class) {
                if(instance == null) {
                    instance = new ObjParseSingleton();
                }
            }
        }
        return instance;
    }

    /**
     * Adds an object with keyword to this singleton data holder.
     * @param idName the keyword to be used to retrieve the object later
     * @param objToParse the object to hold.
     */
    public void addObject(String idName, Object objToParse) {
        objectStorage.put(idName, objToParse);
    }

    /**
     * Retrieves an object from the singleton data holder with given keyword.
     * If no such keyword exist, returned object is null!
     * If exist object will be returned and the keyword & object pair removed from this singleton data holder.
     * @param idName the keyword to find the object.
     * @return the object associated o the keyword.
     */
    public Object popObject(String idName) {
        Object obj = objectStorage.get(idName);
        objectStorage.remove(idName);
        return obj;
    }

    /**
     * Checks whether the singleton data holder contains such a keyword.
     * @param idName the keyword to check for existence.
     * @return whether it exists or not.
     */
    public Boolean keywordExists(String idName) {
        return objectStorage.containsKey(idName);
    }

    /**
     * Checks whether the singleton data holder contains such an object
     * @param object the object to check for existence.
     * @return whether it exists or not.
     */
    public Boolean objectExist(Object object) {
        return objectStorage.containsKey(object);
    }

    /**
     * Retrieves an object from the singleton data holder with given keyword.
     * If no such keyword exist, returned object is null!
     * If exist object will be returned and the keyword & object pair will NOT be removed from this singleton data holder.
     * So the keyword & object pair continue to exist in the singleton data holder.
     * @param idName the keyword to find the object.
     * @return the object associated o the keyword.
     */
    public Object getObject(String idName) {
        return objectStorage.get(idName);
    }

}
