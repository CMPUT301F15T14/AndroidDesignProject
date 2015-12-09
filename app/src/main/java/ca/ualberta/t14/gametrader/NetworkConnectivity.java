package ca.ualberta.t14.gametrader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Check Internet status
 * Source code is from http://www.androidhive.info/2012/07/android-detect-internet-connection-status/
 * Created by Brigitte-Ng on 2015-11-28.
 */
public class NetworkConnectivity{

    private Context _context;
    public static String IS_NETWORK_ONLINE = "5465146468";

    /**
     * Constructor for NetworkConnectivity.
     * @param context
     */
    public NetworkConnectivity(Context context){
        this._context = context;
    }

    /**This is to check if the phone is connected to network.
     * Source code is from http://www.androidhive.info/2012/07/android-detect-internet-connection-status/
     * @return a boolean with the network connectivity
     */
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