package com.example.cs440project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.cs440project.Quests.Quest;
import com.example.cs440project.firebase.Fire;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide(); // hide the title bar
        setContentView(R.layout.activity_main);
    }

    public void handleStartBtn(View view) {
        getRoleScreen();
    }

    private void getRoleScreen(){

        Intent startRoleScreen = new Intent(MainActivity.this, com.example.cs440project.RoleActivity.class);
        MainActivity.this.startActivity(startRoleScreen);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}