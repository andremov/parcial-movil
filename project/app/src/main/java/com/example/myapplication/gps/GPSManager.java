package com.example.myapplication.gps;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;

import com.example.myapplication.activities.MapActivity;

public class GPSManager implements LocationListener {

    private MapActivity mapActivity;
    private GPSManagerCallerInterface caller;

    LocationManager locationManager;

    public GPSManager(MapActivity mapActivity,
                      GPSManagerCallerInterface caller) {
        this.mapActivity = mapActivity;
        this.caller = caller;
    }

    public void initializeLocationManager() {
        try {
            locationManager = (LocationManager) this.mapActivity.getSystemService(Context.LOCATION_SERVICE);
            if (mapActivity.checkSelfPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED ||
                    mapActivity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                caller.needPermissions();
                return;
            }else{
                startGPSRequesting();
            }

        }catch (Exception error){
            caller.gpsErrorHasBeenThrown(error);
        }
    }

    public void startGPSRequesting(){
        try {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0, 50, this, Looper.getMainLooper());
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0, 50, this, Looper.getMainLooper());
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }catch (Exception error){
            caller.gpsErrorHasBeenThrown(error);
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        caller.locationHasBeenReceived(location);
        mapActivity.postCurrentLocation();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
