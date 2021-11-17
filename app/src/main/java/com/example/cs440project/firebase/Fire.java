

package com.example.cs440project.firebase;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Fire {
    //Logcat TAG
    private static final String TAG = "firebaseService";
    private static final HashMap<String, LatLngBounds> places = new HashMap<>();
    private static final HashMap<String, LatLng> multiPlayerCoord = new HashMap<>();
    private static final ArrayList<Marker> markers = new ArrayList<>();

    // Init database should only be called if we decide to change the long and lat of an area but not onCreate
    public static void initDatabase() {
        Log.i(TAG, "Should be done");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Locations");
        try {
            // TODO All interest points should be a polygon and have > 4 vertices
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "Successfully initialized database");
    }

    public static void initLogsTable(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference logsRef = database.getReference("Logs").child("Config");
        HashMap<String, String> log = new HashMap<>();
        log.put("ConnectionString","Bologna");
        logsRef.setValue(log);
    }

    // Write a log to the Logs table on firebase. Use this for sending errors.
    public static void logToDatabase(String logType, String logDescription){
        // Reference to database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Create a unique id string
        String uuid = UUID.randomUUID().toString();

        // Instantiate a new log object
        FireLog currentLog = new FireLog(uuid,logType, logDescription);

        // Create a new child in the db using the uuid
        // Create a new child in the db using the uuid
        DatabaseReference logsRef = database.getReference("Logs").child(currentLog.getLogType()+"-"+uuid);

        // Write to the db
        logsRef.setValue(currentLog);
    }

    public static HashMap<String, LatLng> getMultiPlayerCoord() {
        return multiPlayerCoord;
    }

    public static ArrayList<Marker> getMarkers() {return markers; }


    // TODO - fetch other players coordinates
    public static void fetchMultiPlayLocation(GoogleMap mMap) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Double lon = ds.child("lon").getValue(Double.class);
                    Double lat = ds.child("lat").getValue(Double.class);
                    String username = ds.child("username").getValue(String.class);
                    Log.i("Players", "Lat: " + lat + " Lon: " + lon);
                    LatLng coord = new LatLng(lat, lon);
                    multiPlayerCoord.put(username, coord);
                }

                for (Map.Entry<String, LatLng> entry : multiPlayerCoord.entrySet()) {
                    // Print
                    Log.d(TAG, entry.getKey() + " : " + entry.getValue());
                    markers.add(mMap.addMarker(new MarkerOptions().position(entry.getValue()).title(entry.getKey())));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        myRef.addValueEventListener(postListener);
    }
}