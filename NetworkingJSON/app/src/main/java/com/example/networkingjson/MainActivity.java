package com.example.networkingjson;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;

public class MainActivity extends AppCompatActivity {
    TextView txtView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtView = (TextView) findViewById(R.id.textView);
        new ReadJSONFeedTask().execute(
                "http://extjs.org.cn/extjs/examples/grid/survey.html");
    }//onCreate()
    // Open Connection
    private InputStream OpenHttpConnection(String urlString)
            throws IOException{
        java.io.InputStream in = null;
        int response = -1;

        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        if (!(conn instanceof HttpURLConnection)) throw new
                IOException("Not an HTTP connection");
        try{
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK){
                in = httpConn.getInputStream();
            }
        }
        catch (Exception ex){
            Log.d("Networking", ex.getLocalizedMessage());
            throw new IOException("Error connecting...");
        }
        return in;
    }//OpenHttpConnection()

    private class ReadJSONFeedTask extends AsyncTask<String, Void, String>{
        protected String doInBackground(String...urls){
            return readJSONFeed(urls[0]);
        }
        protected void onPostExecute(String result){
            try{
                JSONArray jsonArray = new JSONArray(result);
                Log.i("JSON", "Number of surveys in feed: "
                + jsonArray.length());
                String str = "";
                // Print Contents of JSON feed
                for (int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    Toast.makeText(
                            getBaseContext(), "appeId: " +
                            jsonObject.getString(
                                    "appeID") + " , input time: " +
                                    jsonObject.getString("inputTime"),
                                    Toast.LENGTH_SHORT).show();

                    str +=
                            "appeID: " + jsonObject.getString("appeId") +
                            " , inputTime: " + jsonObject.getString("inputTIme") +
                            "\n";
                }
                txtView.setText(str);
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
    }// ReadJSONFeedTask()

    public String readJSONFeed (String address){
        URL url = null;
        try{
            url = new URL(address);
        } catch (MalformedURLException e){
            e.printStackTrace();
        }
        StringBuilder stringBuilder = new StringBuilder();
        HttpURLConnection urlConnection = null;
        try{
             urlConnection = (HttpURLConnection) url.openConnection();

        } catch (IOException e){
            e.printStackTrace();
        }
        try {
            InputStream content = new
                    BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new
                    BufferedReader(new InputStreamReader(content));
            String line;
            while ((line = reader.readLine()) != null){
                stringBuilder.append(line);
            }
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return stringBuilder.toString();
    }//readJSONFeed
}// Class

