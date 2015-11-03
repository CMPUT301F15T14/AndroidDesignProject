package ca.ualberta.t14.gametrader;

import android.graphics.Bitmap;

/**
 * Created by AndyHu on 2015-11-02.
 */
public class Settings {

    private Boolean enableDownloadPhoto1;

    public Boolean getEnableDownloadPhoto1() {
        return enableDownloadPhoto1;
    }

    public void setEnableDownloadPhoto1(Boolean enableDownloadPhoto1) {
        this.enableDownloadPhoto1 = enableDownloadPhoto1;
    }

    private SettingsController control = new SettingsController(this);

    {
        public static boolean DownloadPhoto() {
        if (getEnableDownloadPhoto1() == Boolean.TRUE) {
            control.EnableDownloadPhotos();
        } else {
            control.DisableDownloadPhotos();
        }
    }
    }
}
/*
    public boolean DownloadIt() {

        if (enableDownloadPhoto1 == Boolean.TRUE) {
            Game game = new Game();
            game.getPicture();
            return enableDownloadPhoto1 = Boolean.TRUE;
        } else {
            return enableDownloadPhoto1 = Boolean.FALSE;
        }
    }
}
    //private SettingsController control = new SettingsController(this);


    //public void enableDownloadPhoto(){
    //    control.enableDownloadPhotos();
    //}

    //public void disableDownloadPhoto(){
        //control.disableDownloadPhotos();
    //}
}
