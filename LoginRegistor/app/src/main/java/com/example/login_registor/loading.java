package com.example.login_registor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class loading extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);


        Runnable basic = () ->
        {
            try {
                Thread.sleep(1000);


                Intent im = new Intent(getApplicationContext(),Register.class);
                startActivity(im);
                finish();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        Thread thr = new Thread(basic);
        thr.start();
    }
}