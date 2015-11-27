package ca.ualberta.t14.gametrader;

import android.content.ContentResolver;
import android.content.Context;

import java.io.IOException;

/**
 * Created by satyabra on 11/27/15.
 */
public class MainMenuController {
    User user = new User();
    SettingsMode settingsMode = new SettingsMode();

    void preLoadAllSingletons(Context context) {
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
