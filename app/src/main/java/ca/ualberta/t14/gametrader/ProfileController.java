package ca.ualberta.t14.gametrader;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by jjohnston on 10/30/15.
 */
public class ProfileController {
    public static String MainUser = "MainUserProfile";
    private static Context context;

    public ProfileController(User model, Context context) throws IOException {
        this.context = context;
        this.model = model;
        try {
            this.model = (User) this.model.loadJson(MainUser, this.context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private User model;

    void SaveProfileEdits(String userName, String email, String address, String phoneNumber){

        model.setUserName(userName);
        model.setEmail(email);
        model.setAddress(address);
        model.setPhoneNumber(phoneNumber);

        model.saveJson(MainUser, this.context);
    }
}
