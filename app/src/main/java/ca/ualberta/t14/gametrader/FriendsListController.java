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
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.InputFilter;
import android.text.LoginFilter;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * this is a class that handles input for the FriendsListActivity
 * @author Ryan Satyabrata
 */
public class FriendsListController {
    FriendsController fc;
    Context context;
    private final int EDIT_TEXT_ID = 1337;

    private Button addFriend;
    private RelativeLayout friendListLayout;
    private EditText inputFriend;

    private final String FRIEND_TEXT_DEFAULT = "Add Friend";
    private final String FRIEND_TEXT_REQ = "add as Friend!";

    private String userToAdd;


    // 2 states, if false, no input text and clicking it will open a text box. True then clicking it will submit that text and remove that editText.
    private Boolean addFriendButtonState;

    FriendsListController(Context context) {
        addFriendButtonState = Boolean.FALSE;
        this.context = context;
        fc = new FriendsController(UserSingleton.getInstance().getUser().getFriends(), context);
    }

    /*void initButonOnClickListeners(Activity activity) {

        final Context c = context;
        final Activity a = activity;
        addFriend = (Button) activity.findViewById(R.id.addFriendButton);

        friendListLayout = (RelativeLayout) activity.findViewById(R.id.friendListRelLayout);

        addFriend.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!addFriendButtonState) {
                    inputFriend = new EditText(c);
                    inputFriend.setId(EDIT_TEXT_ID);
                    inputFriend.setSelectAllOnFocus(Boolean.TRUE);
                    inputFriend.requestFocus();
                    inputFriend.dispatchWindowFocusChanged(Boolean.TRUE);

                    // #3399FF
                    inputFriend.setBackgroundColor(Color.rgb(255, 255, 255));
                    inputFriend.setTextColor(Color.rgb(0, 0, 0));
                    inputFriend.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    inputFriend.setMaxLines(1);
                    inputFriend.setMinWidth(150);
                    inputFriend.setLines(1);
                    InputFilter[] a = { new InputFilter.LengthFilter(14), new LoginFilter.UsernameFilterGeneric()};
                    inputFriend.setFilters(a);
                    friendListLayout.addView(inputFriend, 1);

                    addFriend.setText(FRIEND_TEXT_REQ);

                    addFriendButtonState = Boolean.TRUE;
                } else if (addFriendButtonState) {
                    userToAdd = inputFriend.getText().toString();
                    if(userToAdd.isEmpty()) {
                        userToAdd = "";
                    } else {
                        // query the server for a friend with the given user name
                        fc.AddFriend(userToAdd);
                        friendListLayout.removeView(inputFriend);

                        addFriend.setText(FRIEND_TEXT_DEFAULT);
                        addFriendButtonState = Boolean.FALSE;
                    }
                }
            }
        });
    }*/

}
