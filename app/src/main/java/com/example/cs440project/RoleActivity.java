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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cs440project.firebase.Fire;
import java.util.Objects;

public class RoleActivity extends AppCompatActivity {

    Button b1, b2;
    EditText ed1,ed2;
    TextView tx1;
    int counter = 3;


    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide(); // hide the title bar
        setContentView(R.layout.role_screen);
        tx1 = (TextView)findViewById(R.id.textView3);
        tx1.setVisibility(View.GONE);

    }

    public void handleExplorerBtn(View view) {

        b1 = (Button)findViewById(R.id.assasssinBtn);
        ed1 = (EditText)findViewById(R.id.editText);
        ed2 = (EditText)findViewById(R.id.editText2);

        b2 =  (Button)findViewById(R.id.explorerBtn);
        tx1 = (TextView)findViewById(R.id.textView3);
        tx1.setVisibility(View.GONE);

        if(ed1.getText().toString().equals("admin") && ed2.getText().toString().equals("admin")){
            getPermission();
        }
        else{
            tx1.setVisibility(View.VISIBLE);
            counter--;
            tx1.setText(Integer.toString(counter));
            if (counter == 0) {
                b1.setEnabled(false);
                b2.setEnabled(false);
            }
        }
    }

    public void handleAssassinBtn(View view) {

        b1 = (Button)findViewById(R.id.assasssinBtn);
        ed1 = (EditText)findViewById(R.id.editText);
        ed2 = (EditText)findViewById(R.id.editText2);

        b2 =  (Button)findViewById(R.id.explorerBtn);
        tx1 = (TextView)findViewById(R.id.textView3);
        tx1.setVisibility(View.GONE);

        if(ed1.getText().toString().equals("admin") && ed2.getText().toString().equals("admin")){
            getPermission();
        }
        else{
            tx1.setVisibility(View.VISIBLE);
            counter--;
            tx1.setText(Integer.toString(counter));
            if (counter == 0) {
                b1.setEnabled(false);
                b2.setEnabled(false);
            }
        }
    }

    private void getPermission() {
        int LOCATION_REQUEST = 1;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
        } else {
            Intent startMapScreen = new Intent(RoleActivity.this, MapsActivity.class);
            RoleActivity.this.startActivity(startMapScreen);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            Intent startMapScreen = new Intent(RoleActivity.this, MapsActivity.class);
            RoleActivity.this.startActivity(startMapScreen);
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }


}

