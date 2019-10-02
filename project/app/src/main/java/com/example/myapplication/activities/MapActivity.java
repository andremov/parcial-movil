package com.example.myapplication.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.example.myapplication.R;
//import com.example.myapplication.broadcast.BroadcastManager;
import com.example.myapplication.broadcast.BroadcastManager;
import com.example.myapplication.broadcast.BroadcastManagerCallerInterface;
import com.example.myapplication.gps.GPSManager;
import com.example.myapplication.gps.GPSManagerCallerInterface;
import com.example.myapplication.network.SocketManagementService;
import com.example.myapplication.objects.ServerResponse;
import com.example.myapplication.objects.User;
import com.example.myapplication.objects.UserHistoryLocations;
import com.example.myapplication.objects.UserLocations;
import com.example.myapplication.utils.ConnectionManager;
import com.example.myapplication.utils.LocationDrawer;
import com.example.myapplication.utils.Requests;
import com.example.myapplication.utils.Settings;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GPSManagerCallerInterface, BroadcastManagerCallerInterface {

    GPSManager gpsManager;
    private MapView map;
    private MyLocationNewOverlay mLocationOverlay;
    UserLocations[] userLocations;
    UserHistoryLocations[] userHistoryLocations;
    ArrayList<OverlayItem> itemsInMap;
    String lastMarkerClicked;
    User user;
    LocationDrawer locationDrawer;
    Requests requests;
    ArrayList<Location> locations;
    ConnectionManager connectionManager;

    Button btnHistoryLocations, btnBackLocations;

    BroadcastManager broadcastManagerForSocketIO;

    boolean serviceStarted = false;

    public void initializeGPSManager() {
        gpsManager = new GPSManager(this, this, this);
        gpsManager.initializeLocationManager();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_menu_map);

        connectionManager = new ConnectionManager(this);

        btnHistoryLocations = (Button) findViewById(R.id.btn_location_history);
        btnBackLocations = (Button) findViewById(R.id.btn_back_locations);

        btnHistoryLocations.setVisibility(View.VISIBLE);
        btnBackLocations.setVisibility(View.INVISIBLE);

        initDrawer();

        btnBackLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionManager.checkConnection()) {
                    btnHistoryLocations.setVisibility(View.VISIBLE);
                    btnBackLocations.setVisibility(View.INVISIBLE);
                    map.getOverlays().clear();
                    getUserLocations();
                    centerMapAgain();
                } else {
                    Toast.makeText(getApplicationContext(), "Ups! No hay conexión", Toast.LENGTH_SHORT). show();
                }

            }
        });

        ((Button)findViewById(R.id.btn_location_history)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialogShells datePickerDialogShells = new DatePickerDialogShells();
                datePickerDialogShells.show(getSupportFragmentManager(), "datePickerDialogShells");
//                    openDateDialogPicker();
//                map.getOverlays().clear();
//                requestUserLocationHistory();
            }
        });

        user = (User) getIntent().getSerializableExtra("user_obj");
        Toast.makeText(  this, "Welcome, " + user.getmFirst_name(), Toast.LENGTH_SHORT). show();

        locations = new ArrayList<Location>();
        requests = new Requests();
        locationDrawer = new LocationDrawer();

        initializeGPSManager();
        initializeOSM();

        initializeBroadcastManagerForSocketIO();

        getUserLocations();
        postCurrentLocation();

        user = (User) getIntent().getSerializableExtra("user_obj");
        Toast.makeText(this, "Welcome, " + user.getmFirst_name(), Toast.LENGTH_SHORT).show();

        ((Button) findViewById(R.id.btn_location_history)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialogShells datePickerDialogShells = new DatePickerDialogShells();
                datePickerDialogShells.show(getSupportFragmentManager(), "datePickerDialogShells");
            }
        });

        if (!serviceStarted) {
            Intent intent = new Intent(getApplicationContext(), SocketManagementService.class);
            intent.setAction(SocketManagementService.ACTION_CONNECT);
            startService(intent);
            serviceStarted = true;
        }
    }

    public void centerMapAgain(){
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        this.mLocationOverlay =
                new MyLocationNewOverlay(
                        new GpsMyLocationProvider(
                                this), map);
        this.mLocationOverlay.enableMyLocation();
        map.getOverlays().add(this.mLocationOverlay);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        setMapCenter(location);
    }

    public void initializeBroadcastManagerForSocketIO() {
        broadcastManagerForSocketIO = new BroadcastManager(this,
                SocketManagementService.
                        SOCKET_SERVICE_CHANNEL, this);
    }

    public void postCurrentLocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        String mLon = location.getLongitude() + "";
        String mLat = location.getLatitude() + "";
        String mCurrentDateandTime = sdf.format(new Date());
        user = (User) getIntent().getSerializableExtra("user_obj");
        String mUsername = user.getmUsername();

        try {
            RequestQueue queue = Volley.newRequestQueue(this);

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("lon", mLon);
            jsonBody.put("lat", mLat);
            jsonBody.put("location_timestamp", mCurrentDateandTime);
            jsonBody.put("username", mUsername);

            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Settings.getUrlAPI() + "locations", jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String res = response.toString();

                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.SSS").create();
                            ServerResponse responseJSON = gson.fromJson(res, ServerResponse.class);
                            String json = responseJSON.getData();

                            if(responseJSON.isSuccess()) {
                                Toast.makeText(getApplicationContext(), "Ubicacion atualizada", Toast.LENGTH_SHORT). show();
                            } else {
                                //ALMACENAR UBICACION EN ROM DATABASE
                                Toast.makeText(getApplicationContext(), "Ups! Error en servidor", Toast.LENGTH_SHORT). show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(getApplicationContext(), "Ups! No hay conexión", Toast.LENGTH_SHORT). show();
                }
            });

            queue.add(stringRequest);
        } catch(Exception e) { }
    }

    public void requestUserLocationHistory(String startSearchDate, String endSearchDate){
        try {
            RequestQueue queue = Volley.newRequestQueue(this);

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("first_value", startSearchDate);
            jsonBody.put("last_value", endSearchDate);

            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Settings.getUrlAPI() + "locations/" + lastMarkerClicked, jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String res = response.toString();
                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.SSS").create();
                            ServerResponse responseJSON = gson.fromJson(res, ServerResponse.class);
                            if(responseJSON.isSuccess()) {
                                String data = responseJSON.getData();
                                userHistoryLocations = gson.fromJson(data, UserHistoryLocations[].class);
                                drawUsersHistoryLocations();
                            } else {
                                Toast.makeText(getApplicationContext(), "Ups! Error en servidor", Toast.LENGTH_SHORT). show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(getApplicationContext(), "Ups! No hay conexión", Toast.LENGTH_SHORT). show();
                }
            });

            queue.add(stringRequest);
        } catch(Exception e) { }




