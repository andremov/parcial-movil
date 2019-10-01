package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.preference.PreferenceManager;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.example.myapplication.broadcast.BroadcastManager;
import com.example.myapplication.broadcast.BroadcastManagerCallerInterface;
import com.example.myapplication.gps.GPSManager;
import com.example.myapplication.gps.GPSManagerCallerInterface;
import com.example.myapplication.network.SocketManagementService;
import com.example.myapplication.objects.ServerResponse;
import com.example.myapplication.objects.User;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements GPSManagerCallerInterface, BroadcastManagerCallerInterface {

    GPSManager gpsManager;
    private MapView map;
    private MyLocationNewOverlay mLocationOverlay;
    BroadcastManager broadcastManagerForSocketIO;
    ArrayList<String> listOfMessages = new ArrayList<>();
    ArrayAdapter<String> adapter;
    boolean serviceStarted = false;

    //    final String url ="http://localhost:8080/MovilAPI/api/";
    final String url = "http://192.168.1.71:8080/MovilAPI/api/";

    ArrayList<Location> locations;

    public void initializeGPSManager() {
        gpsManager = new GPSManager(this, this);
        gpsManager.initializeLocationManager();
    }

    public void initializeBroadcastManagerForSocketIO() {
        broadcastManagerForSocketIO = new BroadcastManager(this,
                SocketManagementService.
                        SOCKET_SERVICE_CHANNEL, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_map);

        locations = new ArrayList<Location>();

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        NavigationView navigationView = findViewById(R.id.nav_view);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//        navigationView.setNavigationItemSelectedListener(this);

        String user = getIntent().getExtras().
                getString("user_name");
        Toast.makeText(
                this,
                "Welcome " + user, Toast.LENGTH_SHORT).
                show();
//        ((Button)findViewById(R.id.start_service_button)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(
//                        getApplicationContext(),SocketManagementService.class);
//                intent.putExtra("SERVER_HOST",((EditText)findViewById(R.id.server_ip_txt)).getText()+"");
//                intent.putExtra("SERVER_PORT",Integer.parseInt(((EditText)findViewById(R.id.server_port_txt)).getText()+""));
//                intent.setAction(SocketManagementService.ACTION_CONNECT);
//                startService(intent);
//                serviceStarted=true;
//            }
//        });
        initializeGPSManager();
        initializeOSM();
        initializeBroadcastManagerForSocketIO();
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, listOfMessages);

        getUserLocations();


        //MODIFICAR *************************************

        //CREACIÓN DE PINES CLICKEABLES

        //your items
        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        items.add(new OverlayItem("Agua", "El marcador en el agua derecha", new GeoPoint(11.038239, -74.665461))); // Lat/Lon decimal degrees
        items.add(new OverlayItem("Agua 2", "El marcador en el agua izquierda", new GeoPoint(11.058781, -74.934484))); // Lat/Lon decimal degrees

        //the overlay
        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        Toast.makeText(getApplicationContext(), "SINGLE CLICK" + index, Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    @Override
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return false;
                    }
                }, this);
        mOverlay.setFocusItemsOnTap(true);

        map.getOverlays().add(mOverlay);


        //MODIFICAR *************************************
    }

    private void getUserLocations() {
        RequestQueue queue = Volley.newRequestQueue(this);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + "users/demarchenac",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //FORMA DE SACAR INFO DE PETICION
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.SSS").create();
                        ServerResponse responseJSON = gson.fromJson(response, ServerResponse.class);
                        String data = responseJSON.getData();
                        Toast.makeText(getApplicationContext(), data.toString(), Toast.LENGTH_SHORT).show();

                        //Location[] locations = gson.fromJson(data, Location[].class);

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

    private void httpRequestTest() {
        final TextView textView = (TextView) findViewById(R.id.text);
// ...

// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + "users/demarchenac",
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

        if (serviceStarted)
            if (broadcastManagerForSocketIO != null) {
                broadcastManagerForSocketIO.sendBroadcast(
                        SocketManagementService.CLIENT_TO_SERVER_MESSAGE,
                        location.getLatitude() + " / " + location.getLongitude() + "\n");
            }
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
        mapController.setZoom(9.5);
        GeoPoint startPoint = new GeoPoint(
                location.getLatitude(), location.getLongitude());
        mapController.setCenter(startPoint);
    }

    @Override
    public void MessageReceivedThroughBroadcastManager(final String channel, final String type, final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listOfMessages.add(message);
                ((ListView) findViewById(R.id.messages_list_view)).setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void ErrorAtBroadcastManager(Exception error) {

    }

    @Override
    protected void onDestroy() {
        if (broadcastManagerForSocketIO != null) {
            broadcastManagerForSocketIO.unRegister();
        }
        super.onDestroy();
    }
}

