package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.objects.ServerResponse;
import com.example.myapplication.objects.User;
import com.example.myapplication.utils.Settings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
//        hideSystemUI();

        ((Button)findViewById(R.id.register_button)).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        doSignUp();
                    }
                });

    }


    private void doSignUp() {
        /*
         {
              "newUser": {
                  "username": "test",
                  "first_name": "JOHNNY",
                  "last_name": "TEST",
                  "full_name": "JOHNNY TEST",
                  "email": "jtest@uninorte.edu.co",
                  "lastLat": 10.963889,
                  "lastLon": -74.796387,
                  "status": "online",
                  "lastSeen": "2019-09-29 06:21:32.54"
              },
              "pwd": "test",
              "pwdConfirmation": "test"
          }
         */

        final String username = ((TextView)findViewById(R.id.signup_input_username)).getText().toString();
        final String password = ((TextView)findViewById(R.id.signup_input_password)).getText().toString();
        String firstName = ((TextView)findViewById(R.id.signup_input_name)).getText().toString();
        String lastName = ((TextView)findViewById(R.id.signup_input_lastname)).getText().toString();
        String email = ((TextView)findViewById(R.id.signup_input_email)).getText().toString();
        String ip = Settings.getIPAddress(true);


        try {
            RequestQueue queue = Volley.newRequestQueue(this);

            JSONObject jsonBody = new JSONObject();

            JSONObject userBody = new JSONObject();
            userBody.put("username", username);
            userBody.put("first_name", firstName);
            userBody.put("last_name", lastName);
            userBody.put("full_name", firstName+" "+lastName);
            userBody.put("email", email);
            userBody.put("lastLat", 0.0);
            userBody.put("lastLon", 0.0);
            userBody.put("status", "offline");
            userBody.put("lastSeen", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));

            jsonBody.put("newUser", userBody);
            jsonBody.put("pwd", password);
            jsonBody.put("pwdConfirmation", password);

            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Settings.getUrlAPI() + "users", jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String res = response.toString();

                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.SSS").create();
                            ServerResponse responseJSON = gson.fromJson(res, ServerResponse.class);
                            String json = responseJSON.getData();

                            if(responseJSON.isSuccess()) {
                                doLogIn(username,password);
                            } else {
                                Toast.makeText(getApplicationContext(), "Usuario o correo en uso.", Toast.LENGTH_SHORT).show();
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

    private void doLogIn(String username, String password) {
        try {
            RequestQueue queue = Volley.newRequestQueue(this);

            String ip = Settings.getIPAddress(true);

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("first_value", password);
            jsonBody.put("last_value", ip);

            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Settings.getUrlAPI() + "users/"+username, jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String res = response.toString();

                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.SSS").create();
                            ServerResponse responseJSON = gson.fromJson(res, ServerResponse.class);
                            String json = responseJSON.getData();

                            if(responseJSON.isSuccess()) {
                                User user = gson.fromJson(json,User.class);

                                goToMapActivity(user);
                            } else {
                                Toast.makeText(getApplicationContext(), "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
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

    private void goToMapActivity(User user) {
        Intent intetToBecalled=new
                Intent(getApplicationContext(),
                MapActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("user_obj", user);

        intetToBecalled.putExtras(bundle);

        finish();

        startActivity(intetToBecalled);
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