//        RequestQueue queue = Volley.newRequestQueue(this);
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, Settings.getUrlAPI() + "locations/" + lastMarkerClicked,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.SSS").create();
//                        ServerResponse responseJSON = gson.fromJson(response, ServerResponse.class);
//                        String data = responseJSON.getData();
//                        userHistoryLocations = gson.fromJson(data, UserHistoryLocations[].class);
//                        drawUsersHistoryLocations();
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getApplicationContext(), "That didn't work!", Toast.LENGTH_SHORT).show();
//            }
//        });
//        queue.add(stringRequest);
    }

    public String getLastMarkerClicked() {
        return lastMarkerClicked;
    }

    public void setLastMarkerClicked(String lastMarkerClicked) {
        this.lastMarkerClicked = lastMarkerClicked;
    }

    public UserHistoryLocations[] getUserHistoryLocations() {
        return userHistoryLocations;
    }

    public void setUserHistoryLocations(UserHistoryLocations[] userHistoryLocations) {
        this.userHistoryLocations = userHistoryLocations;
    }

    private void getUserLocations() {
        RequestQueue queue = Volley.newRequestQueue(this);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Settings.getUrlAPI() + "locations",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //FORMA DE SACAR INFO DE PETICION
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.SSS").create();
                        ServerResponse responseJSON = gson.fromJson(response, ServerResponse.class);
                        String data = responseJSON.getData();
                        userLocations = gson.fromJson(data, UserLocations[].class);
                        drawUsersLocations();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getApplicationContext(), "Ups! No hay conexión", Toast.LENGTH_SHORT). show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void drawUsersHistoryLocations(){
        map.getOverlays().clear();
        btnHistoryLocations.setVisibility(View.INVISIBLE);
        btnBackLocations.setVisibility(View.VISIBLE);
        for(UserHistoryLocations ul : userHistoryLocations) {;
            Marker startMarker = new Marker(map);
            startMarker.setPosition(new GeoPoint(ul.getmLat(), ul.getmLon()));
            startMarker.setTitle(ul.getmUsername());
            startMarker.setTextIcon(ul.getmLocation_timestamp());
            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            map.getOverlays().add(startMarker);

            Drawable newMarker = getResources().getDrawable(R.drawable.ic_location);
            newMarker.setTint(Color.argb(255, 14, 35, 175));
            startMarker.setIcon(newMarker);

            startMarker.setTitle(ul.getmLocation_timestamp());
        }
    }

    private void drawUsersLocations(){
        for(UserLocations ul : userLocations) {
            Marker startMarker = new Marker(map);
            startMarker.setPosition(new GeoPoint(ul.getmLat(), ul.getmLon()));
            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            map.getOverlays().add(startMarker);
            Drawable icon;
            if (ul.getmStatus().equals("online")) {
                icon = getResources().getDrawable(R.drawable.ic_location);
                icon.setTint(Color.argb(255, 38, 139, 45));
            } else {
                icon = getResources().getDrawable(R.drawable.ic_location);
                icon.setTint(Color.argb(255, 200, 49, 38));
            }
            startMarker.setIcon(icon);
            startMarker.setTitle(ul.getmUsername());
            startMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    lastMarkerClicked = marker.getTitle();
                    Toast.makeText(getApplicationContext(), lastMarkerClicked, Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

            //CREATE MYINFOWINDOW CLASS
//            InfoWindow infoWindow = new MyInfoWindow(R.layout.clicked_marker_dialog, map);
//            startMarker.setInfoWindow(infoWindow);
        }
    }

    private void drawUsersLocationsTEST() {
        for(UserLocations ul : userLocations) {
            OverlayItem overlayItem = new OverlayItem(ul.getmFull_name(), ul.getmLastSeen(), new GeoPoint(ul.getmLat(), ul.getmLon()));
            Drawable icon;

            //CHECK IF USER IS ONLINE AND SET MARKER
            if (ul.getmStatus().equals("online")) {
                icon = this.getResources().getDrawable(R.drawable.ic_location);
                icon.setTint(Color.argb(255, 38, 139, 45));
            } else {
                icon = this.getResources().getDrawable(R.drawable.ic_location);
                icon.setTint(Color.argb(255, 200, 49, 38));
            }

            overlayItem.setMarker(icon);
            //ADD TO ITEMS IN MAP ARRAY
            itemsInMap.add(overlayItem);
        }
//        itemsInMap.add(new OverlayItem("Agua", "El marcador en el agua derecha", new GeoPoint(11.038239, -74.665461))); // Lat/Lon decimal degrees
//        itemsInMap.add(new OverlayItem("Agua 2", "El marcador en el agua izquierda", new GeoPoint(11.058781, -74.934484))); // Lat/Lon decimal degrees

        //the overlay
        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(itemsInMap,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        Toast.makeText(getApplicationContext(), "SINGLE CLICK " + index, Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    @Override
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return false;
                    }
                }, this);
        mOverlay.setFocusItemsOnTap(true);

        map.getOverlays().add(mOverlay);
    }

    private void httpRequestTest() {
        final TextView textView = (TextView) findViewById(R.id.text);
// ...

// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Settings.getUrlAPI() + "users/demarchenac",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        textView.setText(response);
//                        FORMA DE SACAR INFO DE PETICION
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.SSS").create();
                        ServerResponse responseJSON = gson.fromJson(response, ServerResponse.class);
                        String json = responseJSON.getData();
                        User user = gson.fromJson(json, User.class);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "That didn't work!", Toast.LENGTH_SHORT).show();
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @Override
    public void needPermissions() {
        this.requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION},
                1001);
    }

    @Override
    public void locationHasBeenReceived(final Location location) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                ((TextView)findViewById(R.id.latitude_text_view)).setText(location.getLatitude()+"");
