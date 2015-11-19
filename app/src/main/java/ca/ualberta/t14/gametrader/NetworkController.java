package ca.ualberta.t14.gametrader;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by jjohnsto on 11/19/15.
 */
public class NetworkController {
    // Http Connector
    private HttpClient httpclient = new DefaultHttpClient();

    // JSON Utilities
    private Gson gson = new Gson();
    // https://github.com/rayzhangcl/ESDemo/blob/master/ESDemo/src/ca/ualberta/cs/CMPUT301/chenlei/ESClient.java used as reference/source
    public void InsertGame(Game game) {
        // TODO: parse text so the title corresonds to a unique ID
        HttpPost httpPost = new HttpPost("http://cmput301.softwareprocess.es:8080/t14/games"+game.getTitle());
        StringEntity stringentity = null;

        try {
            stringentity = new StringEntity(gson.toJson(game));
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
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
            String output;
            System.err.println("Output from Server -> ");
            while ((output = br.readLine()) != null) {
                System.err.println(output);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // bunch of stuff for releasing connection deleted because some of the functions
        // don't seem to exist in our http libraries
    }
}
