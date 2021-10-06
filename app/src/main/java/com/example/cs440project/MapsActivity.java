package com.example.cs440project;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.cs440project.interestPoints.InterestPoints;
import com.example.cs440project.databinding.ActivityMapsBinding;
import com.example.cs440project.mapPreference.MapPreference;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener {

    private GoogleMap mMap;
    private FusedLocationProviderClient FSL;
    private HashMap<String, LatLngBounds> places;

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
        places = new HashMap<>();
        initMap();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;

        MapPreference.setMapStyle(mMap, MapsActivity.this); // Set blue style to mMap
        InterestPoints.drawBuildingPolygons(mMap); // Draws all of the buildings
        InterestPoints.drawUicBounds(mMap); // Draws stroke around uic block

        // When map finished loading, prevents the error
        mMap.setOnMapLoadedCallback(MapPreference.setCamera(mMap));
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
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
        AtomicBoolean isInPlace = new AtomicBoolean(false);
        FSL.getLastLocation().addOnSuccessListener(this, (OnSuccessListener<Location>) location -> {
            if (location != null) {
                LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                for (Map.Entry place : places.entrySet()) {
                    String POI = (String)place.getKey();
                    LatLngBounds b = (LatLngBounds) place.getValue();
                    if (b.contains(current)) {
                        Toast.makeText(MapsActivity.this, "You are in " + POI, Toast.LENGTH_SHORT).show();
                        isInPlace.set(true);
                        break;
                    }
                }
                if (!isInPlace.get()) {
                    Toast.makeText(MapsActivity.this, "You are not in a POI", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.i("location", "Location returned null");
            }
        });
    }

    protected void initMap() {
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
    }
}