package ca.ualberta.t14.gametrader;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

// Offers a set controls to change a user's information.
public class EditProfileActivity extends Activity {
    public EditText getProfileText() {
        return profileText;
    }

    private EditText profileText;

    public EditText getPhoneText() {
        return phoneText;
    }

    private EditText phoneText;

    public EditText getEmailText() {
        return emailText;
    }

    private EditText emailText;

    public EditText getAddressText() {
        return addressText;
    }

    private EditText addressText;

    public Button getSaveButton() {
        return saveButton;
    }

    private Button saveButton;

    ProfileController profileController; // we need to instantiate this with an intent

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profileText = (EditText) findViewById(R.id.profile);
        phoneText = (EditText) findViewById(R.id.phone);
        emailText = (EditText) findViewById(R.id.email);
        addressText = (EditText) findViewById(R.id.address);
        saveButton = (Button) findViewById(R.id.saveProfileButton);

        user = (User)getIntent().getSerializableExtra("User");
        profileController = new ProfileController(user);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void SaveChanges(View view) {
        profileController.SaveProfileEdits(this.findViewById(R.id.profile).toString(),
                                                this.findViewById(R.id.email).toString(),
                                                this.findViewById(R.id.address).toString(),
                                                this.findViewById(R.id.phone).toString());
    }
}
