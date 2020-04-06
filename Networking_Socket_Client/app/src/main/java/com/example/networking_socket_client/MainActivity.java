package com.example.networking_socket_client;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {
    TextView textResponse;
    EditText editTextAddress, editTextPort;
    Button buttonConnect, buttonClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextAddress = findViewById(R.id.address);
        editTextPort = findViewById(R.id.port);
        buttonConnect = findViewById(R.id.connect);
        buttonClear = findViewById(R.id.clear);
        textResponse = findViewById(R.id.response);

        buttonConnect.setOnClickListener(buttonConnectOnClickListener);

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textResponse.setText("");
            }
        });
    }
    View.OnClickListener buttonConnectOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            MyClientTask myClientTask = new MyClientTask(
                    editTextAddress.getText().toString(),
                    Integer.parseInt(editTextPort.getText().toString())
            );
            myClientTask.execute();
        }// on click
    }; //buttonConnectOnClickListener

    public class MyClientTask extends AsyncTask<Void, Void, Void>{
       String dstAddress;
       int dstPort;
       String response = "";

       MyClientTask(String addr, int port){
           dstAddress = addr;
           dstPort = port;
       }
       @Override
        protected Void doInBackground(Void...arg0){
           Socket socket = null;
           try{
               socket = new Socket(dstAddress, dstPort);
               ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
               byte[] buffer = new byte[1024];
               int bytesRead;
               InputStream inputStream = socket.getInputStream();

               while((bytesRead = inputStream.read(buffer)) != -1){
                   byteArrayOutputStream.write(buffer, 0, bytesRead);
                   response += byteArrayOutputStream.toString("UTF-8");
               }
           } catch (UnknownHostException e){
               e.printStackTrace();
               response = "UnknownHostException: " + e.toString();
           } catch (IOException e) {
               e.printStackTrace();
               response = "IOException: " + e.toString();
           } finally {
               if (socket != null){
                   try{
                       socket.close();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
           }
           return null;
       }
       @Override
        protected void onPostExecute(Void result){
           textResponse.setText(response);
           super.onPostExecute(result);
       }
    }// MyClientTask
}
