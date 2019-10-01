package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        ((Button)findViewById(R.id.login_button)).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
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
}