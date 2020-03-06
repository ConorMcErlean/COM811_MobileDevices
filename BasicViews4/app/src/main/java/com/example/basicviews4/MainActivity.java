package com.example.basicviews4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class MainActivity extends AppCompatActivity {
    String[] presidents = {"Dwight D. Eisenhower","John F. Kennedy",
            "Lyndon B. Johnson","Richard Nixon",
            "Gerald Ford","Jimmy Carter","Ronald Reagan",
            "George H. W. Bush","Bill Clinton","George W. Bush",
            "Barack Obama","Donald Trump"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, presidents);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.txtCountries);
        textView.setThreshold(3);
        textView.setAdapter(adapter);

    }
}
