package ca.ualberta.t14.gametrader;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

/**
 * Created by jjohnsto on 11/1/15.
 *
 * Tests functionality related to the profile.
 */
public class UserTest extends ActivityInstrumentationTestCase2 {

    public UserTest() {
        super(MainActivity.class);
    }

    public void testEditProfile() {
        setActivityInitialTouchMode(true);

        EditProfileActivity activity = (EditProfileActivity) getActivity();

        EditText emailView = (EditText) activity.findViewById(R.id.email);
        emailView.setText("edit@changed.biz");


    }
}
