package com.example.cs440project;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

import org.w3c.dom.Text;

import java.util.HashMap;

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener {

    private TextView score;
    private FusedLocationProviderClient FSL;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Location currentLocation;
    private double lat;
    private double lon;
    private User user = new User();
    private Button customButton;
    int loc;
    String DailyBounty;
    boolean visible = false;
    boolean dailyRedeemed = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getDailyBounty();
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
        score = findViewById(R.id.scoreText);
        updateScore();
        customButton = findViewById(R.id.customButton);
        customButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Points", "Added 10 Points");
                Toast.makeText(MapsActivity.this, "You just got +10 points!", Toast.LENGTH_SHORT).show();
                user.addPoints(10);
                dailyRedeemed = true;
                customButton.setVisibility(View.INVISIBLE);
                updateScore();
            }
        });
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
        String curLoc = locationCheck.checkLocation(lat, lon);
        Log.i("current", curLoc);
        if (curLoc == DailyBounty && dailyRedeemed == false) {
            Log.i("Button", "Turning visible");
            customButton.setVisibility(View.VISIBLE);
            visible = true;
        } else if (curLoc != DailyBounty && visible) {
            Log.i("Button", "Turning invisible");
            customButton.setVisibility(View.INVISIBLE);
            visible = false;
        }
    }

    public void getDailyBounty() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("DailyQuestPOI");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loc = dataSnapshot.child("interestPointId").getValue(Integer.class);
                getDailyLocation(loc);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("DailyQuest", "Couldn't get Quest");
            }
        });
    }

    public void getDailyLocation(int locationID) {
        Log.i("DailyLocation", "" + locationID);
        switch (locationID){
            case 1:
                DailyBounty = "ARC";
                break;
            case 2:
                DailyBounty = "BSB";
                break;
            case 3:
                DailyBounty = "CirclePark";
                break;
            case 4:
                DailyBounty = "Library";
                break;
            case 5:
                DailyBounty = "Quad";
                break;
            case 6:
                DailyBounty = "SCE";
                break;
            case 7:
                DailyBounty = "SELE";
                break;
            case 8:
                DailyBounty = "SELW";
                break;
            default:
                DailyBounty = "";
        }
    }

    // TODO Get place where the bounty assigned is in
    public String getBounty(int bounty) {
        return "";
    }

    public void updateScore() {
        score.setText(String.valueOf(user.getPoints()));
    }

}