package com.example.cs440project.mapPreference;

import android.content.res.Resources;
import android.util.Log;

import com.example.cs440project.MapsActivity;
import com.example.cs440project.R;
import com.example.cs440project.interestPoints.InterestPoints;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MapStyleOptions;

// Map Preference dictates the style of the map
public class MapPreference {
    public static GoogleMap.OnMapLoadedCallback setCamera(GoogleMap mMap){
        mMap.setLatLngBoundsForCameraTarget(InterestPoints.uicBounds);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(InterestPoints.uicCenter));
        mMap.setMinZoomPreference(16.5f);
        mMap.setMaxZoomPreference(18.0f);
        return null;
    }

    public static void setMapStyle(GoogleMap mMap, MapsActivity mapsActivity) {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            mapsActivity, R.raw.style_json));

            if (!success) {
                Log.e("Map", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("Map", "Can't find style.", e);
        }
    }

}
