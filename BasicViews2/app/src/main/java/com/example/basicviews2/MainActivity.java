package com.example.basicviews2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
    static int progress;
    ProgressBar progressBar;
    int progressStatus = 0;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        progress = 0;
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setMax(200);
        //---do some work in background thread---
        new Thread(new Runnable() {
            public void run() {
                //---do some work here---
                while (progressStatus < 100) {
                    progressStatus = doSomeWork();
                }//---Updates the progress bar---
                handler.post(new Runnable() {
                    public void run() {
                        //---0 -VISIBLE; 4 -INVISIBLE; 8 -GONE---
                        progressBar.setProgress(progressStatus);
                    }
                });

                //---hides the progress bar---
                handler.post(new Runnable() {
                    public void run() {
                        //---0 -VISIBLE; 4 -INVISIBLE; 8 -GONE---
                        progressBar.setVisibility(View.GONE);
                    }
                });

            } // run

            //---do some long running work here---
            private int doSomeWork() {
                try {
                    //---simulate doing some work---
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return ++progress;
            }// doSomeWork

        }).start();// Runable

    }// Override
}// Main Activity
