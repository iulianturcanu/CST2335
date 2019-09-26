package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//declaring used variables
        Button button1 = findViewById(R.id.buttonLogin);
        EditText et = findViewById(R.id.typeEmail);
        SharedPreferences sp = getSharedPreferences("File", Context.MODE_PRIVATE);
//posting from shared preferences
        String toPost = sp.getString("email", "no email available");
        et.setText(toPost);
//activity to go to next page
        if(button1!=null){
            button1.setOnClickListener(clk-> {
                Intent profilePage = new Intent(MainActivity.this, ProfileActivity.class);
                profilePage.putExtra("email", et.getText().toString());
                startActivity(profilePage);
            });
        }



    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
//saving to shared preferences
    EditText et = findViewById(R.id.typeEmail);
    SharedPreferences sp = getSharedPreferences("File", Context.MODE_PRIVATE);
    SharedPreferences.Editor spedit = sp.edit();
    spedit.putString("email", et.getText().toString());
    spedit.commit();


    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

