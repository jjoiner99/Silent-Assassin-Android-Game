package com.example.cs440project.interestPoints;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
// Find coordinates using https://www.latlong.net/
// Map styled with https://mapstyle.withgoogle.com/
public class InterestPoints {
    private static float[] hsv = new float[3];

    // Colors of Buildings
    private static int polygonFillColor = Color.parseColor("#1DA1F2");
    private static int polygonStrokeColor = Color.parseColor("#1DA1F2");

    public static LatLng uicCenter = new LatLng(41.871899, -87.649252);
    public static LatLngBounds uicBounds = new LatLngBounds(
            new LatLng(41.869573, -87.651078),
            new LatLng(41.874249, -87.647262)
    );

    // Draw all of the shapes (Could do this cleaner...)
    public static void drawBuildingPolygons(GoogleMap mMap) {
        Polygon sce = mMap.addPolygon(new PolygonOptions() // Student Center East
                .add(new LatLng(41.872527, -87.647666))
                .add(new LatLng(41.872523, -87.648255))
                .add(new LatLng(41.871304, -87.648207))
                .add(new LatLng(41.871312, -87.647628)).fillColor(polygonFillColor).strokeColor(polygonStrokeColor)
        );

        Polygon sel = mMap.addPolygon(new PolygonOptions() //Science Engineering Labs
                .add(new LatLng(41.870756, -87.649418))
                .add(new LatLng(41.870061, -87.649394))
                .add(new LatLng(41.870098, -87.647349))
                .add(new LatLng(41.870785, -87.647387)).fillColor(polygonFillColor).strokeColor(polygonStrokeColor)
        );

        Polygon lib = mMap.addPolygon(new PolygonOptions() // Library
                .add(new LatLng(41.872459, -87.650761))
                .add(new LatLng(41.872475, -87.650278))
                .add(new LatLng(41.871289, -87.650246))
                .add(new LatLng(41.871285, -87.650729)).fillColor(polygonFillColor).strokeColor(polygonStrokeColor)
        );

        Polygon bsb = mMap.addPolygon(new PolygonOptions() // Behavior Science Building
                .add(new LatLng(41.874211, -87.653192))
                .add(new LatLng(41.874220, -87.652874))
                .add(new LatLng(41.873989, -87.652531))
                .add(new LatLng(41.873929, -87.652075))
                .add(new LatLng(41.873577, -87.652070))
                .add(new LatLng(41.873505, -87.652654))
                .add(new LatLng(41.873258, -87.652896))
                .add(new LatLng(41.873270, -87.653213))
                .fillColor(polygonFillColor)
                .strokeColor(polygonStrokeColor)
        );

        Polygon ses = mMap.addPolygon(new PolygonOptions() // Science Engineering South
                .add(new LatLng(41.869312, -87.648341))
                .add(new LatLng(41.869323, -87.647510))
                .add(new LatLng(41.869107, -87.647465))
                .add(new LatLng(41.869095, -87.647242))
                .add(new LatLng(41.868912, -87.647231))
                .add(new LatLng(41.868892, -87.647435))
                .add(new LatLng(41.868624, -87.647526))
                .add(new LatLng(41.868616, -87.648422))
                .add(new LatLng(41.868836, -87.648475))
                .add(new LatLng(41.868608, -87.648663))
                .add(new LatLng(41.868620, -87.649189))
                .add(new LatLng(41.869255, -87.649216))
                .add(new LatLng(41.869327, -87.648749))
                .add(new LatLng(41.869052, -87.648642))
                .add(new LatLng(41.869020, -87.648401))
                .fillColor(polygonFillColor)
                .strokeColor(polygonStrokeColor)
        );

        Polygon lcd = mMap.addPolygon(new PolygonOptions() // Lecture Center D
                .add(new LatLng(41.871855, -87.648924))
                .add(new LatLng(41.871855, -87.648460))
                .add(new LatLng(41.871544, -87.648454))
                .add(new LatLng(41.871523, -87.648927))
                .fillColor(polygonFillColor)
                .strokeColor(polygonStrokeColor)
        );
        Polygon lcc = mMap.addPolygon(new PolygonOptions() // Lecture Center C
                .add(new LatLng(41.872266, -87.648938))
                .add(new LatLng(41.872279, -87.648481))
                .add(new LatLng(41.871940, -87.648476))
                .add(new LatLng(41.871940, -87.648926))
                .fillColor(polygonFillColor)
                .strokeColor(polygonStrokeColor)
        );
        Polygon lca = mMap.addPolygon(new PolygonOptions() // Lecture Center A
                .add(new LatLng(41.872258, -87.650052))
                .add(new LatLng(41.872263, -87.649591))
                .add(new LatLng(41.871936, -87.649564))
                .add(new LatLng(41.871904, -87.650026))
                .fillColor(polygonFillColor)
                .strokeColor(polygonStrokeColor)
        );
        Polygon lcf = mMap.addPolygon(new PolygonOptions() // Lecture Center F
                .add(new LatLng(41.871852, -87.650031))
                .add(new LatLng(41.871840, -87.649559))
                .add(new LatLng(41.871520, -87.649554))
                .add(new LatLng(41.871504, -87.650015))
                .fillColor(polygonFillColor)
                .strokeColor(polygonStrokeColor)
        );
        Polygon lh = mMap.addPolygon(new PolygonOptions() // Lincoln Hall
                .add(new LatLng(41.872568, -87.649485))
                .add(new LatLng(41.872584, -87.649055))
                .add(new LatLng(41.872396, -87.649034))
                .add(new LatLng(41.872379, -87.649489)).fillColor(polygonFillColor)
                .strokeColor(polygonStrokeColor)
        );
        Polygon dh = mMap.addPolygon(new PolygonOptions() // Douglas Hall
                .add(new LatLng(41.872994, -87.649210))
                .add(new LatLng(41.872994, -87.648958))
                .add(new LatLng(41.872683, -87.648948))
                .add(new LatLng(41.872667, -87.649205))
                .fillColor(polygonFillColor).strokeColor(polygonStrokeColor)
        );
        Polygon sh = mMap.addPolygon(new PolygonOptions() // Stevenson Hall
                .add(new LatLng(41.872938, -87.650589))
                .add(new LatLng(41.872954, -87.650112))
                .add(new LatLng(41.872751, -87.650090))
                .add(new LatLng(41.872766, -87.650546))
                .fillColor(polygonFillColor)
                .strokeColor(polygonStrokeColor)
        );
    }
}