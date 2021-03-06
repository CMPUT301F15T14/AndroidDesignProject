package ca.ualberta.t14.gametrader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
    TextView userNameView;
    TextView phoneView;
    TextView emailView;
    TextView addressView;
    TextView profileText;
    private AlertDialog removeDialogue;

    public AlertDialog getRemoveDialogue() {
        return removeDialogue;
    }

    private Button editprof;

    public Button getEditprofButton() {
        return editprof;
    }

    private Button inventory;

    public Button getInventoryButton() {
        return inventory;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private User user;
    private FriendsController friendsController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        friendsController=new FriendsController(UserSingleton.getInstance().getUser().getFriends(),getApplicationContext());
        profileText= (TextView) findViewById(R.id.profileText);
        addressView = (TextView) findViewById(R.id.addressDisplay);
        emailView = (TextView) findViewById(R.id.emailDisplay);
        phoneView = (TextView) findViewById(R.id.phoneDisplay);
        userNameView = (TextView) findViewById(R.id.userNameDisplay);

        user = (User) ObjParseSingleton.getInstance().popObject("userProfile");

        if(user == null){
            throw new RuntimeException("ProfileActivity received null user.");
        }

        addressView.setText(user.getAddress());
        emailView.setText(user.getEmail());
        phoneView.setText(user.getPhoneNumber());
        userNameView.setText(user.getUserName());

        //Toast.makeText(this, mainUser.getAddress(), Toast.LENGTH_SHORT).show();

        editprof = (Button) findViewById(R.id.editProfile);

        if (user.isUser(user)){
            profileText.setText("My Profile");
            editprof.setText("Edit");
            editprof.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }else if (user.isFriend(user)){
            profileText.setText(user.getUserName()+"'s Profile");
            editprof.setText("Remove");
            editprof.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeDialogue = new AlertDialog.Builder(ProfileActivity.this).create();
                    removeDialogue.setTitle("Warning");
                    removeDialogue.setMessage("Are you sure you want to remove this user from friend list?");
                    removeDialogue.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    friendsController.RemoveFriend(user);
                                    Toast.makeText(ProfileActivity.this, user.getUserName() + " has been removed from your friend list!", Toast.LENGTH_SHORT).show();
                                    finish();
                                    finish();
                                    dialog.dismiss();
                                }
                            }
                    );

                    removeDialogue.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }
                    );

                    removeDialogue.show();

                }
            });
        }else{
            profileText.setText(user.getUserName()+"'s Profile");
            editprof.setText("Add Friend");
            editprof.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    friendsController.AddFriend(user.getUserName());
                    Toast.makeText(ProfileActivity.this, user.getUserName()+" has been added to your friend list!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }

        inventory = (Button) findViewById(R.id.viewInventory);
        inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjParseSingleton.getInstance().addObject("User", user);
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
            Intent intent = new Intent(ProfileActivity.this, SettingActivity.class);
            startActivity(intent);        }

        return super.onOptionsItemSelected(item);
    }
}
