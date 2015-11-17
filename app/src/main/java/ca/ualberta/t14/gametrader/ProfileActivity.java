package ca.ualberta.t14.gametrader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;


public class ProfileActivity extends Activity {
    public static String userProfile = "MainUserProfile";

    ProfileController profileController;

    TextView userNameView;
    TextView phoneView;
    TextView emailView;
    TextView addressView;

    private Button editprof;

    public Button getEditprofButton() {
        return editprof;
    }

    private Button inventory;

    public Button getInventoryButton() {
        return inventory;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        addressView = (TextView) findViewById(R.id.addressDisplay);
        emailView = (TextView) findViewById(R.id.emailDisplay);
        phoneView = (TextView) findViewById(R.id.phoneDisplay);
        userNameView = (TextView) findViewById(R.id.userNameDisplay);

        User mainUser = UserSingleton.getInstance().getUser();
        try {
            mainUser = (User) mainUser.loadJson(userProfile, getApplicationContext());
            addressView.setText(mainUser.getAddress());
            emailView.setText(mainUser.getEmail());
            phoneView.setText(mainUser.getPhoneNumber());
            userNameView.setText(mainUser.getUserName());

            //Toast.makeText(this, mainUser.getAddress(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        editprof = (Button) findViewById(R.id.editProfile);
        editprof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        inventory = (Button) findViewById(R.id.viewInventory);
        inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, InventoryListActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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
