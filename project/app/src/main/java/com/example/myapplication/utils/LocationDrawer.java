package com.example.myapplication.utils;

import android.content.Context;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.objects.UserLocations;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;


public class LocationDrawer {

    public String lastMarkerClicked;

    public void drawUsersLocations(UserLocations[] userLocations, MapView map, Context context){
        for(UserLocations ul : userLocations) {
            Marker startMarker = new Marker(map);
            startMarker.setPosition(new GeoPoint(ul.getmLat(), ul.getmLon()));
            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            map.getOverlays().add(startMarker);
            if (ul.getmStatus().equals("online")) {
                startMarker.setIcon(context.getResources().getDrawable(R.drawable.ic_location_online));
            } else {
                startMarker.setIcon(context.getResources().getDrawable(R.drawable.ic_location_offline));
            }
            startMarker.setTitle(ul.getmUsername());
            startMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    lastMarkerClicked = marker.getTitle();
//                    Toast.makeText(context.getApplicationContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

            //CREATE MYINFOWINDOW CLASS
//            InfoWindow infoWindow = new MyInfoWindow(R.layout.clicked_marker_dialog, map);
//            startMarker.setInfoWindow(infoWindow);
        }
    }

    public String getLastMarkerClicked() {
        return lastMarkerClicked;
    }

    public void setLastMarkerClicked(String lastMarkerClicked) {
        this.lastMarkerClicked = lastMarkerClicked;
    }
}
