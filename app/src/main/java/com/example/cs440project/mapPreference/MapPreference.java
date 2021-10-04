package com.example.cs440project.mapPreference;

import com.example.cs440project.R;
import com.example.cs440project.interestPoints.InterestPoints;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MapStyleOptions;

public class MapPreference {
    public static GoogleMap.OnMapLoadedCallback setCamera(GoogleMap mMap){
        mMap.setLatLngBoundsForCameraTarget(InterestPoints.uicBounds);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(InterestPoints.uicCenter));
        mMap.setMinZoomPreference(17.0f);
        mMap.setMaxZoomPreference(18.0f);
        return null;
    }
    public static void setMapStyle(GoogleMap mMap) {
    }
}
