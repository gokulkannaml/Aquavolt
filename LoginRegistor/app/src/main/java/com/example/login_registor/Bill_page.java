package com.example.login_registor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Bill_page extends AppCompatActivity
        implements BottomNavigationView
        .OnNavigationItemSelectedListener


{
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_page);



        bottomNavigationView = findViewById(R.id.Bill_Nav);
        bottomNavigationView
                .setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.E_bill);

    }

    Elec_Bill_Frag ebillfrag = new Elec_Bill_Frag();
    Water_Bill_Frag waterBillFrag  = new Water_Bill_Frag();
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch ((item.toString())+""){
            case "Electric Bill":
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.Bill_layout,ebillfrag)
                        .commit();
                return true;
            case "Water Bill":
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.Bill_layout,waterBillFrag)
                        .commit();
                return true;
        }
        return false;
    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        return false;
//    }

//

//    public boolean
//    onNavigationItemSelected(@NonNull MenuItem item)
//    {
//
//        switch ((item.toString())+"") {
//
//
//
//            case "Electric Bill":
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.Bill_frame,eBillFrag)
//                        .commit();
//                return true;
//
//
//
//        }
//        return false;
//    }
}