package ca.ualberta.t14.gametrader;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by jjohnsto on 11/1/15.
 */
public class UserTest extends ActivityInstrumentationTestCase2 {

    public UserTest() {
        super(MainActivity.class);
    }

    public void testEditProfile() {
        EditProfileActivity activity = (EditProfileActivity) getActivity();


    }
}
