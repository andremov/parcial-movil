package com.example.myapplication.objects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class User implements Serializable {
    @SerializedName("username")
    private String mUsername;
    @SerializedName("first_name")
    private String mFirst_name;
    @SerializedName("last_name")
    private String mLast_name;
    @SerializedName("full_name")
    private String mFull_name;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("lastLat")
    private double mLastLat;
    @SerializedName("lastLon")
    private double mLastLon;
    @SerializedName("status")
    private String mStatus;
    @SerializedName("lastSeen")
    private Date mLastSeen;
    @SerializedName("ip")
    private String ip;

    public User() throws ParseException {
    }

    public User(
            String username, String first_name, String last_name, String name,
            String email, String lastLat, String lastLon, String status,
            String lastSeen, String ip
    ) throws ParseException {
        this.mUsername = username;
        this.mFirst_name = first_name;
        this.mLast_name = last_name;
        this.mFull_name = name;
        this.mEmail = email;
        this.mLastLat = Double.parseDouble(lastLat);
        this.mLastLon = Double.parseDouble(lastLon);
        this.mStatus = status;
        this.mLastSeen = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(lastSeen);
        this.ip = ip;
    }

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getmFirst_name() {
        return mFirst_name;
    }

    public void setmFirst_name(String mFirst_name) {
        this.mFirst_name = mFirst_name;
    }

    public String getmLast_name() {
        return mLast_name;
    }

    public void setmLast_name(String mLast_name) {
        this.mLast_name = mLast_name;
    }

    public String getmFull_name() {
        return mFull_name;
    }

    public void setmFull_name(String mFull_name) {
        this.mFull_name = mFull_name;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public double getmLastLat() {
        return mLastLat;
    }

    public void setmLastLat(double mLastLat) {
        this.mLastLat = mLastLat;
    }

    public double getmLastLon() {
        return mLastLon;
    }

    public void setmLastLon(double mLastLon) {
        this.mLastLon = mLastLon;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public Date getmLastSeen() {
        return mLastSeen;
    }

    public void setmLastSeen(Date mLastSeen) {
        this.mLastSeen = mLastSeen;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
