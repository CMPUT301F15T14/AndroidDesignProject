package ca.ualberta.t14.gametrader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ProfileActivity extends Activity {
    User user;

    TextView userNameView;
    TextView phoneView;
    TextView emailView;
    TextView addressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user = UserSingleton.getInstance().getUser();

        Button editprof = (Button) findViewById(R.id.editProfile);
        editprof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        Button inventory = (Button) findViewById(R.id.viewInventory);
        inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, InventoryListActivity.class);
                startActivity(intent);
            }
        });

        userNameView = (TextView) findViewById(R.id.userNameDisplay);
        userNameView.setText(user.getUserName());

        phoneView = (TextView) findViewById(R.id.phoneDisplay);
        phoneView.setText(user.getPhoneNumber());

        emailView = (TextView) findViewById(R.id.emailDisplay);
        emailView.setText(user.getEmail());

        addressView = (TextView) findViewById(R.id.addressDisplay);
        addressView.setText(user.getAddress());
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
