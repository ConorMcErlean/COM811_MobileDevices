package com.example.switchactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Button secondButton = (Button)findViewById(R.id.button);
        secondButton.setOnClickListener(new View.OnClickListener(){
            public void onClick (View view){
                finish();
            }
        });
    }
}
