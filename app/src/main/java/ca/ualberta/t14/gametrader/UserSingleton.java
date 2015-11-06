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
 * Created by kobitoko on 06/11/15.
 */
public class UserSingleton {
    //Lazy init taken from https://en.wikipedia.org/wiki/Singleton_pattern#Lazy_initialization
    private static volatile UserSingleton instance;
    private User u;
    private UserSingleton() { u = new User(); }


    public static UserSingleton getInstance() {
        if(instance == null) {
            synchronized (UserSingleton.class) {
                if(instance == null) {
                    instance = new UserSingleton();
                }
            }
        }
        return instance;
    }

    public void setUser(User user) { u = user; }

    public User getUser() { return u; }

}
