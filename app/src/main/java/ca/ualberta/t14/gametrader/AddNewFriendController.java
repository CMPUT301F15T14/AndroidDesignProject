package ca.ualberta.t14.gametrader;

import android.content.Context;
import android.os.AsyncTask;
import java.util.ArrayList;

/**
 * Created by michaelximac on 2015-12-05.
 */
public class AddNewFriendController {
    private ArrayList<User> UserListAll;
    private Context context;
    public AddNewFriendController(Context context){
        this.context=context;
        UserListAll = new ArrayList<User>();
    }

    public void getAllUser(){
        new GetUserThread().execute();
    }

    private class GetUserThread extends AsyncTask<String, Integer, ArrayList<User>> {
        protected ArrayList<User> doInBackground(String... params) {
            NetworkController nc = new NetworkController(context);

            ArrayList<User> results = nc.getUserList();

            if(!results.isEmpty()) {
                return results;
            }

            return new ArrayList<User>();
        }

        protected void onPostExecute(ArrayList<User> result) {
            super.onPostExecute(result);
            if(result != null) {
                UserListAll=result;
            }
        }
    }
}
