package com.example.sqlitedatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    LoginDataBaseAdapter loginDataBaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create an instance of SQLite Database
        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();
    }

    // Method to handle Click Event of Sign in Button
    public void signIn(View v){
        try {
            String username = ((EditText) findViewById(R.id.editText_username))
                    .getText().toString();
            String password = ((EditText) findViewById(R.id.editText_password))
                    .getText().toString();

            // Fetch the password from database for respective username
            String storedPassword = loginDataBaseAdapter.getSingleEntry(username);

            if (password.equals(storedPassword)){
                Toast.makeText(MainActivity.this, "Successful Login",
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this,
                        LoginSuccessActivity.class);
                intent.putExtra("Name", username);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this,
                        "The given records are not available. Please sign up",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex){
            Log.e("Error", "error login");
        }
    }
    public void goToSignUp(View v){
        Intent intent = new Intent(MainActivity.this, signUp.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        // Close the database
        loginDataBaseAdapter.close();
    }
}