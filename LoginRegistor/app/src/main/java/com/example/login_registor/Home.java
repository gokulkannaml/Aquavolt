package com.example.login_registor;


//import static androidx.core.app.AppOpsManagerCompat.Api29Impl.getSystemService;

//import static androidx.core.app.AppOpsManagerCompat.Api23Impl.getSystemService;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Home extends Fragment {
    public Home(){
        // require a empty public constructor

    }

    Button bill_btn;
    public double Total=0.0;
    public double units=0.0,bill=0.0,last=0.0 ;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        bill_btn = rootView.findViewById(R.id.button);




        bill_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent im = new Intent(getActivity(),Bill_page.class);
                startActivity(im);
            }
        });









        final String[] str = {""};
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        ProgressBar mon = rootView.findViewById(R.id.month);
        ProgressBar day = rootView.findViewById(R.id.perday);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Context context = getContext(); // Use this if you are in a Fragment

            // Example: Accessing the Location Service
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            NotificationChannel channel = new NotificationChannel("notification", "notification", NotificationManager.IMPORTANCE_DEFAULT);

            // Access NotificationManager using the context object
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // Create the notification channel
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        TextView un = rootView.findViewById(R.id.Month_total);





        DocumentReference docRef = db.collection("Units").document("Values");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> map  = document.getData();
                        last =0.0;
                        Total = 0.0;

                        int size = map.size();

                        for(int i=1;i<=size;i++){
                            double value = Double.parseDouble(""+document.get("Day "+i));
                            Total = Total  + value;
                            last = value;
                        }
//                        Toast.makeText(getContext(),""+map,/Toast.LENGTH_LONG).show();

                        DecimalFormat fo = new DecimalFormat("0.00");
                        un.setText(fo.format(Total)+" Units");
                    } else {

                    }
                } else {

                }
                DocumentReference limits = db.collection("Limits").document("Fields");
                limits.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.isSuccessful()){
                            DocumentSnapshot docu = task.getResult();
                            if(docu.exists()){
                                Map<String, Object> map = docu.getData();
                                bill=Integer.parseInt(map.get("Bill").toString());
                                units = Integer.parseInt(map.get("Units").toString());
                                Toast.makeText(getContext(), ""+units, Toast.LENGTH_SHORT).show();

                                TextView val = rootView.findViewById(R.id.Value);
                                val.setText(last+" Units");
                                int progress = (int)((Total/units)*100);




                                double perday =(units/30);
                                int pro_perday =(int)((last/perday)*100);
                                if(last>=perday){
                                    day.setProgressDrawable(getResources().getDrawable(R.drawable.circle_full));
                                }else {
                                    day.setProgress(pro_perday);
                                }


                                mon.setProgress(progress);





                                ///calu
                                TextView toto = rootView.findViewById(R.id.Total);
                                double amount=0.0;
                                if(Total<=500.0){
                                    if(Total>=.0 && Total<=100.0){
                                        amount=0.0;
                                    }else if(Total>=101.0 && Total<=200.0){
                                        amount = (100*0)+((Total-100)*2.25);
                                    }else if(Total>=201.0 && Total<=400.0){
                                        amount = (100*0)+(100*2.25)+((Total-200)*4.5);
                                    }else{
                                        amount = (100*0)+(100*2.25)+(200*4.5)+((Total-400)*6);
                                    }
                                }else{
                                    if(Total>=0.0 && Total<=100){
                                        amount=0.0;
                                    }else if(Total>=101.0 && Total<=400.0){
                                        amount = (100*0)+((Total-100)*4.5);
                                    }else if(Total>=401.0 && Total<=500.0){
                                        amount = (100*0)+(300*4.5)+((Total-400)*6);
                                    }else if(Total>=501.0 && Total<=600.0){
                                        amount = (100*0)+(300*4.5)+(100*6)+((Total-500)*8);
                                    } else if (Total>=601.0 && Total<=800.0) {
                                        amount = (100*0)+(300*4.5)+(100*6)+(100*8)+((Total-600)*9);
                                    } else if (Total>=801.0 && Total<=1000.0) {
                                        amount = (100*0)+(300*4.5)+(100*6)+(100*8)+((200)*9)+((Total-800)*10);
                                    }else{
                                        amount = (100*0)+(300*4.5)+(100*6)+(100*8)+((200)*9)+(200*10)+((Total-1000)*11);
                                    }
                                }
                                DecimalFormat fo = new DecimalFormat("0.00");
//                                Toast.makeText(getContext(),""+amount, Toast.LENGTH_LONG).show();
                                toto.setText("₹"+(fo.format(amount)));
                                ProgressBar Money = rootView.findViewById(R.id.amount);
                                int mone =(int) ((amount/bill)*100);
                                Money.setProgress(mone);
                                ///



                            }else{
                                Toast("1. Some Proble  m");
                            }
                        }else{
                            Toast("2.  Some Problem");
                        }
                    }


                });


            }
        });















        return rootView;

    }

    private void Toast(String someProblem) {
        Toast.makeText(getContext(), ""+someProblem, Toast.LENGTH_SHORT).show();
    }
}