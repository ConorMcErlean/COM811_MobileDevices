package com.example.networking_socket_server;

import androidx.appcompat.app.AppCompatActivity;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    TextView info, infoip, msg;
    String message = "";
    ServerSocket serverSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        info = (TextView) findViewById(R.id.info);
        infoip= (TextView) findViewById(R.id.infoip);
        msg = (TextView) findViewById(R.id.msg);

        WifiManager wifiMgr = (WifiManager) getApplicationContext()
                .getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        String ipString = String.format("%d.%d.%d.%d.", (ip & 0xff),
                (ip >> 8 & 0xff), (ip>> 16 & 0xff), (ip >> 24 & 0xff));
        infoip.setText("My ip address: " + ipString);

        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();
    }//onCreate

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (serverSocket != null){
            try{
                serverSocket.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }//onDestroy

    private class SocketServerThread extends Thread  {
        static final int SocketServerPORT = 10000;

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(SocketServerPORT);
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        info.setText("I'm waiting here: " + serverSocket.getLocalPort());
                    }
                });
                while (true) {
                    Socket socket = serverSocket.accept();
                    message += " from " + socket.getInetAddress() + ":" +
                            socket.getPort() + "\n";
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            info.setText(message);
                        }
                    });
                    SocketServerReplyThread socketServerReplyThread = new
                            SocketServerReplyThread(socket);
                    socketServerReplyThread.run();
                }//while
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }// SocketServerThread

    private class SocketServerReplyThread extends Thread {
        private Socket hostThreadSocket;
        int cnt;

        SocketServerReplyThread(Socket socket){
            hostThreadSocket = socket;
        }
        @Override
        public void run(){
            OutputStream outputStream;
            String msgReply = "Hello from Server....";

            try{
                outputStream = hostThreadSocket.getOutputStream();
                PrintStream printStream = new PrintStream(outputStream);
                printStream.print(msgReply);
                printStream.close();

                message += "replied: " + msgReply + "\n";

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        msg.setText(message);
                    }
                });
            } catch (IOException e){
                e.printStackTrace();
                message += "Something wrong! " + e.toString() + "\n";
            }
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    msg.setText(message);
                }
            });
        }

    }// SocketServerReplyThread
}// Class
