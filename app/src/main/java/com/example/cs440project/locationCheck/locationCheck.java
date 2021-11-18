package com.example.cs440project.locationCheck;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.example.cs440project.firebase.Fire;
import com.example.cs440project.user.User;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class locationCheck {
    private static HashMap<String, LatLngBounds> places = new HashMap<>();
    private static String TAG = "LocationCheck";
    // Initializes the Hashmap of Places of Interests for location checking
    public static void initMap() {

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

    public static HashMap<String, LatLngBounds> getMap() {
        return places;
    }

    // Check the current user location if they are in a POI
    public static String check(Activity context, double lat, double lon) {
        // Populate Hashmap of bounds
        initMap();

        // Get current location
        LatLng current = new LatLng(lat, lon);
        for (Map.Entry place : getMap().entrySet()) {
            String POI =(String) place.getKey();
            LatLngBounds b = (LatLngBounds)  place.getValue();
//            Log.i(TAG, "Latitude: " + current.latitude + " Longitude: " + current.longitude);
            if (b.contains(current)) {
                Log.i(TAG, POI);
                // checkForOthers((String) place.getKey(), b);
                return POI;
            }
        }
        return null;
    }

    public static String checkLocation(double lat, double lon) {
        initMap();
        LatLng current = new LatLng(lat, lon);
        for (Map.Entry place : getMap().entrySet()) {
            LatLngBounds b = (LatLngBounds)  place.getValue();
            if (b.contains(current)) {
                return (String) place.getKey();
            }
        }
        return "";
    }

    // Return a hashmap of other players
    public static ArrayList<String> checkForOthers(String currentUserLocation, String currentUsername){ //Function to check if other players are in the same POI
        // Map of other user coords
        HashMap<String, LatLng> mult = Fire.getMultiPlayerCoord();
        ArrayList<String> playersInTheSame = new ArrayList<String>();

        // Loop through all players location
        for(Map.Entry elem : mult.entrySet()){
            String otherUsername = (String)elem.getKey();
            LatLng cord = ((LatLng)elem.getValue());
            String otherUserLocation = locationCheck.checkLocation(cord.latitude, cord.longitude);

            // Check if they are in current Location & dont have the same username
            if(otherUserLocation.equals(currentUserLocation) && !(otherUsername.equals(currentUsername))){
                Log.i(TAG, otherUsername+" is in the same poi ");
                playersInTheSame.add(otherUsername);
            }
        }
        return playersInTheSame;
    }
}
