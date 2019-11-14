package com.example.androidlabs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.provider.MediaStore;
import android.graphics.Bitmap;


import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";
    ImageButton mImageButton;
    Button goToChatButton;
    Button weatherBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        Log.e(ACTIVITY_NAME, "In function:"+"onCreate");
//getting data from previous page
        Intent previousData = getIntent();
        String email = previousData.getStringExtra("email");
        TextView profileEmail = findViewById(R.id.typeEmail1);
        profileEmail.setText(email);


        mImageButton = findViewById(R.id.photoButton);
        mImageButton.setOnClickListener(clk -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }});

        goToChatButton = findViewById(R.id.goToChat);
        goToChatButton.setOnClickListener(clk -> {
            Intent chatPage = new Intent(ProfileActivity.this, ChatRoomActivity.class);
            startActivity(chatPage);
        });

        weatherBtn = findViewById(R.id.weatherBtn);
        weatherBtn.setOnClickListener(clk->{
                Intent weatherAct = new Intent(ProfileActivity.this, WeatherForecast.class);
                startActivity(weatherAct);
                });

    }





    @Override
    protected void onStart()
    {
        super.onStart();
        Log.e(ACTIVITY_NAME, "In function:"+"onStart");


    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.e(ACTIVITY_NAME, "In function:"+"onResume");


    }

    @Override
    protected void onPause() {

        super.onPause();
        Log.e(ACTIVITY_NAME, "In function:"+"onPause");

    }

    @Override
    protected void onStop() {

        super.onStop();
        Log.e(ACTIVITY_NAME, "In function:"+"onStop");

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        Log.e(ACTIVITY_NAME, "In function:"+"onDestroy");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(ACTIVITY_NAME, "In function:"+"onActivityResut");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageButton.setImageBitmap(imageBitmap);
        }
    }



}
