package ca.ualberta.t14.gametrader;

import java.util.ArrayList;

/**
 * Created by jjohnston on 10/30/15.
 */
public class ProfileController {
    private User model;

    void SaveProfileEdits(String userName, String email, String address, String phoneNumber){
        model.userName = userName;
        model.address = address;
        model.phoneNumber = phoneNumber;
    }

}
