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
 * This class controls the InventoryItemPictureViewer view
 * @author Ryan Satyabrata
 */
public class PictureViewerController {

    private Context context;
    private Activity activity;

    /**
     * Constructor for the PictureViewerController class.
     * @param context
     * @param activity
     */
    public PictureViewerController(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    /**
     * Sets the view button's functionality. Basically just the close button.
     */
    public void setButtonClickers() {
        Button buttonClose = (Button) activity.findViewById(R.id.PictureViewerClose);
        buttonClose.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                activity.finish();
            }
        });
        }

    /**
     * This method tries to retrieve all images of the given game, and put them into the view's body
     * so that the images will be displayed.
     * It will first try load image from disk, if found. If nonexistend on local storage, it will
     * try to download it from the elastic server. If settings are so that disables automatic download
     * it will ask if the user wants to download all the images only if the askDownload parameter is True.
     * @param game
     * @param askDownload whether to display a dialog that ask to download all images or not
     */
    public void putImages(Game game, Boolean askDownload) {
        PictureNetworker pn = PictureNetworkerSingleton.getInstance().getPicNetMangager();
        if (!SettingsSingleton.getInstance().getSettings().getEnableDownloadPhoto1()
                && !pn.getLocalCopyOfImageIds().containsAll(game.getPictureIds())) {
            askManualDownloadImages(game, askDownload);
        } else if(!pn.getLocalCopyOfImageIds().containsAll(game.getPictureIds())) {
            downloadImages(game);
        } else {
            for (String eachId : game.getPictureIds()) {
                loadImages(eachId);
            }
        }
    }

    private void downloadImages(Game game) {
        if (!game.pictureIdIsEmpty()) {
            final Game g = game;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int num = g.getPictureIds().size();
                    for (int i = 0; i < num; i++) {
                        setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.saveimages));
                    }
                }
            });
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    for (String eachId : g.getPictureIds()) {
                        PictureNetworkerSingleton.getInstance().getPicNetMangager().addImageToDownload(eachId);
                    }
                }
            };
            Thread t = new Thread(r);
            t.start();
        }
    }

    private void loadImages(String imageId) {
        try {
            String loadedJson = PictureManager.loadImageJsonFromJsonFile(imageId, context);
            setImageBitmap(PictureManager.getBitmapFromJson(loadedJson));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void askManualDownloadImages(Game game, Boolean askDownload) {
        if(askDownload) {
            final Game g = game;
            AlertDialog SinglePrompt = new AlertDialog.Builder(activity).create();
            SinglePrompt.setTitle("Warning");
            SinglePrompt.setMessage("Would you like to download all photos for " + g.getTitle() + "?");
            SinglePrompt.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            downloadImages(g);
                            dialog.dismiss();
                        }
                    }
            );
            SinglePrompt.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.cd_empty));
                            dialog.dismiss();
                        }
                    }
            );
            SinglePrompt.show();
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

    /**
     * Removes all the images from inside the view.
     */
    public void clearAllImages() {
        LinearLayout photoContainer = (LinearLayout) activity.findViewById(R.id.viewPhotoScroller);
        photoContainer.removeAllViewsInLayout();
    }

}
