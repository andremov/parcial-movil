package com.example.myapplication.objects;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserLocations {

    @SerializedName("username")
    private String mUsername;
    @SerializedName("full_name")
    private String mFull_name;
    @SerializedName("lat")
    private double mLat;
    @SerializedName("lon")
    private double mLon;
    @SerializedName("lastSeen")
    private Date mLastSeen;
    @SerializedName("status")
    private String mStatus;

    public UserLocations(String mUsername, String mFull_name, String mLat, String mLon, String mLastSeen, String mStatus) throws ParseException {
        this.mUsername = mUsername;
        this.mFull_name = mFull_name;
        this.mLat = Double.parseDouble(mLat);
        this.mLon = Double.parseDouble(mLon);
        this.mStatus = mStatus;
        this.mLastSeen = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(mLastSeen);
    }

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getmFull_name() {
        return mFull_name;
    }

    public void setmFull_name(String mFull_name) {
        this.mFull_name = mFull_name;
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

    public String getmLastSeen() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return dateFormat.format(mLastSeen);
    }

    public void setmLastSeen(Date mLastSeen) {
        this.mLastSeen = mLastSeen;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }
}
