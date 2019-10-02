package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.utils.Settings;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_settings);

        ((Button)findViewById(R.id.link_server)).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String ip = ((TextView)findViewById(R.id.server_ip_txt)).getText().toString();
                        String port = ((TextView)findViewById(R.id.server_port_txt)).getText().toString();
                        Settings.linkServer(ip, port);
                        
                        finish();
                    }
                });

        ((Button)findViewById(R.id.back_button_to_login)).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
    }
}