//                ((TextView)findViewById(R.id.longitude_text_view)).setText(location.getLongitude()+"");
                if (map != null)
                    setMapCenter(location);

            }
        });

        /*
        if (serviceStarted)
            if (broadcastManagerForSocketIO != null) {
                broadcastManagerForSocketIO.sendBroadcast(
                        SocketManagementService.CLIENT_TO_SERVER_MESSAGE,
                        location.getLatitude() + " / " + location.getLongitude() + "\n");
            }
         */
    }

    @Override
    public void gpsErrorHasBeenThrown(final Exception error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder =
                        new AlertDialog.
                                Builder(getApplicationContext());
                builder.setTitle("GPS Error")
                        .setMessage(error.getMessage())
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //TODO
                            }
                        });
                builder.show();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1001) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,
                        "Thanks!!", Toast.LENGTH_SHORT).show();
                gpsManager.startGPSRequesting();
            }

        }
        if (requestCode == 1002) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeOSM();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

    }

    public void initializeOSM() {
        try {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.
                                        WRITE_EXTERNAL_STORAGE}, 1002);

                return;
            }
            Context ctx = getApplicationContext();
            Configuration.getInstance().load(ctx,
                    PreferenceManager.
                            getDefaultSharedPreferences(ctx));
            map = (MapView) findViewById(R.id.map);
            map.setTileSource(TileSourceFactory.MAPNIK);
            this.mLocationOverlay =
                    new MyLocationNewOverlay(
                            new GpsMyLocationProvider(
                                    this), map);
            this.mLocationOverlay.enableMyLocation();
            map.getOverlays().add(this.mLocationOverlay);
        } catch (Exception error) {
            Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    public void setMapCenter(Location location) {
        IMapController mapController =
                map.getController();
        mapController.setZoom(15);
        GeoPoint startPoint = new GeoPoint(
                location.getLatitude(), location.getLongitude());
        mapController.setCenter(startPoint);
    }

    @Override
    public void MessageReceivedThroughBroadcastManager(final String channel, final String type, final String message) {
        System.out.println("--- [MAP ACTIVITY] --- " +message);
        if(message.equals("update@locations")){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    getUserLocations();
                }
            });
        }


    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_chat) {
            goToChat();
        } else if (id == R.id.menu_logout) {
            doLogOut();
        } else if (id == R.id.menu_map) {
            // YA ESTÁ ACÁ
        }

        DrawerLayout drawer = findViewById(R.id.map_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void doLogOut() {
        RequestQueue queue = Volley.newRequestQueue(this);
        // Request a string response from the provided URL.

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Settings.getUrlAPI() + "users/logout/" + user.getmUsername(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.SSS").create();
                        ServerResponse responseJSON = gson.fromJson(response, ServerResponse.class);

                        if (responseJSON.isSuccess()) {

                            Intent intetToBecalled=new
                                    Intent(getApplicationContext(),
                                    LogInActivity.class);

                            finish();

                            startActivity(intetToBecalled);
                            Toast.makeText(getApplicationContext(), "Logged out.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Couldn't log out :(", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "That didn't work!", Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void initDrawer() {

        Toolbar toolbar = findViewById(R.id.map_toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.map_drawer_layout);

        NavigationView navigationView = findViewById(R.id.map_nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.menu_open, R.string.menu_close);

        drawer.addDrawerListener(toggle);

        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.nav_view);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            doLogOut();
        }
    }

    public void goToChat() {
        Intent intetToBecalled=new
                Intent(getApplicationContext(),
                ChatActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("user_obj", user);

        intetToBecalled.putExtras(bundle);

        startActivity(intetToBecalled);
    }

    @Override
    public void ErrorAtBroadcastManager(Exception error) {

    }

    @Override
    protected void onDestroy() {
        /*
        if (broadcastManagerForSocketIO != null) {
            broadcastManagerForSocketIO.unRegister();
        }
        */

        super.onDestroy();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
//            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}
