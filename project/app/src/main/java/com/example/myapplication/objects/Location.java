package com.example.myapplication.objects;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Location {
    @SerializedName("lat")
    private double mLat;
    @SerializedName("lon")
    private double mLon;
    @SerializedName("location_timestamp")
    private Date mLocation_timestamp;
    @SerializedName("username")
    private String mUsername;

    public Location(
            String lat, String lon, String location_timestamp, String username
    ) throws ParseException {
        this.mLat = Double.parseDouble(lat);
        this.mLon = Double.parseDouble(lon);
        this.mLocation_timestamp
                = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
                .parse(location_timestamp);
        this.mUsername = username;
    }

    public double getmLat() {
        return mLat;
    }

    public void setmLat(double mLat) {
        this.mLat = mLat;
    }

    public double getmLon() {
        return mLon;
    }

    public void setmLon(double mLon) {
        this.mLon = mLon;
    }

    public Date getmLocation_timestamp() {
        return mLocation_timestamp;
    }

    public void setmLocation_timestamp(Date mLocation_timestamp) {
        this.mLocation_timestamp = mLocation_timestamp;
    }

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }
}
