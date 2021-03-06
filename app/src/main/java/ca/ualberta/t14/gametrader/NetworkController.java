package ca.ualberta.t14.gametrader;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Observer;

import ca.ualberta.t14.gametrader.es.data.ElasticSearchResponse;
import ca.ualberta.t14.gametrader.es.data.ElasticSearchSearchResponse;
import ca.ualberta.t14.gametrader.es.data.ImagePackage;

/**
 * Talks to the elastic search user. Supports adding, loading, and updating users.
 * Stole a lot of code from ESDemo
 * Created by jjohnsto on 11/26/15.
 */
public class NetworkController implements AppObserver, NetworkerListener, AppObservable {
    
    transient Context context;

    private transient ArrayList<AppObserver> observers;

    private final String netLocation = "http://cmput301.softwareprocess.es:8080/t14/Users/";
    private final String tradesLocation = "http://cmput301.softwareprocess.es:8080/t14/Trades/";
    private final String imagesLocation = "http://cmput301.softwareprocess.es:8080/t14/Images/";

    Boolean isInternetPresent;

    private HttpClient httpclient = new DefaultHttpClient();
    Gson gson = new Gson();

    /**
     * Constructor for NetworkController. Sets TradeNetworker, PictureNetworker,
     * and User of device as observers to this object. Initializes private variables.
     * @param context
     */
    public NetworkController(Context context) {
        TradeNetworkerSingleton.getInstance().getTradeNetMangager().addListener(this);
        PictureNetworkerSingleton.getInstance().getPicNetMangager().addListener(this);
        UserSingleton.getInstance().getUser().addObserver(this);
        this.context = context;
        observers=new ArrayList<AppObserver>();
    }

