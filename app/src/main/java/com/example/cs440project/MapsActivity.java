package com.example.cs440project;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.cs440project.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener {

    private GoogleMap mMap;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.cs440project.databinding.ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Locations");
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;
        LatLng UIC = new LatLng(41.871899, -87.649252);
        LatLngBounds UICBounds = new LatLngBounds(
                new LatLng(41.869573, -87.651078),
                new LatLng(41.874249, -87.647262)
        );
        HashMap<String, LatLng> places = new HashMap<String, LatLng>();
        places.put("UIC", UIC);
        myRef.setValue(places);
        // When map finished loading, prevents the error
        mMap.setOnMapLoadedCallback(() -> {
            mMap.addMarker(new MarkerOptions().position(UIC).title("University of Illinois at Chicago"));
            mMap.setLatLngBoundsForCameraTarget(UICBounds);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(UIC));
            mMap.setMinZoomPreference(16.0f);
            mMap.setMaxZoomPreference(17.0f);
        });
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "Current location: ", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location: " + location, Toast.LENGTH_LONG).show();
    }
}