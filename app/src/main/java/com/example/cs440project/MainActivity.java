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
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide(); // hide the title bar
        setContentView(R.layout.activity_main);

        Log.i("Database", "Initing database");
        initDatabase();
        Log.i("Database", "Should be done");
    }

    public void handleStartBtn(View view) {
        getPermission();
    }

    public void initDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Locations");


        HashMap<String, LatLngBounds> places = new HashMap<>();
        LatLngBounds SCEBounds = new LatLngBounds(
                new LatLng(41.871302, -87.648222),
                new LatLng(41.872526, -87.647667)
        );
        places.put("SCE", SCEBounds);

        LatLngBounds ARCBounds = new LatLngBounds(
                new LatLng(41.874521, -87.651644),
                new LatLng(41.875060, -87.650325)
        );
        places.put("ARC", ARCBounds);

        LatLngBounds SELEBounds = new LatLngBounds(
                new LatLng(41.870097, -87.648551),
                new LatLng(41.870788, -87.647376)
        );
        places.put("SELE", SELEBounds);

        LatLngBounds SELWBounds = new LatLngBounds(
                new LatLng(41.870083, -87.649383),
                new LatLng(41.870776, -87.648751)
        );
        places.put("SELW", SELWBounds);

        LatLngBounds QuadBounds = new LatLngBounds(
                new LatLng(41.871653, -87.649534),
                new LatLng(41.872110, -87.648955)
        );
        places.put("Quad", QuadBounds);

        LatLngBounds LibraryBounds = new LatLngBounds(
                new LatLng(41.871234, -87.650747),
                new LatLng(41.872538, -87.650227)
        );
        places.put("Library", LibraryBounds);

        LatLngBounds BSBBounds = new LatLngBounds(
                new LatLng(41.873148, -87.653264),
                new LatLng(41.874274, -87.651918)
        );
        places.put("BSB", BSBBounds);

        LatLngBounds CircleParkBounds = new LatLngBounds(
                new LatLng(41.869669, -87.650959),
                new LatLng(41.870640, -87.649682)
        );
        places.put("CirclePark", CircleParkBounds);
        myRef.setValue(places);
    }

    private void getPermission() {
        int LOCATION_REQUEST = 1;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
        } else {
            Intent startMapScreen = new Intent(MainActivity.this, MapsActivity.class);
            MainActivity.this.startActivity(startMapScreen);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            Intent startMapScreen = new Intent(MainActivity.this, MapsActivity.class);
            MainActivity.this.startActivity(startMapScreen);
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}