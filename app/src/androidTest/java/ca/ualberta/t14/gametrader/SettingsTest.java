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
    /*
    public void testEnableDownloadPhotos(){
        photolist pictures = new photolist();
        Bitmap photo = new Bitmap("from gallery");
        pictures.addPhoto(photo);

        Game item = new Game("");
        item.attachPhotos(photos);
        
        photos.disableDownload();
        assertTrue(photo.isEnable());//ena -> true
        }
    */



    /*
    public static boolean testEnableDownloadPhoto(){
        Game game = new Game();
        if (game.getPicture() == null ){
            return true;
        }
        else{
            return false;
        }
    }
    */
}
