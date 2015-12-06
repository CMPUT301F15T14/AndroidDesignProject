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
 * this is a mode that get and set the value for enableDownloadPhoto1
 * which judge the switch is off or on.
 */
public class SettingsMode extends FileIO {
    private Boolean enableDownloadPhoto1 = Boolean.TRUE;
    public static final String SETTINGS_FILE = "settings"; // name of settings file

    /**
     * this is getting the value of enableDownloadPhoto1 from controller changed
     * @return the value to enableDownloadPhoto1.
     */
    public Boolean getEnableDownloadPhoto1() {
        return enableDownloadPhoto1;
    }

    /**
     * this is the setting function for set the value for enableDownloadPhoto1 for the initial value
     * for enableDownloadPhoto1.
     * @param enableDownloadPhoto1
     */
    public void setEnableDownloadPhoto1(Boolean enableDownloadPhoto1) {
        this.enableDownloadPhoto1 = enableDownloadPhoto1;
    }
}