package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
//        hideSystemUI();

        ((Button)findViewById(R.id.login_button)).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        doLogIn();

                        ((EditText) findViewById(R.id.login_input_password)).getText().clear();
                    }
                });

        ((Button)findViewById(R.id.go_to_register_button)).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intetToBecalled=new
                                Intent(getApplicationContext(),
                                SignUpActivity.class);
                        startActivity(intetToBecalled);
                    }
                });

        ((FloatingActionButton)findViewById(R.id.conf_button)).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intetToBecalled=new
                                Intent(getApplicationContext(),
                                SettingsActivity.class);

                        startActivity(intetToBecalled);
                    }
                });
    }

    private void doLogIn() {

        String username = ((TextView)findViewById(R.id.login_input_username)).getText().toString();
        String password = ((TextView)findViewById(R.id.login_input_password)).getText().toString();
        String ip = Settings.getIPAddress(true);

        try {
            RequestQueue queue = Volley.newRequestQueue(this);

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