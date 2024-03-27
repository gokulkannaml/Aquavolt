package com.example.login_registor;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Water_Bill_Frag extends Fragment {

    private int Total=0,wash=0,other=0;

    private Map<String, Integer> typeAmountMap = new HashMap<>();
    PieChart pieChart ;


    public Water_Bill_Frag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_water__bill_, container, false);



        pieChart =rootView.findViewById(R.id.pieChart);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference dr = db.collection("Litre").document("Kitchen");
        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
//                    Toast.makeText(getContext(),"Hello",Toast.LENGTH_LONG).show();

                    if (document.exists()) {
                        Map<String, Object> map = document.getData();
                        Total = 0;
                        for(Map.Entry<String,Object> i:map.entrySet()){
                            int value = Integer.parseInt(i.getValue().toString());
                            Total = Total + value;
//                            Toast.makeText(getContext(),""+Total,Toast.LENGTH_LONG).show();

                        }

                        DocumentReference dr1 = db.collection("Litre").document("Washroom");
                        dr1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()) {
                            Toast.makeText(getContext(),"sfvg",Toast.LENGTH_LONG).show();

                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Map<String, Object> map = document.getData();
                                        wash = 0;
                                        for (Map.Entry<String, Object> i : map.entrySet()) {
                                            int value = Integer.parseInt(i.getValue().toString());
                                            wash = wash + value;
//                            Toast.makeText(getContext(),""+wash,Toast.LENGTH_LONG).show();

                                        }
                                        DocumentReference dr2 = db.collection("Litre").document("Others");
                                        dr2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if(task.isSuccessful()){
                                                    DocumentSnapshot de = task.getResult();
                                                    if(de.exists()){
                                                        other=0;
                                                        Map<String,Object> map = de.getData();
                                                        for(Map.Entry<String,Object> i : map.entrySet()){
                                                            int a = Integer.parseInt(i.getValue().toString());
                                                            other = other + a;
                                                        }

                                                        showPieChart(pieChart,Total,wash,other);

                                                        TextView in = rootView.findViewById(R.id.TOTAL);
                                                        in.setText(in.getText()+" "+(Total+wash+other));
                                                    }
                                                }
                                            }
                                        });

                                    }
                                }
                            }
                        });

                    } else {

                    }
                }else{

                }
            }
        });

        return  rootView;
    }

    private void showPieChart(PieChart pieChart, int total,int wash,int oth) {
        Toast.makeText(getContext(),"Hello",Toast.LENGTH_LONG).show();
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        String label = "type";

        //initializing data
        Map<String, Integer> typeAmountMap = new HashMap<>();
        typeAmountMap.put("Kitchen",total);
        typeAmountMap.put("WashRoom",wash);
        typeAmountMap.put("Other",oth);




        //initializing colors for the entries
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#890567"));
        colors.add(Color.parseColor("#a35567"));
        colors.add(Color.parseColor("#ff5f67"));
        colors.add(Color.parseColor("#3ca567"));


        //input data and fit data into pie chart entry
        for(String type: typeAmountMap.keySet()){
            pieEntries.add(new PieEntry(typeAmountMap.get(type).floatValue(), type));
        }

        //collecting the entries with label name
        PieDataSet pieDataSet = new PieDataSet(pieEntries,label);
        //setting text size of the value
        pieDataSet.setValueTextSize(12f);
        //providing color list for coloring different entries
        pieDataSet.setColors(colors);
        //grouping the data set from entry to chart
        PieData pieData = new PieData(pieDataSet);
        //showing the value of the entries, default true if not set
        pieData.setDrawValues(true);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }



}
