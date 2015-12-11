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
 * Created by jjohnsto on 11/17/15.
 */
public class SettingsSingleton {
    private static transient SettingsSingleton instance;
    private SettingsMode model;
    private SettingsSingleton() { model = new SettingsMode(); }

    /**
     * Gets the instance of this singleton.
     * @return a reference to the instance of this class.
     */
    public static SettingsSingleton getInstance() {
        if(instance == null) {
            synchronized (SettingsSingleton.class) {
                if(instance == null) {
                    instance = new SettingsSingleton();
                }
            }
        }
        return instance;
    }

    public SettingsMode getSettings() { return model; }

    public void setSettings(SettingsMode settings) { model = settings; }
}
