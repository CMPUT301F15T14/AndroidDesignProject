package ca.ualberta.t14.gametrader;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.BoringLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by satyabra on 11/28/15.
 */
public class GameImageAdderController {
    public static final int PICK_IMAGE = 81353;
    Context context;
    Activity activity;
    LinearLayout imagesLayout;
    HashMap<String, UriBitmapPair> uriBitmapPair;
    Integer uriBitPair;

    GameImageAdderController(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;

        uriBitPair = 0;
        uriBitmapPair = new HashMap<String, UriBitmapPair>();

        // make fancy fading graphics haha.
        (activity.findViewById(R.id.ImageAdderScrollView)).setVerticalFadingEdgeEnabled(Boolean.TRUE);

        imagesLayout = (LinearLayout) activity.findViewById(R.id.ImageAdderImagesLayout);
    }

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
                // SAVE STUFF AAAAAAjiofsjiji
            }
        });

    }

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

        final String id = uriBitmapPair.toString();
        uriBitmapPair.put(id, new UriBitmapPair(uri, addImage));

        Button deleteThatImage = new Button(context);
        deleteThatImage.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                uriBitmapPair.remove(id);
            }
        });

        ImageView image = new ImageView(context);
        image.setImageBitmap(addImage);

        LinearLayout entry = new LinearLayout(context);
        entry.setOrientation(LinearLayout.HORIZONTAL);
        entry.addView(deleteThatImage, -1);
        entry.addView(image, -1);

        imagesLayout.addView(entry, -1);
        uriBitPair += 1;
    }


}
