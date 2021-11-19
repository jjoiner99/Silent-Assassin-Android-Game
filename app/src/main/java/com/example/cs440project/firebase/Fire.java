

package com.example.cs440project.firebase;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;
import androidx.core.content.ContextCompat;
import com.example.cs440project.R;
import com.example.cs440project.user.User;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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

public class Fire {
    //Logcat TAG
    private static final String TAG = "firebaseService";
    private static final HashMap<String, LatLngBounds> places = new HashMap<>();
    private static final HashMap<String, LatLng> multiPlayerCoord = new HashMap<>();
    private static final ArrayList<Marker> markers = new ArrayList<>();
    private static final ArrayList<User> users = new ArrayList<>();

    // Table Ref
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final DatabaseReference usersRef = database.getReference("Users");

    // Init database should only be called if we decide to change the long and lat of an area but not onCreate
    public static void initDatabase() {
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
    }

    // Takes a username and subtracts points
    public static void killPlayer(String username){
        usersRef.child(username).child("points").setValue(1);
    }

    public static HashMap<String, LatLng> getMultiPlayerCoord() {
        return multiPlayerCoord;
    }

    public static ArrayList<Marker> getMarkers() {return markers; }

    public static void fetchMultiPlayLocation(GoogleMap mMap, int isHunter, Context mContext) {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Double lon = ds.child("lon").getValue(Double.class);
                    Double lat = ds.child("lat").getValue(Double.class);
                    String username = ds.child("username").getValue(String.class);

                    LatLng coord = new LatLng(lat, lon);
                    multiPlayerCoord.put(username, coord);
                }

                if(isHunter != 1) {
                    // First render
                    if (markers.isEmpty()) {
                        drawMarkers(mContext);
                    } else {
                        // Update positions of players as they move
                        deleteMarkers();
                        drawMarkers(mContext);
                    }
                }
            }

            private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
                Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
                vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
                Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                vectorDrawable.draw(canvas);
                return BitmapDescriptorFactory.fromBitmap(bitmap);
            }

            // Draws marker for each player
            private void drawMarkers(Context mContext){
                for (Map.Entry<String, LatLng> entry : multiPlayerCoord.entrySet()) {
//                    Marker marker = mMap.addMarker(new MarkerOptions().position(entry.getValue()).title(entry.getKey()).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_person_background)));
                    // Print

                    markers.add(mMap.addMarker(new MarkerOptions()
                            .position(entry.getValue())
                            .icon(bitmapDescriptorFromVector(mContext, R.drawable.person2))
                            .title(entry.getKey())));
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        usersRef.addValueEventListener(postListener);
    }
    // Delete all markers
    public static void deleteMarkers(){
        for (Marker m : markers) {
            m.remove();
        }
        markers.clear();
    }
}