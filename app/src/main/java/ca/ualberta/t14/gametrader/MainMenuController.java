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
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;

import java.io.IOException;

/**
 * This class handles some big portion of the MainActivity's functionality.
 * @author Ryan Satyabrata
 */
public class MainMenuController {
    User user = new User();
    SettingsMode settingsMode = new SettingsMode();
    NetworkController netCtrl;
    PictureNetworker pn = new PictureNetworker();
    TradeNetworker tn = new TradeNetworker();

    /**
     * The constructor for MainMenuController. The application context has to be passed to here.
     * @param context
     */
    public MainMenuController(Context context) {
        netCtrl = new NetworkController(context);
    }

    /**
     * Initializes all singletons upon program start so that they are accessible by all activities
     * @param context is the context of the caller necessary for certain API calls
     */
    void preLoadAllSingletons(final Context context, final Activity activity) {

        final Context c = context;
        final Activity activityFinal = activity;

        // Inside a runnable because put in a new thread, it won't freeze the UI that way.
        Runnable r = new Runnable() {
            @Override
            public void run() {

                // Try setting the user singleton to the loaded user.
                try {
                    user = (User) user.loadJson("MainUserProfile", c);
                    user.getInventory().addObserver(user);
                    UserSingleton.getInstance().setUser(user);
                } catch (IOException e) {
                    e.printStackTrace();

                    Intent intent = new Intent(activityFinal, EditProfileActivity.class);
                    activityFinal.startActivity(intent);
                }

                UserSingleton.getInstance().getUser().setAndroidID(Settings.Secure.getString(c.getContentResolver(),
                        Settings.Secure.ANDROID_ID));
                System.out.println(Settings.Secure.getString(c.getContentResolver(),
                        Settings.Secure.ANDROID_ID));
                System.out.println("Does this print?");

                FriendsController fc = new FriendsController(UserSingleton.getInstance().getUser().getFriends(), c);
                fc.LoadFriends(c);

                // Try to load this device's Picture Manager and Networker.
                try {
                    pn = (PictureNetworker) pn.loadJson(PictureNetworker.PictureNetworkId, c);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                pn.setContext(context);
                pn.setActivity(activity);
                PictureNetworkerSingleton.getInstance().setPicNetMangager(pn);
                PictureNetworkerSingleton.getInstance().getPicNetMangager().saveJson(PictureNetworker.PictureNetworkId, c);

                // Try to load this device's Trade Networker.
                try {
                    tn = (TradeNetworker) tn.loadJson(TradeNetworker.TradeNetworkId, c);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                tn.setContext(c);
                TradeNetworkerSingleton.getInstance().setTradeNetMangager(tn);
                TradeNetworkerSingleton.getInstance().getTradeNetMangager().saveTradeNetworker();

                // Try to load the user's settings.
                try {
                    settingsMode = (SettingsMode) settingsMode.loadJson(SettingsMode.SETTINGS_FILE, c);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                SettingsSingleton.getInstance().setSettings(settingsMode);
                SettingsSingleton.getInstance().getSettings().saveJson(SettingsMode.SETTINGS_FILE, c);

                // Check if the unique installation identity exists, and add one if there is none.
                if (UserSingleton.getInstance().getUser().getInstallationId() == null) {
                    // Retrieve/Create unique installation identity.
                    InstallationIdGenerator iIdG = new InstallationIdGenerator();
                    String installationIdStr = iIdG.id(c);

                    // Set the unique installation identity if user has none.
                    UserSingleton.getInstance().getUser().setInstallationId(installationIdStr);
                }
            }
        };

        Thread t = new Thread(r);
        t.start();
    }

    /**
     * This method checks/gets updates for some network component, for trade and pictures,
     * as well upload the current userProfile to the users online.
     */
    public synchronized void updateChecker() {
        final NetworkController netc = netCtrl;
        // Update device user
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    netc.addUser(UserSingleton.getInstance().getUser());
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread t = new Thread(r);
        t.start();

        // Trades updates
        TradeNetworker tn = TradeNetworkerSingleton.getInstance().getTradeNetMangager();
        if(!tn.getTradeToUpload().isEmpty()) {
            tn.notifyAllListeners(TradeNetworker.PUSH_TRADES);
        }
        if (!tn.getTradeToRemove().isEmpty()) {
            tn.notifyAllListeners(TradeNetworker.PUSH_TRADES_TO_DELETE);
        }
        tn.getAllTradesOnNet(Boolean.TRUE);
        // Pictures update
        PictureNetworker pn = PictureNetworkerSingleton.getInstance().getPicNetMangager();
        if(!pn.getImageFilesToUpload().isEmpty()) {
            pn.notifyAllListeners(PictureNetworker.PUSH_IMAGE);
        }
        if(!pn.getImageFilesToRemove().isEmpty()) {
            pn.notifyAllListeners(PictureNetworker.PUSH_IMAGES_TO_DELETE);
        }
        if(!pn.getImagesToDownload().isEmpty()) {
            pn.notifyAllListeners(PictureNetworker.PULL_IMAGES);
        }
    }

}
