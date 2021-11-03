package com.example.cs440project.user;

import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;

public class User {
    private String username;
    private int role;
    private static User instance = null;
    private double lon;
    private double lat;
    private String ID;

    public User() {
        ID = UUID.randomUUID().toString();
        username = ID;
    }

    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}