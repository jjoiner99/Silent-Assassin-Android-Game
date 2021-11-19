package com.example.cs440project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cs440project.firebase.Fire;
import java.util.Objects;

public class RoleActivity extends AppCompatActivity {

    Button b1, b2;
    EditText ed1, ed2;
    TextView tx1;
    int counter = 3;
    private String username;
    private int role;
    private int EXPLORER = 0;
    private int ASSASSIN = 1;


    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide(); // hide the title bar
        setContentView(R.layout.role_screen);
        tx1 = findViewById(R.id.textView3);
        tx1.setVisibility(View.GONE);

    }

    public void handleExplorerBtn(View view) {

        b1 = findViewById(R.id.assasssinBtn);
        ed1 = findViewById(R.id.editText);

        b2 =  findViewById(R.id.explorerBtn);
        tx1.setVisibility(View.GONE);

        if(ed1.getText().toString().matches("")){
            Log.i("Username", ed1.getText().toString());
            tx1.setText("Please enter a name");
            tx1.setVisibility(View.VISIBLE);
        }
        else{
            username = ed1.getText().toString();
            role = EXPLORER;
            getPermission();
        }
    }

    public void handleAssassinBtn(View view) {

        b1 = findViewById(R.id.assasssinBtn);
        ed1 = findViewById(R.id.editText);

        b2 =  findViewById(R.id.explorerBtn);
        tx1.setVisibility(View.GONE);

        if(ed1.getText().toString().matches("")){
            tx1.setText("Please enter a name");
            tx1.setVisibility(View.VISIBLE);
        }
        else{
            role = ASSASSIN;
            username = ed1.getText().toString();
            getPermission();
        }
    }

    private void getPermission() {
        int LOCATION_REQUEST = 1;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
        } else {
            Intent startMapScreen = new Intent(RoleActivity.this, MapsActivity.class);
            startMapScreen.putExtra("role", role);
            startMapScreen.putExtra("username", username);
            RoleActivity.this.startActivity(startMapScreen);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            Intent startMapScreen = new Intent(RoleActivity.this, MapsActivity.class);
            startMapScreen.putExtra("role", role);
            startMapScreen.putExtra("username", username);
            RoleActivity.this.startActivity(startMapScreen);
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }


}

