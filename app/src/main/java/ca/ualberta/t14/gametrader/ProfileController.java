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
        model.setUserName(userName);
        model.setEmail(email);
        model.setAddress(address);
        model.setPhoneNumber(phoneNumber);
    }
}
