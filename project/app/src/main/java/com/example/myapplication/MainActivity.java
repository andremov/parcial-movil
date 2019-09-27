package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // CUANDO QUIERE HACER LOG IN
        ((Button)findViewById(R.id.login_button)).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(MainActivity.this, LogInActivity.class));
                    }
                });

        // CUANDO QUIERE HACER SIGN UP
        ((Button)findViewById(R.id.signup_button)).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(MainActivity.this, SignUpActivity.class));
                    }
                });
    }
}
