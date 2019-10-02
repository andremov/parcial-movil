package com.example.myapplication.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.broadcast.BroadcastManager;
import com.example.myapplication.broadcast.BroadcastManagerCallerInterface;
import com.example.myapplication.gps.GPSManager;
import com.example.myapplication.gps.GPSManagerCallerInterface;
import com.example.myapplication.network.SocketManagementService;
import com.example.myapplication.objects.ServerResponse;
import com.example.myapplication.objects.User;
import com.example.myapplication.objects.UserHistoryLocations;
import com.example.myapplication.objects.UserLocations;
import com.example.myapplication.utils.Settings;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BroadcastManagerCallerInterface {

    GPSManager gpsManager;
    private MapView map;
    private MyLocationNewOverlay mLocationOverlay;
    BroadcastManager broadcastManagerForSocketIO;
    ArrayList<String> listOfMessages = new ArrayList<>();
    ArrayAdapter<String> adapter;
    boolean serviceStarted = false;
    UserLocations[] userLocations;
    UserHistoryLocations[] userHistoryLocations;
    ArrayList<OverlayItem> itemsInMap;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_menu_chat);
//        hideSystemUI();

        initDrawer();

        user = (User) getIntent().getSerializableExtra("user_obj");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_chat) {
            // YA ESTÁ ACÁ
        } else if (id == R.id.menu_logout) {
            doLogOut();
        } else if (id == R.id.menu_map) {
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.chat_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void initDrawer() {

        Toolbar toolbar = findViewById(R.id.chat_toolbar);
        DrawerLayout drawer = findViewById(R.id.chat_drawer_layout);
        NavigationView navigationView = findViewById(R.id.chat_nav_view);

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
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (broadcastManagerForSocketIO != null) {
            broadcastManagerForSocketIO.unRegister();
        }
        super.onDestroy();
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

    @Override
    public void MessageReceivedThroughBroadcastManager(final String channel,final String type, final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listOfMessages.add(message);
                ((ListView)findViewById(R.id.messages_list_view)).setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void ErrorAtBroadcastManager(Exception error) {

    }

}
