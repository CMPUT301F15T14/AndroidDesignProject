package ca.ualberta.t14.gametrader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

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

    public void putImages(Game game) {

        if(!game.pictureIdIsEmpty()) {
            for(String eachId : game.getPictureIds()) {
                String loadedJson = UserSingleton.getInstance().getUser().getPictureManager().loadImageJsonFromJsonFile(eachId, context);
                setImageBitmap(PictureManager.getBitmapFromJson(loadedJson));
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
