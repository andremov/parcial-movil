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
                                SettingsActivity.class);

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

            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Settings.getUrl() + "users/"+username, jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            String res = response.toString();

                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.SSS").create();
                            ServerResponse responseJSON = gson.fromJson(res, ServerResponse.class);
                            String json = responseJSON.getData();

                            if(responseJSON.isSuccess()) {
                                User user = gson.fromJson(json,User.class);
                              //  Toast.makeText(LogInActivity.this, "Welcome, "+user.getmFirst_name(), Toast.LENGTH_LONG).show();
                                goToMainActivity(user);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            queue.add(stringRequest);
        } catch(Exception e) { }
    }

    private void goToMainActivity(User user) {

        Intent intetToBecalled=new
                Intent(getApplicationContext(),
                MainActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("user_obj", user);

        intetToBecalled.putExtras(bundle);

        finish();

        startActivity(intetToBecalled);
    }
}