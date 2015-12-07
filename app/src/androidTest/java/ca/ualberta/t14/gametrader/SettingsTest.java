package ca.ualberta.t14.gametrader;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by AndyHu on 2015-11-02.
 */
public class SettingsTest extends ActivityInstrumentationTestCase2 {
    public SettingsTest() {
        super(ca.ualberta.t14.gametrader.MainActivity.class);
    }
    public void testEnableDownloadPhotos(){
        SettingsMode downloadPic = new SettingsMode();
        downloadPic.setEnableDownloadPhoto1(Boolean.TRUE);
    }

    public void testDisableDownloadPhotos(){
        SettingsMode downloadPic = new SettingsMode();
        downloadPic.setEnableDownloadPhoto1(Boolean.FALSE);
    }

}
