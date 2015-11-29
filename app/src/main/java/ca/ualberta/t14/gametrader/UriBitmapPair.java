package ca.ualberta.t14.gametrader;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by satyabra on 11/28/15.
 */
public class UriBitmapPair {
    private Bitmap bitmap;
    private Uri uri;

    UriBitmapPair(Uri uri, Bitmap bitmap) {
        this.bitmap = bitmap;
        this.uri = uri;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
