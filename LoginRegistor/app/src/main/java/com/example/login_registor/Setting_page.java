package com.example.login_registor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Setting_page extends AppCompatActivity {


    CheckBox  uni_opt,mon_opt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);

        FirebaseFirestore db = FirebaseFirestore.getInstance();



        uni_opt = findViewById(R.id.UNITS_OPT);
        mon_opt = findViewById(R.id.MONEY_OPT);

        TextView te = findViewById(R.id.back);

        te.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent im = new Intent(getApplicationContext(), Main_Page.class);
                startActivity(im);
                finish();
            }
        });
        TextView de = findViewById(R.id.demo);
        Button upd  = findViewById(R.id.update);
        TextInputLayout uni = findViewById(R.id.in_units);
        TextInputLayout mon = findViewById(R.id.in_money);


        TextInputEditText s1 = findViewById(R.id.Units);
        TextInputEditText s2 = findViewById(R.id.Money);

        uni_opt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!uni_opt.isChecked()){
                    de.setVisibility(View.GONE);
                    uni.setVisibility(View.GONE);
                    mon.setVisibility(View.GONE);
                    upd.setVisibility(View.GONE);
                }else {
                    de.setVisibility(View.VISIBLE);
                    if (mon_opt.isChecked()) {
                        mon_opt.setChecked(false);
                    }
                    uni.setVisibility(View.VISIBLE);
                    mon.setVisibility(View.GONE);
                    upd.setVisibility(View.VISIBLE);
                }
            }
        });
        mon_opt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mon_opt.isChecked()){
                    de.setVisibility(View.GONE);
                    mon.setVisibility(View.GONE);
                    uni.setVisibility(View.GONE);
                    upd.setVisibility(View.GONE);
                }else {
                    de.setVisibility(View.VISIBLE);
                    if (uni_opt.isChecked()) {
                        uni_opt.setChecked(false);
                    }
                    mon.setVisibility(View.VISIBLE);
                    uni.setVisibility(View.GONE);
                    upd.setVisibility(View.VISIBLE);
                }
            }
        });
        upd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String units,money;
                units = String.valueOf(s1.getText());
                money = String.valueOf(s2.getText());
                if(TextUtils.isEmpty(units) && TextUtils.isEmpty(money)){
                    Toast.makeText(Setting_page.this,"Empty",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    db.collection("Limits").document("Fields")
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Setting_page.this, "Done", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Setting_page.this, "Not", Toast.LENGTH_SHORT).show();
                                }
                            });

//                    ////////
                    CollectionReference limi = db.collection("Limits");
                    Map<String, Number> data1 = new HashMap<>();
                    if(units.length()!=0){
                        data1.put("Lts",12000);
                        data1.put("Units",Integer.parseInt(units));
                        double amount=0.0;
                        int Total = Integer.parseInt(units);
                        if(Total<=500){
                            if(Total>=0 && Total<=100){
                                amount=0.0;
                            }else if(Total>=101 && Total<=200){
                                amount = (100*0)+((Total-100)*2.25);
                            }else if(Total>=201 && Total<=400){
                                amount = (100*0)+(100*2.25)+((Total-200)*4.5);
                            }else{
                                amount = (100*0)+(100*2.25)+(200*4.5)+((Total-400)*6);
                            }
                        }else{
                            if(Total>=0 && Total<=100){
                                amount=0.0;
                            }else if(Total>=101 && Total<=400){
                                amount = (100*0)+((Total-100)*4.5);
                            }else if(Total>=401 && Total<=500){
                                amount = (100*0)+(300*4.5)+((Total-400)*6);
                            }else if(Total>=501 && Total<=600){
                                amount = (100*0)+(300*4.5)+(100*6)+((Total-500)*8);
                            } else if (Total>=601 && Total<=800) {
                                amount = (100*0)+(300*4.5)+(100*6)+(100*8)+((Total-600)*9);
                            } else if (Total>=801 && Total<=1000) {
                                amount = (100*0)+(300*4.5)+(100*6)+(100*8)+((200)*9)+((Total-800)*10);
                            }else{
                                amount = (100*0)+(300*4.5)+(100*6)+(100*8)+((200)*9)+(200*10)+((Total-1000)*11);
                            }
                        }
                        data1.put("Bill",((int) Math.round(amount)));
                        limi.document("Fields").set(data1);

                        Intent im = new Intent(getApplicationContext(),Main_Page.class);
                        startActivity(im);
                        finish();
                    }else{
                        data1.put("Lts",12000);
                        data1.put("Bill",Integer.parseInt(money));
                        double total=0.0;
                        double amount = Double.parseDouble(money);
                        if (amount == 0) {
                            total = 0;  // No charge
                        } else if (amount <= 100 * 4.5) {
                            total = amount / 4.5;
                        } else if (amount <= (100 * 4.5 + 300 * 6)) {
                            total = 100 + (amount - 100 * 4.5) / 6;
                        } else if (amount <= (100 * 4.5 + 300 * 6 + 100 * 8)) {
                            total = 500 + (amount - (100 * 4.5 + 300 * 6)) / 8;
                        } else if (amount <= (100 * 4.5 + 300 * 6 + 100 * 8 + 100 * 9)) {
                            total = 600 + (amount - (100 * 4.5 + 300 * 6 + 100 * 8)) / 9;
                        } else if (amount <= (100 * 4.5 + 300 * 6 + 100 * 8 + 200 * 9)) {
                            total = 800 + (amount - (100 * 4.5 + 300 * 6 + 100 * 8 + 100 * 9)) / 10;
                        } else {
                            total = 1000 + (amount - (100 * 4.5 + 300 * 6 + 100 * 8 + 200 * 9)) / 11;
                        }
                        data1.put("Units",((int)Math.round(total)));
                        limi.document("Fields").set(data1);

                        Intent im = new Intent(getApplicationContext(),Main_Page.class);
                        startActivity(im);
                        finish();
                    }
                    ////
                }
            }
        });
    }
}