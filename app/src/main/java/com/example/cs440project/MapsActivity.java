package com.example.cs440project;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.cs440project.databinding.ActivityMapsBinding;
import com.example.cs440project.interestPoints.InterestPoints;
import com.example.cs440project.locationCheck.locationCheck;
import com.example.cs440project.mapPreference.MapPreference;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener {

    private FusedLocationProviderClient FSL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.cs440project.databinding.ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        FSL = LocationServices.getFusedLocationProviderClient(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        MapPreference.setMapStyle(googleMap, MapsActivity.this); // Set blue style to mMap
        InterestPoints.drawBuildingPolygons(googleMap); // Draws all of the buildings
        InterestPoints.drawUicBounds(googleMap); // Draws stroke around uic block

        // When map finished loading, prevents the error
        googleMap.setOnMapLoadedCallback(MapPreference.setCamera(googleMap));
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationButtonClickListener(this);
        googleMap.setOnMyLocationClickListener(this);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        isUserInPOI();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Latitude: " + location.getLatitude() + " Longitude: " + location.getLongitude(), Toast.LENGTH_LONG).show();
    }
    
    @SuppressLint("MissingPermission")
    public void isUserInPOI() {
        String TAG = "Maps Activity";
        Log.i(TAG, "Clicked ");
        FSL.getLastLocation().addOnSuccessListener(this, (OnSuccessListener<Location>) location -> {
            if (location != null) {
                locationCheck.check(this, location.getLatitude(), location.getLongitude());
            } else {
                Log.i("location", "Location returned null");
            }
        });
    }

}