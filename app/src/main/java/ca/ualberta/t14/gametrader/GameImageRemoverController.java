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
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by satyabra on 11/29/15.
 */
public class GameImageRemoverController {
    private Context context;
    private Activity activity;
    private LinearLayout imagesLayout;
    private ArrayList<String> imageIdList;
    private ArrayList<Uri> uriIdList;

    private Integer checkboxIds;
    private ArrayList<Integer> eachCheckboxId;
    private ArrayList<Integer> eachCheckBoxIdUri;

    private HashMap<Integer, String> checkBoxAndImageId;
    private HashMap<Integer, Uri> checkBoxAndUri;

    public GameImageRemoverController(ArrayList<String> imageIds, ArrayList<Uri> uriIds, Context context, Activity activity) {
        this.context = context;
        this.activity = activity;

        eachCheckboxId = new ArrayList<Integer>();
        checkBoxAndImageId = new HashMap<Integer, String>();

        eachCheckBoxIdUri = new ArrayList<Integer>();
        checkBoxAndUri = new HashMap<Integer, Uri>();

        checkboxIds = 12654;

        imagesLayout = (LinearLayout) activity.findViewById(R.id.ImageRemoverImagesLayout);

        imageIdList = imageIds;
        if(imageIdList == null) {
            imageIdList = new ArrayList<String>();
        }

        uriIdList = uriIds;
        if(uriIdList == null) {
            uriIdList = new ArrayList<Uri>();
        }

        createTheContent();
    }

    private void createTheContent() {
        for(String each : imageIdList) {
            String imgJson = new String();
            try {
                imgJson = PictureManager.loadImageJsonFromJsonFile(each, context);

            } catch(IOException e) {
                e.printStackTrace();
            }

            Bitmap showImage = PictureManager.getBitmapFromJson(imgJson);

            addToViews(showImage, checkboxIds);
            eachCheckboxId.add(checkboxIds);
            checkBoxAndImageId.put(checkboxIds, each);

            checkboxIds++;

        }

        for(Uri each : uriIdList) {
            Bitmap showUriImg = PictureManager.resolveUri(each , activity.getContentResolver());
            addToViews(showUriImg, checkboxIds);
            eachCheckBoxIdUri.add(checkboxIds);
            checkBoxAndUri.put(checkboxIds, each);

            checkboxIds++;
        }

        imagesLayout.refreshDrawableState();
    }

    private void addToViews(Bitmap showImage, int idNum) {

        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        int goodHeight = Math.round(0.6f * (dm.heightPixels / dm.density));

        LinearLayout entry = new LinearLayout(context);
        entry.setOrientation(LinearLayout.VERTICAL);
        entry.setWeightSum(6.0f);
        entry.setPadding(0, 0, 0, 40);
        entry.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, goodHeight, 1.0f));

        CheckBox checkBox = new CheckBox(context);
        checkBox.setId(idNum);

        // Taken from http://stackoverflow.com/questions/4641072/how-to-set-layout-weight-attribute-dynamically-from-code
        checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 4.5f));
        checkBox.setBackgroundColor(Color.rgb(20, 130, 168));
        checkBox.setGravity(Gravity.CENTER);

        ImageView image = new ImageView(context);
        image.setImageBitmap(showImage);
        image.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.5f));

        entry.addView(image, 0);
        entry.addView(checkBox, 1);

        imagesLayout.addView(entry, -1);
    }

    public void setOnClickButtons() {
        Button btnUncheck = (Button) activity.findViewById(R.id.ImageRemoverCheckAll);
        btnUncheck.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Integer> iterating = eachCheckboxId;
                iterating.addAll(eachCheckBoxIdUri);
                for(Integer each : eachCheckboxId) {
                   CheckBox checkBox = (CheckBox) activity.findViewById(each);
                    checkBox.setChecked(Boolean.TRUE);
                }
            }
        });

        Button btnSave = (Button) activity.findViewById(R.id.ImageRemoverSave);
        btnSave.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> idsToRemove = new ArrayList<String>();
                for(Integer each : eachCheckboxId) {

                    CheckBox checkbox = (CheckBox) activity.findViewById(each);
                    if(checkbox.isChecked()) {
                        idsToRemove.add(checkBoxAndImageId.get(each));
                    }
                }

                ArrayList<Uri> urisToRemove = new ArrayList<Uri>();
                for(Integer each : eachCheckBoxIdUri) {
                    CheckBox checkbox = (CheckBox) activity.findViewById(each);
                    if(checkbox.isChecked()) {
                        urisToRemove.add(checkBoxAndUri.get(each));
                    }
                }

                Intent returnIntent = new Intent();
                returnIntent.putStringArrayListExtra("resultIdsToRem", idsToRemove);
                returnIntent.putParcelableArrayListExtra("resultsUrisToRem", urisToRemove);
                activity.setResult(Activity.RESULT_OK, returnIntent);
                activity.finish();
            }
        });

    }

}
