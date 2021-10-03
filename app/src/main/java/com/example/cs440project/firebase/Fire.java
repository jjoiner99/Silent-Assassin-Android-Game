package com.example.cs440project.firebase;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Fire {
    //Logcat TAG
    private static final String TAG = "firebaseService";
    // Reference
    private DatabaseReference myRef;

    // Init database should only be called if we decide to change the long and lat of an area but not onCreate
    public static void initDatabase() {
        Log.i(TAG, "Should be done");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Locations");
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "Successfully initialized database");
    }

    // TODO - send a users coordinates to firebase on an interval
    public static void sendUserLocation() {
    }

    // TODO - check if a users location is inside an interest point boundary
    public static boolean isUserInPOI() {
        return false;
    }


    // TODO - fetch other players coodinates
    public static void fetchMultiPlayLocation() {
    }
}
