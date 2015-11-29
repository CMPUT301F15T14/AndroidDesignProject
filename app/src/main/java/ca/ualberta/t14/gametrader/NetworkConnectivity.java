package ca.ualberta.t14.gametrader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Brigitte-Ng on 2015-11-28.
 */
public class NetworkConnectivity{
    /* This is to check if the phone is connected to network.
     * Source code is from http://www.androidhive.info/2012/07/android-detect-internet-connection-status/
     * @return a boolean with the network connectivity
     */
    private Context _context;

    public NetworkConnectivity(Context context){
        this._context = context;
    }

    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }
}