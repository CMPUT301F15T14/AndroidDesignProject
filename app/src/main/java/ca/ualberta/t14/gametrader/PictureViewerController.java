package ca.ualberta.t14.gametrader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.FileNotFoundException;

/**
 * Created by satyabra on 11/28/15.
 */
public class PictureViewerController {

    private Context context;
    private Activity activity;

    public PictureViewerController(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void setButtonClickers() {
        Button buttonClose = (Button) activity.findViewById(R.id.PictureViewerClose);
        buttonClose.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                activity.finish();
            }
        });
        }

    public void putImages(Game game, Boolean askDownload) {

        if(!game.pictureIdIsEmpty()) {
            for(String eachId : game.getPictureIds()) {
                try {
                    String loadedJson = PictureManager.loadImageJsonFromJsonFile(eachId, context);
                    setImageBitmap(PictureManager.getBitmapFromJson(loadedJson));
                } catch(FileNotFoundException e) {
                    if (SettingsSingleton.getInstance().getSettings().getEnableDownloadPhoto1()) {
                        for (String each : game.getPictureIds()) {
                            PictureNetworkerSingleton.getInstance().getPicNetMangager().addImageToDownload(each, context);
                        }
                    } else {
                        if(askDownload) {
                            final Game g = game;
                            AlertDialog SinglePrompt = new AlertDialog.Builder(activity).create();
                            SinglePrompt.setTitle("Warning");
                            SinglePrompt.setMessage("Would you like to download all photos for " + g.getTitle() + "?");
                            SinglePrompt.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            for (String each : g.getPictureIds()) {
                                                PictureNetworkerSingleton.getInstance().getPicNetMangager().addImageToDownload(each, context);
                                            }
                                            dialog.dismiss();
                                        }
                                    }
                            );
                            SinglePrompt.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }
                            );
                            SinglePrompt.show();
                        }
                    }
                }
            }
        } else {
            setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.cd_empty));
        }

    }

    private void setImageBitmap(Bitmap img) {
        LinearLayout photoContainer = (LinearLayout) activity.findViewById(R.id.viewPhotoScroller);

        ImageView iv = new ImageView(activity.getApplicationContext());
        iv.setImageBitmap(img);
        iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        iv.setPadding(15, 0, 15, 15);

        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        int goodHeight = Math.round(0.6f * (dm.heightPixels / dm.density));
        iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,goodHeight));

        // -1 add with largest index in other words, last.
        photoContainer.addView(iv, -1);
    }

    public void clearAllImages() {
        LinearLayout photoContainer = (LinearLayout) activity.findViewById(R.id.viewPhotoScroller);
        photoContainer.removeAllViewsInLayout();
    }

}
