package com.example.networkingtext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.PrivateKey;

public class MainActivity extends AppCompatActivity {

    final private int REQUEST_INTERNET =123; // ID for the request
    TextView txtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Check Permission of INTERNET
        txtView=(TextView) findViewById(R.id.textView);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.INTERNET}, REQUEST_INTERNET);
        }//if
        else{
            new DownloadTextTask().execute("http://jfdimarzio.com/test.htm");
        }
    }

    // Open Connection To Internet
    private InputStream OpenHttpConnection(String urlString) throws IOException{
        InputStream in = null;
        int response = -1;

        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        if(!(conn instanceof HttpURLConnection)) throw new IOException(
                "Not a HTTP connection");
        try{
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            response = httpConn.getResponseCode();
            if(response == HttpURLConnection.HTTP_OK){
                in =httpConn.getInputStream();
            }
        }
        catch (Exception ex){
            Log.d("Networking", ex.getLocalizedMessage());
            throw new IOException("Error connecting...");
        }
        return in;
    }

    public void onRequestPermissionResult(int requestCode, String[] permissions,
                                          int[] grantResults){
        switch (requestCode){
            case REQUEST_INTERNET:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    new DownloadTextTask().execute("http://jfdimarzio.com/test.htm");
                }
                else{
                    Toast.makeText(MainActivity.this, "Permission Denied",
                            Toast.LENGTH_LONG).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }


    }// onRequestPermissionResult

    // Allows download task to run on separate thread
    private class DownloadTextTask extends AsyncTask<String, Void, String>{
        protected String doInBackground(String...urls){
            return DownloadText(urls[0]);
        }
        protected void onPostExecute(String result){
            Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
            txtView.setText(result);
        }
    }

    private String DownloadText (String URL){
        int BUFFER_SIZE = 2000;
        InputStream in = null;
        try{
            in = OpenHttpConnection(URL);
        }
        catch (IOException e1){
            Log.d("Networking", e1.getLocalizedMessage());
        }

        InputStreamReader isr = new InputStreamReader(in);
        int charRead;
        String str = "";
        char[] inputBuffer = new char[BUFFER_SIZE];

        try{
            while ((charRead = isr.read(inputBuffer)) > 0){
                // Converting Characters to a string
                String readString = String.copyValueOf(inputBuffer, 0, charRead);
                str += readString;
                inputBuffer = new char[BUFFER_SIZE];
            }
            in.close();
        }
        catch (IOException e){
            Log.d("Networking", e.getLocalizedMessage());
            return "";
        }
        return str;
    }
}
