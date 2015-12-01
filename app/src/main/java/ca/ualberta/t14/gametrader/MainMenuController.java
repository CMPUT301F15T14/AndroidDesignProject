package ca.ualberta.t14.gametrader;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;

import java.io.IOException;

/**
 * Created by satyabra on 11/27/15.
 */
public class MainMenuController {
    User user = new User();
    SettingsMode settingsMode = new SettingsMode();
    NetworkController netCtrl = new NetworkController();
    PictureNetworker pn = new PictureNetworker();
    TradeNetworker tn = new TradeNetworker();

    /**
     * Initializes all singletons upon program start so that they are accessible by all activities
     * @param context is the context of the caller necessary for certain API calls
     */
    void preLoadAllSingletons(Context context) {
        // Try setting the user singleton to the loaded user.
        try {
            user = (User) user.loadJson("MainUserProfile", context);
            UserSingleton.getInstance().setUser(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        UserSingleton.getInstance().getUser().setAndroidID(Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID));
        System.out.println(Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID));
        System.out.println("Does this print?");

        FriendsController fc = new FriendsController(UserSingleton.getInstance().getUser().getFriends());
        fc.LoadFriends(context);

        // Try to load the user's settings.
        try {
            settingsMode = (SettingsMode) settingsMode.loadJson(SettingsMode.SETTINGS_FILE, context);
            SettingsSingleton.getInstance().setSettings(settingsMode);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Check if the unique installation identity exists, and add one if there is none.
        if (UserSingleton.getInstance().getUser().getInstallationId() == null) {
            // Retrieve/Create unique installation identity.
            InstallationIdGenerator iIdG = new InstallationIdGenerator();
            String installationIdStr = iIdG.id(context);

            // Set the unique installation identity if user has none.
            UserSingleton.getInstance().getUser().setInstallationId(installationIdStr);
        }

        // Try to load this device's Picture Manager and Networker.
        try {
            pn = (PictureNetworker) pn.loadJson(PictureNetworker.PictureNetworkId, context);
            PictureNetworkerSingleton.getInstance().setPicNetMangager(pn);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Try to load this device's Trade Networker.
        try {
            tn = (TradeNetworker) tn.loadJson(TradeNetworker.TradeNetworkId, context);
            TradeNetworkerSingleton.getInstance().setTradeNetMangager(tn);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Context c = context;

        // Inside a runnable because put in a new thread, it won't freeze the UI that way.
        Runnable r = new Runnable() {
            @Override
            public void run() {

                // Try setting the user singleton to the loaded user.
                try {
                    user = (User) user.loadJson("MainUserProfile", c);
                    UserSingleton.getInstance().setUser(user);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                UserSingleton.getInstance().getUser().addObserver(netCtrl);
                FriendsController fc = new FriendsController(UserSingleton.getInstance().getUser().getFriends());
                fc.LoadFriends(c);

                // Try to load the user's settings.
                try {
                    settingsMode = (SettingsMode) settingsMode.loadJson(SettingsMode.SETTINGS_FILE, c);
                    SettingsSingleton.getInstance().setSettings(settingsMode);
                } catch (IOException e) {
                    e.printStackTrace();
                }

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

}
