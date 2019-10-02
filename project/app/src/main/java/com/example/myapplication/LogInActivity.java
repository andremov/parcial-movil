package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.objects.ServerResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class LogInActivity extends AppCompatActivity {
    final String url ="http://192.168.0.10:8080/MovilAPI/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        ((Button)findViewById(R.id.login_button)).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*
                        Intent intetToBecalled=new
                                Intent(getApplicationContext(),
                                MainActivity.class);
                        intetToBecalled.putExtra("user_name",
                                ((EditText)findViewById(
                                        R.id.input_username)).getText().toString());
                        intetToBecalled.putExtra("user_password",
                                ((EditText)findViewById(
                                        R.id.input_password)).getText().toString());
                        startActivity(intetToBecalled);
                        */

                        httpRequestTest();

                        ((EditText) findViewById(R.id.input_username)).getText().clear();
                        ((EditText) findViewById(R.id.input_password)).getText().clear();
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
                                LaunchActivity.class);
                        startActivity(intetToBecalled);
                    }
                });
    }


    private void httpRequestTest() {
        try {
            RequestQueue queue = Volley.newRequestQueue(this);

            String username = ((TextView)findViewById(R.id.input_username)).getText().toString();
            String password = ((TextView)findViewById(R.id.input_password)).getText().toString();

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("data", password);


// Request a string response from the provided URL.
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url + "users/"+username, jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
//                        FORMA DE SACAR INFO DE PETICION
/*
                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.SSS").create();
                            ServerResponse responseJSON = gson.fromJson(response, ServerResponse.class);
                            String json = responseJSON.getData();
*/
                            Toast.makeText(LogInActivity.this, "success", Toast.LENGTH_LONG).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

// Add the request to the RequestQueue.
            queue.add(stringRequest);
        } catch(Exception e) {

        }
    }
}