package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ((Button)findViewById(R.id.signup_button)).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // AQUI SE HACE EL SIGN UP!

                        /*
                        Intent intentToBecalled=new
                                Intent(getApplicationContext(),
                                MainActivity.class);
                        intentToBecalled.putExtra("user_name",
                                ((EditText)findViewById(
                                        R.id.input_username)).getText().toString());
                        intentToBecalled.putExtra("user_password",
                                ((EditText)findViewById(
                                        R.id.input_password)).getText().toString());
                        startActivity(intentToBecalled);
                         */
                    }
                });

        ((Button)findViewById(R.id.back_button)).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
    }

}
