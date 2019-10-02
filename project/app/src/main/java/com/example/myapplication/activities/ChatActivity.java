package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.broadcast.BroadcastManager;
import com.example.myapplication.broadcast.BroadcastManagerCallerInterface;
import com.example.myapplication.network.MessageAdapter;
import com.example.myapplication.network.SocketManagementService;
import com.example.myapplication.objects.Message;
import com.example.myapplication.objects.ServerResponse;
import com.example.myapplication.objects.User;
import com.example.myapplication.objects.UserHistoryLocations;
import com.example.myapplication.utils.Settings;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BroadcastManagerCallerInterface {


    String lastReceived;
    MessageAdapter adapter;
    ListView messagesView;
    User user;

    BroadcastManager broadcastManagerForSocketIO;

    public void initializeBroadcastManagerForSocketIO() {
        broadcastManagerForSocketIO = new BroadcastManager(this,
                SocketManagementService.
                        SOCKET_SERVICE_CHANNEL, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_menu_chat);
//        hideSystemUI();

        initDrawer();


        ((Button)findViewById(R.id.send_button)).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        sendMessage();

                        ((EditText) findViewById(R.id.message_body)).getText().clear();
                    }
                });

        user = (User) getIntent().getSerializableExtra("user_obj");

        adapter = new MessageAdapter(this, user.getmUsername());

        messagesView = ((ListView)findViewById(R.id.messages_list_view));
        messagesView.setAdapter(adapter);

        requestAllMessages();
        initializeBroadcastManagerForSocketIO();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

    }

    public void requestAllMessages() {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Settings.getUrlAPI() + "messages",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.SSS").create();
                        ServerResponse responseJSON = gson.fromJson(response, ServerResponse.class);
                        String data = responseJSON.getData();

                        if (responseJSON.isSuccess()) {
                            try {
                                lastReceived =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
                                        ArrayList<LinkedTreeMap> msgs = gson.fromJson(data, ArrayList.class);
                                for(LinkedTreeMap message : msgs) {

                                        MessageReceived(new Message((String) message.get("body"), (String) message.get("message_timestamp"), (String) message.get("sender")));

                                }
                            } catch (Exception e) { }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "That didn't work!", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);


    }

    public void requestNewMessages() {
        final String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());

        try {
            RequestQueue queue = Volley.newRequestQueue(this);

            JSONObject jsonBody = new JSONObject();

            jsonBody.put("first_value", lastReceived);
            jsonBody.put("last_value", timestamp);


            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Settings.getUrlAPI() + "withinDate", jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String res = response.toString();

                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.SSS").create();
                            ServerResponse responseJSON = gson.fromJson(res, ServerResponse.class);
                            String data = responseJSON.getData();

                            if(responseJSON.isSuccess()) {
                                try {
                                    lastReceived = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
                                    ArrayList<LinkedTreeMap> msgs = gson.fromJson(data, ArrayList.class);
                                    for (LinkedTreeMap message : msgs) {

                                        MessageReceived(new Message((String) message.get("body"), (String) message.get("message_timestamp"), (String) message.get("sender")));

                                    }
                                } catch (Exception e) {
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


            queue.add(stringRequest);

        } catch(Exception e) { }

    }

    public void sendMessage() {
        /*
         {
            "body": "Probando envio de mensajes 001",
            "message_timestamp": "2019-09-29 14:53:10.15",
            "sender": "demarchenac"
          }
         */

        final String messageBody = ((TextView)findViewById(R.id.message_body)).getText().toString();
        final String username = user.getmUsername();
        final String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());

        try {
            RequestQueue queue = Volley.newRequestQueue(this);

            JSONObject jsonBody = new JSONObject();

            jsonBody.put("body", messageBody);
            jsonBody.put("message_timestamp", timestamp);
            jsonBody.put("sender", username);


            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Settings.getUrlAPI() + "messages", jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String res = response.toString();

                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.SSS").create();
                            ServerResponse responseJSON = gson.fromJson(res, ServerResponse.class);
                            String json = responseJSON.getData();

                            if(responseJSON.isSuccess()) {
                                try {
                                    Message msg = new Message(messageBody, timestamp, username);
                                    MessageReceived(msg);
                                }catch (Exception e) { }
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            queue.add(stringRequest);
        } catch(Exception e) { }
    }


    public void MessageReceived(final Message message) {

        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.add(message);
                    messagesView.setSelection(messagesView.getCount() - 1);
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
        }
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
            finish();
        }
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
    public void MessageReceivedThroughBroadcastManager(String channel, String type, String message) {

        //System.out.println("--- [MAP ACTIVITY] --- " +message);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                requestNewMessages();
            }
        });
    }

    @Override
    public void ErrorAtBroadcastManager(Exception error) {

    }
}
