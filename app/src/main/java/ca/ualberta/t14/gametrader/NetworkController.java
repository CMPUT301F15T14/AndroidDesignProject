package ca.ualberta.t14.gametrader;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import ca.ualberta.t14.gametrader.es.data.ElasticSearchResponse;
import ca.ualberta.t14.gametrader.es.data.ElasticSearchSearchResponse;

/**
 * Talks to the elastic search user. Supports adding, loading, and updating users.
 * Stole a lot of code from ESDemo
 * Created by jjohnsto on 11/26/15.
 */
public class NetworkController implements AppObserver {
    Boolean isInternetPresent;

    private final String netLocation = "http://cmput301.softwareprocess.es:8080/testing/t14/";

    private HttpClient httpclient = new DefaultHttpClient();
    Gson gson = new Gson();

    public ArrayList<User> SearchByUserName(String str) throws IOException {
        HttpPost searchRequest = new HttpPost(netLocation + "_search?pretty=1");
        String query = 	"{\"query\" : {\"query_string\" : {\"default_field\" : \"userName\",\"query\" : \"" + str + "\"}}}";
        StringEntity stringentity = new StringEntity(query);

        searchRequest.setHeader("Accept","application/json");
        searchRequest.setEntity(stringentity);

        HttpResponse response = httpclient.execute(searchRequest);
        String status = response.getStatusLine().toString();
        System.out.println(status);

        String json = getEntityContent(response);

        Type elasticSearchSearchResponseType = new TypeToken<ElasticSearchSearchResponse<User>>(){}.getType();
        ElasticSearchSearchResponse<User> esResponse = gson.fromJson(json, elasticSearchSearchResponseType);
        System.err.println(esResponse);

        ArrayList<User> returnValue = new ArrayList<User>();
        if(esResponse.getHits()!=null) {
            for (ElasticSearchResponse<User> r : esResponse.getHits()) {
                User result = r.getSource();
                User ret = new User();
                ret.setAddress(result.getAddress());
                ret.setPhoneNumber(result.getPhoneNumber());
                ret.setAndroidID(result.getAndroidID());
                ret.setUserName(result.getUserName());
                ret.setEmail(result.getEmail());
                ret.setInventory(result.getInventory());

                returnValue.add(ret);
            }
        }

        return returnValue;
    }

    /**
     * Uploads user data to the elastic search server. If the user has already been added, their data
     * will be updated
     * @param user is the user we want to upload. This will always be the phone's user, accessed
     *             through the singleton
     * @throws IllegalStateException
     * @throws IOException
     */
    public void AddUser(User user) throws IllegalStateException, IOException {
        HttpPost httpPost = new HttpPost(netLocation+user.getAndroidID());

        System.out.println("Trying to write user to: " + netLocation+user.getAndroidID());

        StringEntity stringentity = null;

        isInternetPresent = MainActivity.networkConnectivity.isConnectingToInternet();

        while(!isInternetPresent){
            isInternetPresent = MainActivity.networkConnectivity.isConnectingToInternet();
        }

        try {
            stringentity = new StringEntity(gson.toJson(user));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        httpPost.setHeader("Accept","application/json");

        httpPost.setEntity(stringentity);
        HttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String status = response.getStatusLine().toString();
        System.out.println(status);
        HttpEntity entity = response.getEntity();
        BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
        String output;
        System.err.println("Output from Server -> ");
        while ((output = br.readLine()) != null) {
            System.err.println(output);
        }

        //httpPost.releaseConnection();
    }

    /**
     * Fetches a user from the server. This is necessary for getting information (user profile,
     * inventory) about any other user using our app. Necessary when adding friends, and browsing
     * their profile/inventory.
     * @param id is the android device id used to index users in the elastic search server.
     * @return a User object filled with the relevent profile/inventory data.
     */
    public User LoadUser(String id) {
        User user = null;

        try{
            HttpGet getRequest = new HttpGet(netLocation+id);

            getRequest.addHeader("Accept","application/json");

            HttpResponse response = httpclient.execute(getRequest);

            String status = response.getStatusLine().toString();
            System.out.println(status);

            String json = getEntityContent(response);

            // We have to tell GSON what type we expect
            Type elasticSearchResponseType = new TypeToken<ElasticSearchResponse<User>>(){}.getType();
            // Now we expect to get a Recipe response
            ElasticSearchResponse<User> esResponse = gson.fromJson(json, elasticSearchResponseType);
            // We get the recipe from it!
            user = esResponse.getSource();

            System.out.println(user.toString());

            return user;
        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
        }

        return user;
    }

    /**
     * NetworkController observers the User. Whenever the user edits their profile/inventory/games,
     * we want to send the updated data to the server.
     * @param observable contains the object that is being observed by this class.
     */
    public void appNotify(final AppObservable observable) {
        System.out.println("Network controller was notified");
        final User test = UserSingleton.getInstance().getUser();

        System.out.println("My name is: " + test.getUserName());

        if(observable.getClass() == User.class){
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("Updating user...");
                        AddUser(test);
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
        else {
            throw new RuntimeException("Unsupported observable passed to NetworkController");
        }
    }

    /**
     * Reads http data whenever grab data from the server. Not our code (taken from the ES example
     * project)
     * @param response response from the elastic search server
     * @return a string with the json data retreived from the server
     * @throws IOException
     */
    String getEntityContent(HttpResponse response) throws IOException {
        BufferedReader br = new BufferedReader(
                new InputStreamReader((response.getEntity().getContent())));
        String output;
        System.err.println("Output from Server -> ");
        String json = "";
        while ((output = br.readLine()) != null) {
            System.err.println(output);
            json += output;
        }
        System.err.println("JSON:"+json);
        return json;
    }
}
