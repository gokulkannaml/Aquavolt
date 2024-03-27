package com.example.login_registor;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class Settings extends Fragment {

    public Settings(){

    }

    CheckBox uni_opt,mon_opt;

    TextInputLayout uni,mon;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        View root = inflater.inflate(R.layout.fragment_settings, container, false);



        uni_opt = root.findViewById(R.id.UNITS_OPT);
        mon_opt = root.findViewById(R.id.MONEY_OPT);


        TextView de = root.findViewById(R.id.demo);
        Button upd  = root.findViewById(R.id.update);
        TextInputLayout uni = root.findViewById(R.id.in_units);
        TextInputLayout mon = root.findViewById(R.id.in_money);


        TextInputEditText s1 = root.findViewById(R.id.Units);
        TextInputEditText s2 = root.findViewById(R.id.Money);


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
                    showToast("Empty");
                    return;
                }else{
                    db.collection("Limits").document("Fields")
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    showToast("Done");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    showToast("Not");
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
                        showToast("Changed");
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
                        showToast("Changed");

                    }
                }
            }
        });






















        return root;

    }

    private void showToast(String empty) {
        Toast.makeText(getContext(), ""+empty, Toast.LENGTH_SHORT).show();
    }
}

