package com.example.login_registor;


import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.login_registor.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.io.*;

public class Main_Page extends AppCompatActivity
        implements BottomNavigationView
        .OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        bottomNavigationView
                = findViewById(R.id.bottomNavigationView);

        bottomNavigationView
                .setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.Home);
    }
    Home home_fra = new Home();

    Profile Pro_fra = new Profile();
    Settings set_fra = new Settings();
    Water water_fra = new Water();

    @Override
    public boolean
    onNavigationItemSelected(@NonNull MenuItem item)
    {

        switch ((item.toString())+"") {



            case "Electricity":
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout,home_fra)
                        .commit();
                return true;

            case "Profile":
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, Pro_fra)
                        .commit();
                return true;

            case "Settings":
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout,set_fra)
                        .commit();
                return true;
            case "Water":
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout,water_fra)
                        .commit();
                return true;
        }
        return false;
    }

}
