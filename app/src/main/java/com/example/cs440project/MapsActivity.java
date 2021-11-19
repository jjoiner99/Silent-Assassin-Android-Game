package com.example.cs440project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.cs440project.databinding.ActivityMapsBinding;
import com.example.cs440project.firebase.Fire;
import com.example.cs440project.interestPoints.InterestPoints;
import com.example.cs440project.leaderboardEntry.LeaderboardEntry;
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
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener {

    String TAG = "Maps Activity";
    private TextView score;
    private TextView currentLocationText;
    private FusedLocationProviderClient FSL;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Location currentLocation;
    private double lat;
    private double lon;
    private User user = new User();
    ArrayList<String> playersInSameLoc;
    private Button customButton; //todo rename customButton
    private Button killExplorerButton;
    int loc;
    String DailyBounty;
    boolean visible = false;
    boolean dailyRedeemed = false;
    private TextView ppTV; // Popup TextView
    private String playerKilled;
    private Button leaderboardButton;
    private ArrayList<LeaderboardEntry> leaderboard;

    ArrayList<Integer> userQuestId = new ArrayList<Integer>();
    ArrayList<String> userQuestKey = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Gets the username and role from previous intent
        Intent i = getIntent();
        user.setUsername(i.getStringExtra("username"));
        user.setRole(i.getIntExtra("role", 0));

        // Retrieve the latest bounty
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
        score = findViewById(R.id.scoreText);
        customButton = findViewById(R.id.customButton);
        currentLocationText = findViewById(R.id.currentLocationText);
        currentLocationText.setVisibility(View.INVISIBLE);
        customButton.setOnClickListener(v -> {
            String curLoc = locationCheck.checkLocation(lat, lon);

            // If we are in a user quest
            if(userQuestKey.contains(curLoc)){
                // Remove daily quest from the list
                userQuestKey.remove(curLoc);
                Toast.makeText(MapsActivity.this, "You just got +10 points!", Toast.LENGTH_SHORT).show();
                user.addPoints(10);
                updateScore();
            } else{
                Log.i("Points", "Added 40 Points");
                Toast.makeText(MapsActivity.this, "You just got +40 points!", Toast.LENGTH_SHORT).show();
                user.addPoints(40);
                dailyRedeemed = true;
                updateScore();
            }
        });

        // Setup kill Explorer Btn
        killExplorerButton = findViewById(R.id.killExplorerButton);
        killExplorerButton.setVisibility(View.INVISIBLE);
        killExplorerButton.setOnClickListener(v -> {
            //pick a random player in the list
            playerKilled = playersInSameLoc.get((int)(Math.random() * playersInSameLoc.size()));

            Log.i(TAG, "Player To Die!!: "+ playerKilled);
            Fire.killPlayer(playerKilled);
            showQuests(findViewById(R.id.map), true);
            user.addPoints(50);
            updateScore();
            killExplorerButton.setVisibility(v.INVISIBLE);
        });
        if(user.getRole() == 1) {
            // Hide view players
            findViewById(R.id.toggleButton).setVisibility(View.INVISIBLE);

        }
        // Button for leaderboards
        leaderboardButton = findViewById(R.id.leaderboard_button);
        leaderboardButton.setOnClickListener(v -> {
            showLeaderboard(leaderboard, findViewById(R.id.map));
        });

    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        MapPreference.setMapStyle(googleMap, MapsActivity.this); // Set blue style to mMap
        InterestPoints.drawBuildingPolygons(googleMap); // Draws all of the buildings
        InterestPoints.drawUicBounds(googleMap); // Draws stroke around uic block
        Fire.fetchMultiPlayLocation(googleMap, user.getRole(), MapsActivity.this);
        // When map finished loading, prevents the error
        googleMap.setOnMapLoadedCallback(MapPreference.setCamera(googleMap));
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationButtonClickListener(this);
        googleMap.setOnMyLocationClickListener(this);
        startLocationUpdates();

        getLeaderboard();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        handleGetLocationButton();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Latitude: " + location.getLatitude() + " Longitude: " + location.getLongitude(), Toast.LENGTH_LONG).show();
    }


    public void onPlayersToggleClick(View v){
        ArrayList<Marker> mark = Fire.getMarkers();

        if(((ToggleButton) v).isChecked()){
            for(Marker m : mark){
                m.setVisible(false);
            }
        }
        else{
            for(Marker m : mark){
                m.setVisible(true);
            }
        }
    }


    @SuppressLint("MissingPermission")
    public void handleGetLocationButton() {
        FSL.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                locationCheck.check(this, location.getLatitude(), location.getLongitude());
            } else {
                Log.i(TAG, "Location returned null");
            }
        });
    }

    public boolean isInPOI(){

        String checkLocationResult = locationCheck.check(this, lat,lon);
        int role = user.getRole();

        // User is in a POI
        if(checkLocationResult != null){
            currentLocationText.setText("Welcome to "+checkLocationResult);
            currentLocationText.setVisibility(View.VISIBLE);

            // list of other players in same location
            playersInSameLoc = locationCheck.checkForOthers(checkLocationResult, user.getUsername());

            // user is a hunter
            if(role == 1 && playersInSameLoc.size() > 0){
                killExplorerButton.setVisibility(View.VISIBLE);
            } else {
                killExplorerButton.setVisibility(View.INVISIBLE);
            }
            return true;
        }
        // Reset TextView
        currentLocationText.setText("");
        currentLocationText.setVisibility(View.INVISIBLE);
        killExplorerButton.setVisibility(View.INVISIBLE);

        // User is not in a POI
        return false;
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
                isInPOI();
                getLeaderboard();
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

    // Setting "collect bounty btn" to visible/invisible
    private void grabData() {
        String curLoc = locationCheck.checkLocation(lat, lon);
        Log.i("current", curLoc);
        if (curLoc == DailyBounty && dailyRedeemed == false || userQuestKey.contains(curLoc)) {
            Log.i("Button", "Turning visible");

            // If the user is a explorer display the collect bounty button
            if(user.getRole() == 0){
                customButton.setVisibility(View.VISIBLE);
            }

            visible = true;
        } else if (curLoc != DailyBounty && visible) {
            Log.i("Button", "Turning invisible");
            customButton.setVisibility(View.INVISIBLE);
            visible = false;
        }
    }

    // populate userQuestsKeys list with non repeating non dailybounty quests
    public void generateRandomQuests(int numQuests){
        Random randomGenerator = new Random();
        while (userQuestId.size() < numQuests) {

            // Random Num between 1 - 8
            int randomId = randomGenerator .nextInt(8);
            String questKey = getDailyLocation(randomId+1);

            // No duplicates!
            if (!userQuestId.contains(randomId)) {
                // Can't be the daily!
                if (DailyBounty == questKey){
                    Log.i("DailyQuest", "Couldn't add daily Quest");
                } else {
                    // Add to list
                    Log.i("DailyQuest", "Adding " + questKey);
                    userQuestId.add(randomId);
                    userQuestKey.add(questKey);
                }
            }
        }
    }

    // Set Daily Bounty to fetched firebase data
    public void getDailyBounty() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("DailyQuestPOI");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Loc = the id of daily location
                loc = dataSnapshot.child("interestPointId").getValue(Integer.class);
                DailyBounty = getDailyLocation(loc);
                if(user.getRole() == 0){
                    showQuests(findViewById(R.id.map), false);
                } else {
                    showQuests(findViewById(R.id.map), false);
                }
                Log.i("DailyQuest", "Daily Bounty = " + DailyBounty);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("DailyQuest", "Couldn't get Quest");
            }
        });
    }

    public String getDailyLocation(int locationID) {
        switch (locationID){
            case 1:
                return "ARC";
            case 2:
                return "BSB";
            case 3:
                return "CirclePark";
            case 4:
                return "Library";
            case 5:
                return "Quad";
            case 6:
                return "SCE";
            case 7:
                return "SELE";
            case 8:
                return "SELW";
            default:
                return "";
        }
    }

    public void updateScore() {
        score.setText(String.valueOf(user.getPoints()));
    }

    // Function to show the list of quests an explorer can go to
    public void showQuests(View view, boolean hunterKill) {
        // Inflate the popup window layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popView = inflater.inflate(R.layout.pop_up, null);

        // Create pop up window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow popup = new PopupWindow(popView, width, height, true);
        popup.setElevation(20);

        StringBuilder builder = new StringBuilder();

        // Display a hunter kill dialog box
        if(hunterKill == true){
            builder.append(playerKilled + " was killed");
        }

        // Display Quest dialog box
        else {
            generateRandomQuests(4);
            if (user.getRole() == 0) {
                builder.append("Quests Available:\n");
            } else {
                builder.append("Explorers have bounties in:\n");
            }
            for (int i = 0; i < userQuestKey.size(); i++) {
                if (i != userQuestKey.size() - 1) {
                    builder.append(userQuestKey.get(i) + "\n");
                } else {
                    builder.append(userQuestKey.get(i));
                }
            }
            builder.append("\n\nDaily bounty: \n");
            builder.append(DailyBounty);
        }

        // Show popup
        popup.showAtLocation(view, Gravity.CENTER, 0, 0);

        ppTV = popup.getContentView().findViewById(R.id.popupTextView);
        ppTV.setText(builder.toString());

        // Dismiss the popup when touched
        popView.setOnTouchListener((view1, motionEvent) -> {
            popup.dismiss();
            return true;
        });
    }

    // Show a leaderboard of the players scores at the current time
    public void getLeaderboard() {
        leaderboard = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getInstance().getReference("Users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String username = ds.child("username").getValue(String.class);
                    int score = ds.child("points").getValue(Integer.class);
                    int role = ds.child("role").getValue(Integer.class);
                    LeaderboardEntry temp = new LeaderboardEntry(username, score, role);
                    leaderboard.add(temp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Leaderboard", "Couldn't get players");
            }
        });

    }

    // Show popup of leaderboard
    @SuppressLint("ClickableViewAccessibility")
    public void showLeaderboard(ArrayList<LeaderboardEntry> list, View view) {
        list.sort((Comparator.comparing(LeaderboardEntry::getScore).reversed()));
        // Inflate the popup window layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popView = inflater.inflate(R.layout.pop_up, null);

        // Create pop up window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow popup = new PopupWindow(popView, width, height, true);
        popup.setElevation(20);

        popup.showAtLocation(view, Gravity.CENTER, 0, 0);

        StringBuilder builder = new StringBuilder();
        builder.append("Leaderboards: \n");
        for (int i = 0; i < list.size(); i++) {
            LeaderboardEntry temp = list.get(i);
            if (temp.getRole() == 0) {
                builder.append(temp.getUsername() + " - Explorer: " + temp.getScore() + "\n");
            } else {
                builder.append(temp.getUsername() + " - Assassin: " + temp.getScore() + "\n");
            }
        }

        ppTV = popup.getContentView().findViewById(R.id.popupTextView);
        ppTV.setText(builder.toString());

        popView.setOnTouchListener((view1, motionEvent) -> {
            popup.dismiss();
            return true;
        });
    }

}