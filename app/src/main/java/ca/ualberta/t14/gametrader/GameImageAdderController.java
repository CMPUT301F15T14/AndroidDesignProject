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
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * This class is responsible for the GameImageAdder view.
 * It will handle all input buttons etc.
 *
 * @author  Ryan Satyabrata
 */
public class GameImageAdderController {
    public static final int PICK_IMAGE = 81353;
    Context context;
    Activity activity;
    LinearLayout imagesLayout;
    ArrayList<Uri> uriToReturn;

    private class UriRemoveButton implements Button.OnClickListener {
        private Uri uriToRemove;
        private View viewToRemove;

        UriRemoveButton(Uri uri, View viewAdd) {
            uriToRemove = uri;
            viewToRemove = viewAdd;
        }

        @Override
        public void onClick(View v) {
            uriToReturn.remove(uriToRemove);
            LinearLayout layout = (LinearLayout) activity.findViewById(R.id.ImageAdderImagesLayout);
            layout.removeView(viewToRemove);
            layout.refreshDrawableState();
        }
    }

    /**
     * The constructor for GameImageAdderController, which will initialize all the private variables.
     * It will store the context and activity. Also makes that the scrollListView have a fancy fade when scrolling.
     * @param context
     * @param activity
     */
    public GameImageAdderController(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        uriToReturn = new ArrayList<Uri>();

        // make fancy fading graphics haha.
        (activity.findViewById(R.id.ImageAdderScrollView)).setVerticalFadingEdgeEnabled(Boolean.TRUE);

        imagesLayout = (LinearLayout) activity.findViewById(R.id.ImageAdderImagesLayout);
    }

    /**
     * This method will set all the onclick listeners for the activity GameImageAdder.
     */
    public void setOnClickListeners() {
        Button addImages = (Button) activity.findViewById(R.id.ImageAdderAddImages);
        addImages.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                activity.startActivityForResult(Intent.createChooser(intent, "Select photo representing the game:"), PICK_IMAGE);
            }
        });

        Button saveButton = (Button) activity.findViewById(R.id.ImageAdderSave);
        saveButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putParcelableArrayListExtra("result", uriToReturn);
                activity.setResult(Activity.RESULT_OK, returnIntent);
                activity.finish();
            }
        });

    }

    /**
     * This method will add an Uri to the list of potential added images.
     * each potential added image will then be gotten as a bitmap and displayed
     * in an ImageView, a RemoveImage button will also be created for tat image, if the user
     * decides after all to not add this image to the added images.
     * @param uri The uri of where the bitmap is.
     * @param cr The content resolver from the activity for the uri.
     */
    public void addAnImageUri(Uri uri, ContentResolver cr) {
        Bitmap addImage = null;
        try {
            InputStream imageStream = cr.openInputStream(uri);

            BitmapFactory.Options o = new BitmapFactory.Options();

            addImage = BitmapFactory.decodeStream(imageStream, null, o);
            imageStream.close();
        } catch(Exception e) {
            e.printStackTrace();
        }

        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        int goodHeight = Math.round(0.6f * (dm.heightPixels / dm.density));

        LinearLayout entry = new LinearLayout(context);
        entry.setOrientation(LinearLayout.VERTICAL);
        entry.setWeightSum(6.0f);
        entry.setPadding(0, 0, 0, 40);
        entry.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, goodHeight, 1.0f));

        uriToReturn.add(uri);

        Button deleteThatImage = new Button(context);
        deleteThatImage.setText("Remove");
        deleteThatImage.setOnClickListener(new UriRemoveButton(uri, entry));

        // Taken from http://stackoverflow.com/questions/4641072/how-to-set-layout-weight-attribute-dynamically-from-code
        deleteThatImage.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 4.0f));

        ImageView image = new ImageView(context);
        image.setImageBitmap(addImage);
        image.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 2.0f));

        entry.addView(image, 0);
        entry.addView(deleteThatImage, 1);

        imagesLayout.addView(entry, 0);
        imagesLayout.refreshDrawableState();

    }


}
