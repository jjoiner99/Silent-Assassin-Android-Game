package com.example.cs440project;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.renderscript.Sampler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.cs440project.databinding.ActivityMapsBinding;
import com.example.cs440project.firebase.Fire;
import com.example.cs440project.interestPoints.InterestPoints;
import com.example.cs440project.locationCheck.locationCheck;
import com.example.cs440project.mapPreference.MapPreference;
import com.example.cs440project.user.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener {

    private FusedLocationProviderClient FSL;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Location currentLocation;
    private double lat;
    private double lon;
    private User user = new User();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.cs440project.databinding.ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        createLocationRequest();
        createLocationCallback();
        FSL = LocationServices.getFusedLocationProviderClient(this);
        Log.i("ID", user.getID());
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        MapPreference.setMapStyle(googleMap, MapsActivity.this); // Set blue style to mMap
        InterestPoints.drawBuildingPolygons(googleMap); // Draws all of the buildings
        InterestPoints.drawUicBounds(googleMap); // Draws stroke around uic block
        Fire.fetchMultiPlayLocation(googleMap);
        // When map finished loading, prevents the error
        googleMap.setOnMapLoadedCallback(MapPreference.setCamera(googleMap));
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationButtonClickListener(this);
        googleMap.setOnMyLocationClickListener(this);
        startLocationUpdates();
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
        FSL.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                locationCheck.check(this, location.getLatitude(), location.getLongitude());
            } else {
                Log.i("location", "Location returned null");
            }
        });
    }

    private void createLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                currentLocation = locationResult.getLastLocation();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference totalRef = database.getReference("CompleteUserData");
                DatabaseReference singleRef = database.getInstance().getReference();
                lat = currentLocation.getLatitude();
                lon = currentLocation.getLongitude();
                user.setLat(lat);
                user.setLon(lon);
                singleRef.child("Users").child(user.getUsername()).setValue(user);
                grabData();
            }
        };
    }

    private void createLocationRequest() {
        locationRequest = LocationRequest.create()
                .setInterval(100)
                .setFastestInterval(3000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(100);
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        FSL.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }

    private void grabData() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("CoordUserData");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Double lat = ds.child("Latitude").getValue(Double.class);
                    Double lon = ds.child("Longitude").getValue(Double.class);
                    Log.i("DataRet", "Latitude: " + lat + " Longitude: " + lon);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Retrieve", "Read failed");
            }
        });
    }

}