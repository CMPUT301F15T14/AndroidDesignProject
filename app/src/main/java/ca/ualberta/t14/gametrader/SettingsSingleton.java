package ca.ualberta.t14.gametrader;

/**
 * Created by jjohnsto on 11/17/15.
 */
public class SettingsSingleton {
    private static volatile SettingsSingleton instance;
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
