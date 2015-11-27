package ca.ualberta.t14.gametrader;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;

import java.io.IOException;

/**
 * Created by satyabra on 11/27/15.
 */
public class MainMenuController {
    User user;
    SettingsMode settingsMode = new SettingsMode();
    NetworkController netCtrl = new NetworkController();

    void preLoadAllSingletons(Context context) {
        User user = new User();

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
        UserSingleton.getInstance().getUser().addObserver(netCtrl);

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
    }

}