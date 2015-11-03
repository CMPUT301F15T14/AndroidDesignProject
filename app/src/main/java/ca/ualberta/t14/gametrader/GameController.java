package ca.ualberta.t14.gametrader;

import android.graphics.Bitmap;

/**
 * Created by satyabra on 11/1/15.
 */
public class GameController {

    private Game model;

    public GameController() {
        model = new Game();
        //model.loadJson();//???
    }

    public Game createGame() {
        model = new Game();

        return model;
    }

    public Boolean isOwner(Game game) {
        // TODO: 1. get device-id from user here (the owner of this device has a profile) and 2. search for inventory that belongs to this user. 3. Then check this inventory if it contains this game
        return Boolean.FALSE;
    }

    public void editGame(Game game) {
        // TODO: launch the activity's edit game screen??

    }

    public void addPhoto(Bitmap img) {
        model.setPicture(img);
    }

    public Bitmap manualDownloadPhoto(Game game) {
        // TODO: from cache or elastisearch, find the image associated to this game and force download it (disregard the settings)
        return null;
    }

    public void removeGame(Game game) {
        // TODO: removeGame will remove the entry from the inventory.
    }

}
