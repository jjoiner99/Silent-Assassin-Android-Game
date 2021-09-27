package com.example.cs440project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_main);
    }

    public void handleStartBtn(View view) {
        Intent startMapScreen = new Intent(MainActivity.this, MapsActivity.class);
        MainActivity.this.startActivity(startMapScreen);
    }
}