package ca.ualberta.t14.gametrader;

import java.util.ArrayList;

/**
 * Created by jjohnston on 10/30/15.
 */
public class ProfileController {

    public ProfileController(User model) {
        this.model = model;
    }

    private User model;

    void SaveProfileEdits(String userName, String email, String address, String phoneNumber){
        UserSingleton.getInstance().getUser().setUserName(userName);
        UserSingleton.getInstance().getUser().setEmail(email);
        UserSingleton.getInstance().getUser().setAddress(address);
        UserSingleton.getInstance().getUser().setPhoneNumber(phoneNumber);
    }
}
