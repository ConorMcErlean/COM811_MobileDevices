package com.example.basicviews3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ViewAnimator;

import java.sql.Time;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    TimePickerDialog.OnTimeSetListener dialogListener = new TimePickerDialog.OnTimeSetListener(){
        @Override
        public void onTimeSet (TimePicker timePicker, int i, int i1){

        }
    };
    Calendar cal = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showTimeDialog();
    }// OverRide

    public void showTimeDialog(){
        new TimePickerDialog(MainActivity.this, dialogListener,
                cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE), false).show();
    }// showTimeDialogue
}