    /**
     * Method to get the list of all users from elastic search.
     * @return an array list of Users.
     */
    public ArrayList<User> getUserList() {
        HttpPost searchRequest = new HttpPost(netLocation + "_search?pretty=1");

        searchRequest.setHeader("Accept", "application/json");

        try {
            HttpResponse response = httpclient.execute(searchRequest);

            String status = response.getStatusLine().toString();
            System.out.println(status);

            String json = getEntityContent(response);

            Type elasticSearchSearchResponseType = new TypeToken<ElasticSearchSearchResponse<User>>() {
            }.getType();
            ElasticSearchSearchResponse<User> esResponse = gson.fromJson(json, elasticSearchSearchResponseType);
            //System.err.println(esResponse);

            ArrayList < User > returnValue = new ArrayList<User>();
            if (esResponse.getHits() != null) {
                for (ElasticSearchResponse<User> r : esResponse.getHits()) {
                    User result = r.getSource();
                    returnValue.add(result);
                }

                return returnValue;
            }
            searchRequest.getEntity().consumeContent();
            response.getEntity().consumeContent();
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * This method Searches the elastic search for the given username.
     * @param str the username string to search for
     * @return an array list of Users that matched the search query.
     * @throws IOException
     */
    public ArrayList<User> searchByUserName(String str) throws IOException {
        HttpPost searchRequest = new HttpPost(netLocation + "_search?pretty=1");
        String query = 	"{\"query\" : {\"query_string\" : {\"default_field\" : \"userName\",\"query\" : \"" + str + "\"}}}";
        StringEntity stringentity = new StringEntity(query);

        searchRequest.setHeader("Accept", "application/json");
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

                returnValue.add(result);
            }
        }
        searchRequest.getEntity().consumeContent();
        response.getEntity().consumeContent();
        notifyAllObservers();

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
    public void addUser(User user) throws IllegalStateException, IOException {
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
        httpPost.getEntity().consumeContent();
        response.getEntity().consumeContent();
    }

    /**
     * Fetches a user from the server. This is necessary for getting information (user profile,
     * inventory) about any other user using our app. Necessary when adding friends, and browsing
     * their profile/inventory.
     * @param id is the android device id used to index users in the elastic search server.
     * @return a User object filled with the relevent profile/inventory data.
     */
    public User loadUser(String id) {
        User user = null;

        try{
            HttpGet getRequest = new HttpGet(netLocation+id);

            getRequest.addHeader("Accept", "application/json");

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

            response.getEntity().consumeContent();

            return user;
        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
        }

        return user;
    }

    /**
     * This method converts the trade into json and then uploads it to the elastic search server.
     * @param trade
     */
    public Boolean postTrade(Trade trade) {
        HttpPost httpPost = new HttpPost(tradesLocation + trade.getTradeId());

        StringEntity stringentity = null;
        try {
            stringentity = new StringEntity(gson.toJson(trade));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return Boolean.FALSE;
        }
        httpPost.setHeader("Accept", "application/json");

        httpPost.setEntity(stringentity);
        HttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return Boolean.FALSE;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return Boolean.FALSE;
        }

        String status = response.getStatusLine().toString();
        System.out.println(status);
        HttpEntity entity = response.getEntity();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));

            String output;
            System.err.println("Output from Server -> ");

            while ((output = br.readLine()) != null) {
                System.err.println(output);
            }
            httpPost.getEntity().consumeContent();
            response.getEntity().consumeContent();
            entity.consumeContent();

        }
        catch (IOException e){
            e.printStackTrace();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * This method retrieves all trades for the given user's android id. The user is involved in the
     * trade if they are borrower or owner of the trade.
     * @param id the device user's android id.
     * @return the array list of all the trades that the given user is involved in.
     */
    public ArrayList<Trade> getMyTrades(String id) {
        String searchWho = "&q=" + id;
        HttpPost searchRequest = new HttpPost(tradesLocation + "_search?pretty=1" + searchWho);

        searchRequest.setHeader("Accept", "application/json");

        try {
            HttpResponse response = httpclient.execute(searchRequest);

            String status = response.getStatusLine().toString();
            System.out.println(status);

            String json = getEntityContent(response);

            Type elasticSearchSearchResponseType = new TypeToken<ElasticSearchSearchResponse<Trade>>() {
            }.getType();
            ElasticSearchSearchResponse<Trade> esResponse = gson.fromJson(json, elasticSearchSearchResponseType);
            System.err.println(esResponse);

            ArrayList < Trade > returnValue = new ArrayList<Trade>();
            if (esResponse.getHits() != null) {
                for (ElasticSearchResponse<Trade> r : esResponse.getHits()) {
                    Trade result = r.getSource();
                    returnValue.add(result);
                }

                return returnValue;
            }
            searchRequest.getEntity().consumeContent();
            response.getEntity().consumeContent();
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * This method removes a given url from the elastic search server.
     * Code taken from the ES Demo project.
     * @throws IOException
     */
    public void deleteGeneric(String removeUrl) {
        HttpDelete httpDelete = new HttpDelete(removeUrl);
        httpDelete.addHeader("Accept", "application/json");

        try {
            HttpResponse response = httpclient.execute(httpDelete);

            String status = response.getStatusLine().toString();
            System.out.println(status);

            HttpEntity entity = response.getEntity();
            BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
            String output;
            System.err.println("Output from Server -> ");
            while ((output = br.readLine()) != null) {
                System.err.println(output);
            }
            response.getEntity().consumeContent();
            entity.consumeContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method wraps the base64 encoded image into a class that stores the encoding and
     * the imageID. That class gets converted into json and uploaded to the elastic search server.
     * @param imageId the image ID of the image.
     * @param json the base64 string representation of the image.
     * @return whether this method was successful or not.
     */
    public Boolean postImages(String imageId, String json) {
        HttpPost httpPost = new HttpPost(imagesLocation + imageId);

        System.out.println(imagesLocation + imageId);
        ImagePackage imagePackage = new ImagePackage();
        imagePackage.setImageId(imageId);
        imagePackage.setImageDataUrl(json);

        StringEntity stringentity = null;
        try {
            stringentity = new StringEntity(gson.toJson(imagePackage));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return Boolean.FALSE;
        }
        httpPost.setHeader("Accept", "application/json");

        httpPost.setEntity(stringentity);
        HttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return Boolean.FALSE;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return Boolean.FALSE;
        }

        String status = response.getStatusLine().toString();
        System.out.println(status);
        HttpEntity entity = response.getEntity();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));

            String output;
            System.err.println("Output from Server -> ");

            while ((output = br.readLine()) != null) {
                System.err.println(output);
            }
            httpPost.getEntity().consumeContent();
            response.getEntity().consumeContent();
            entity.consumeContent();
            stringentity.consumeContent();

        }
        catch (IOException e){
            e.printStackTrace();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * This method, given image ID will fetch and download that given image onto the user's device.
     * The base64 encoded image will be taken out of the class and saved as a file whoese filename
     * is the image ID.
     * @param imageId
     * @param context
     * @return
     */
    public ImagePackage getImages(String imageId, Context context) {
        String searchCommand = "_search?pretty=1&q=";
        HttpPost searchRequest = new HttpPost(imagesLocation + searchCommand + imageId);
        searchRequest.setHeader("Accept", "application/json");
        ImagePackage resultImagePackage = new ImagePackage();
        try {
            HttpResponse response = httpclient.execute(searchRequest);

            String status = response.getStatusLine().toString();
            System.out.println(status);

            String json = getEntityContent(response);

            Type elasticSearchSearchResponseType = new TypeToken<ElasticSearchSearchResponse<ImagePackage>>() {
            }.getType();
            ElasticSearchSearchResponse<ImagePackage> esResponse = gson.fromJson(json, elasticSearchSearchResponseType);
            //System.err.println(esResponse);

            if (esResponse.getHits() != null) {
                for (ElasticSearchResponse<ImagePackage> r : esResponse.getHits()) {
                    resultImagePackage = r.getSource();
                    // save gotten image to disk.
                    PictureManager.saveJsonWithObject(resultImagePackage.getImageDataUrl(), imageId, context);
                }
            }

            if(response != null && response.getEntity() != null)
                response.getEntity().consumeContent();
            if(searchRequest != null && searchRequest.getEntity() != null)
                searchRequest.getEntity().consumeContent();

            return resultImagePackage;
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return resultImagePackage;
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
                        addUser(test);
                        //Todo: Should also pull all users from database?
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
        System.err.println("JSON:" + json);
        return json;
    }

    /**
     * Used by Networker classes (like TradeNetworker, and PictureNetworker).
     * They can call this method to launch this class' methods in a different thread.
     *
     * @param commandRequest
     */
    public void netListenerNotify(int commandRequest) {
        final int command = commandRequest;
        final String identityNetTrade = UserSingleton.getInstance().getUser().getAndroidID();
        final TradeNetworker tradeNetworker = TradeNetworkerSingleton.getInstance().getTradeNetMangager();
        final PictureNetworker pictureNetworker = PictureNetworkerSingleton.getInstance().getPicNetMangager();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    switch (command) {
                        case TradeNetworker.PULL_TRADES:
                            pullTrades(tradeNetworker, identityNetTrade);
                            break;
                        case TradeNetworker.PUSH_TRADES:
                            pushTrades(tradeNetworker);
                            break;
                        case TradeNetworker.PUSH_TRADES_TO_DELETE:
                            pushDeleteTrades(tradeNetworker);
                            break;
                        case PictureNetworker.PULL_IMAGES:
                            pullImages(pictureNetworker);
                            break;
                        case PictureNetworker.PUSH_IMAGE:
                            pushImages(pictureNetworker);
                            break;
                        case PictureNetworker.PUSH_IMAGES_TO_DELETE:
                            pushDeleteImages(pictureNetworker);
                            break;
                    }
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void pullTrades(TradeNetworker tradeNetworker, String identityNetTrade) {
        System.out.print("Getting all trades owner/borrower for user:" + identityNetTrade + "\n");
        ArrayList<Trade> tradingsOnline = getMyTrades(identityNetTrade);
        System.out.print("Getting all trades owner/borrower the new array go is of size:" + tradingsOnline.size() + "\n");
        tradeNetworker.setAllTradesOnNet(tradingsOnline);
        tradeNetworker.saveTradeNetworker();
        tradeNetworker.notifyAllObservers();
    }

    private void pushTrades(TradeNetworker tradeNetworker) {
        ArrayList<Trade> trades = new ArrayList<Trade>(tradeNetworker.getTradeToUpload());
        for (Trade each : trades) {
            Trade aTrade = each;
            System.out.println("Trade adding...");
            postTrade(aTrade);

            tradeNetworker.getTradeToUpload().remove(each);
            tradeNetworker.getAllTradesOnNetLocalArray().add(each);
        }
        tradeNetworker.saveTradeNetworker();
        tradeNetworker.notifyAllObservers();
    }

    private void pushDeleteTrades(TradeNetworker tradeNetworker) {
        ArrayList<Trade> tradesRemove = new ArrayList<Trade>(tradeNetworker.getTradeToRemove());
        for (Trade each : tradesRemove) {
            System.out.println("Trade removing...");
            String urlToRem = tradesLocation + each.getTradeId();
            deleteGeneric(urlToRem);
            // in case it was not uploaded remove it from ToUpload.
            tradeNetworker.getTradeToUpload().remove(each);
            tradeNetworker.getTradeToRemove().remove(each);
            tradeNetworker.getAllTradesOnNetLocalArray().remove(each);
        }
        tradeNetworker.saveTradeNetworker();
        tradeNetworker.notifyAllObservers();
    }

    private void pullImages(PictureNetworker picNetworker) {
        ArrayList<String> imgDl = new ArrayList<String>(picNetworker.getImagesToDownload());
        ImagePackage downloadedPicIds = null;
        for(String each : imgDl) {
            downloadedPicIds = getImages(each, context);
            if (downloadedPicIds != null) {
                picNetworker.getLocalCopyOfImageIds().add(downloadedPicIds.getImageId());
            }
            picNetworker.getImagesToDownload().remove(each);
        }
        picNetworker.savePictureNetworker();
        picNetworker.notifyAllObservers();
    }

    private void pushImages(PictureNetworker picNetworker) {
        ArrayList<String> imgIds = new ArrayList<String>(picNetworker.getImageFilesToUpload());
        for (String each : imgIds) {
            System.out.println("Pic adding...");
            String json  = new String();
            try {
                json = PictureManager.loadImageJsonFromJsonFile(each, context);
                System.out.println("loaded image " + each);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            postImages(each, json);

            picNetworker.getImageFilesToUpload().remove(each);
            picNetworker.getLocalCopyOfImageIds().add(each);
        }
        picNetworker.savePictureNetworker();
        picNetworker.notifyAllObservers();
    }

    private void pushDeleteImages(PictureNetworker picNetworker) {
        ArrayList<String> tradesRemove = new ArrayList<String>(picNetworker.getImageFilesToRemove());
        for (String each : tradesRemove) {
            System.out.println("Pic removing...");
            String urlToRem = imagesLocation + each;
            deleteGeneric(urlToRem);
            // in case it was not uploaded remove it from ToUpload.
            picNetworker.getImageFilesToUpload().remove(each);
            picNetworker.getImageFilesToRemove().remove(each);
            picNetworker.getLocalCopyOfImageIds().remove(each);
            FileIO.removeFile(each, context);
        }
        picNetworker.savePictureNetworker();
        picNetworker.notifyAllObservers();
    }
    /**
     * Adds the ability to be able to add observers who observe the class.
     * @param observer The observer to be added which wants to be notified on the update.
     */
    public void addObserver(AppObserver observer){
        observers.add(observer);
    };

    /**
     * The ability to remove an observer from the watch list,
     * thus that observer will not be observing this class anymore.
     * @param observer This observer will be removed.
     */
    public void deleteObserver(AppObserver observer){
        observers.remove(observer);
    };

    /**
     * A method that should call appNotify on all observers who are watching this class.
     */
    public void notifyAllObservers(){
        for (AppObserver ob: observers){
            ob.appNotify(this);
        }
    };
}
