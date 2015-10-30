package ca.ualberta.t14.gametrader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by satyabra on 10/29/15.
 */
public class Game {

    public enum Platform { PLAYSTATION1, PLAYSTATION2, PLAYSTATION3, PLAYSTATION4,
        XBOX, XBOX360, XBOXONE, WII, WIIU, DREAMCAST }
    public enum Condition { NEW, LIKENEW, MINT, ACCEPTABLE }

    private Platform platform;
    private Condition condition;
    private String title;
    private Boolean sharableStatus;
    private String additionalInfo;
    private Bitmap picture;

    private int quantities;

    public Game() {}

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean isSharable() {
        return sharableStatus;
    }

    public void setSharable(Boolean sharableStatus) {
        this.sharableStatus = sharableStatus;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public int getQuantities() {
        return quantities;
    }

    public void setQuantities(int quantities) {
        this.quantities = quantities;
    }

    public void setPicture(Bitmap image) {
        picture = image;
    }

    public Bitmap getPicture() {
        return picture;
    }

}
