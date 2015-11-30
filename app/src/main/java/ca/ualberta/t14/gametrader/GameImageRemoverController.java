package ca.ualberta.t14.gametrader;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by satyabra on 11/29/15.
 */
public class GameImageRemoverController {
    Context context;
    Activity activity;
    ArrayList<Uri> uriArrayList;

    public GameImageRemoverController(ArrayList<Uri> uriList, Context context, Activity activity) {
        this.context = context;
        this.activity = activity;

        uriArrayList = uriList;
        if(uriArrayList == null) {
            uriArrayList = new ArrayList<Uri>();
        }

    }



}
