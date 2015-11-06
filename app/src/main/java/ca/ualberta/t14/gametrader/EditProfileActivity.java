/*
 * Copyright (C) 2015  Aaron Arnason, Tianyu Hu, Michael Xi, Ryan Satyabrata, Joel Johnston, Suzanne Boulet, Ng Yuen Tung(Brigitte)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package ca.ualberta.t14.gametrader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

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

    private static String userProfile = "MainUserProfile";

    private Button saveButton;
    private Button cancelEditProfileButton;

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

        user = UserSingleton.getInstance().getUser();
        profileController = new ProfileController(user, this.getApplicationContext());

        phoneText = (EditText) findViewById(R.id.phone);

        profileText = (EditText) findViewById(R.id.profile);
        emailText = (EditText) findViewById(R.id.email);
        addressText = (EditText) findViewById(R.id.address);

/*
        profileText.setText(user.getUserName());
        phoneText.setText(user.getPhoneNumber());
        emailText.setText(user.getEmail());
        addressText.setText(user.getAddress());
*/
        saveButton = (Button) findViewById(R.id.saveProfileButton);
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                profileController.SaveProfileEdits(profileText.getText().toString(),
                        emailText.getText().toString(),
                        addressText.getText().toString(),
                        phoneText.getText().toString());
                finish();

                User mainUser = UserSingleton.getInstance().getUser();
                try {
                    mainUser = (User) mainUser.loadJson(userProfile, getApplicationContext());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mainUser.setPhoneNumber(phoneText.getText().toString());
                mainUser.setAddress(addressText.getText().toString());
                mainUser.setEmail(emailText.getText().toString());
                mainUser.setUserName(profileText.getText().toString());

                mainUser.saveJson(userProfile, getApplicationContext());

                Toast.makeText(EditProfileActivity.this, "Saved!", Toast.LENGTH_SHORT).show();

        }
        });

        cancelEditProfileButton = (Button) findViewById(R.id.cancelButton);
        cancelEditProfileButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
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

}
