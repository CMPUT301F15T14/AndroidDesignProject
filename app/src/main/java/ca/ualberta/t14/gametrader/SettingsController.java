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
 * this is the controller connect the Setting and SettingsMode
 * it uses to change the value for enableDownloadPhoto1 via function
 * EnableDownloadPhotos and DisableDownloadPhotos.
 */
public class SettingsController {
    /**
     * enable the photo download from online or storage, turn on the switch button
     */
    public void EnableDownloadPhotos(){
        SettingsMode downloadPic = new SettingsMode();
        downloadPic.setEnableDownloadPhoto1(Boolean.TRUE);
    }

    /**
     * disable the photo download from online or storage, turn off the switch button
     */
    public void DisableDownloadPhotos(){
        SettingsMode downloadPic = new SettingsMode();
        downloadPic.setEnableDownloadPhoto1(Boolean.FALSE);
    }
}
