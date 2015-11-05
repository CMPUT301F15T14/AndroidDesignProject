package ca.ualberta.t14.gametrader;

/**
 * Created by AndyHu on 2015-11-02.
 */
public class SettingsController {

    public void EnableDownloadPhotos(){
        SettingsMode downloadPic = new SettingsMode();
        downloadPic.setEnableDownloadPhoto1(Boolean.TRUE);
    }

    public void DisableDownloadPhotos(){
        SettingsMode downloadPic = new SettingsMode();
        downloadPic.setEnableDownloadPhoto1(Boolean.FALSE);
    }
}
