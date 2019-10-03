package com.example.myapplication.objects;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserHistoryLocations {

    @SerializedName("lat")
    private double mLat;
    @SerializedName("lon")
    private double mLon;
    @SerializedName("location_timestamp")
    private Date mLocation_timestamp;
    @SerializedName("username")
    private String mUsername;

    public UserHistoryLocations(String mUsername, String mLat, String mLon, String mLocation_timestamp) throws ParseException {
        this.mUsername = mUsername;
        this.mLat = Double.parseDouble(mLat);
        this.mLon = Double.parseDouble(mLon);
        this.mLocation_timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(mLocation_timestamp);
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

    public String getmLocation_timestamp() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return dateFormat.format(mLocation_timestamp);
    }

    public double getTime() {
        return Long.valueOf(this.mLocation_timestamp.getTime()).doubleValue();
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
