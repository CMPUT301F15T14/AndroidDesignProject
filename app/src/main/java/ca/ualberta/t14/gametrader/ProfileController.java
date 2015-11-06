/*  Copyright (C) 2015  Aaron Arnason, Tianyu Hu, Michael Xi, Ryan Satyabrata, Joel Johnston, Suzanne Boulet, Ng Yuen Tung(Brigitte)

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*/

package ca.ualberta.t14.gametrader;

import java.util.ArrayList;

/**
 * Created by jjohnston on 10/30/15.
 *
 *  Handles modifications to profile information within the User class (any publicly visible data
 *  such as contact info).
 */

public class ProfileController {

    public ProfileController(User model) {
        this.model = model;
    }

    private User model;

    /**
     * Saves any changes to the profile information.
     * @param userName is a String containing the updated user name.
     * @param email is a String containing the updated email.
     * @param address is a String containing the updated address.
     * @param phoneNumber is a String containing the updated phone number.
     */
    void SaveProfileEdits(String userName, String email, String address, String phoneNumber){
        model.setUserName(userName);
        model.setEmail(email);
        model.setAddress(address);
        model.setPhoneNumber(phoneNumber);
    }
}
