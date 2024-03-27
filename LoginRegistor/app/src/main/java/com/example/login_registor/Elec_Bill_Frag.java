package com.example.login_registor;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Map;

public class Elec_Bill_Frag extends Fragment {

    private  double bill =0.0;
    private  double units =0;
    public DecimalFormat Format = new DecimalFormat("0.00");
    public Elec_Bill_Frag() {
        // Required empty public constructor
    }

    double Total=0.0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_elec__bill_, container, false);
        GraphView graphView = rootView.findViewById(R.id.Elec_Bill_graph);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection("Units").document("Values");

        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> map = document.getData();

                        // Create an array to store DataPoint objects
                        DataPoint[] dataPoints = new DataPoint[map.size()];

                        int day = 1;
                        int index = 0;
                        Total=0;
                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                            double value = Double.parseDouble(entry.getValue().toString());
                            Total = Total + value;
                            dataPoints[index] = new DataPoint(day, value);
                            day++;
                            index++;
                        }

                        TextView tototot = rootView.findViewById(R.id.Tot_uni);

                        tototot.setText(tototot.getText()+"  "+Format.format(Total)+" Units");
                        // Create the LineGraphSeries with the DataPoint array
                        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);

                        graphView.setTitle("Electric Usage Graph");
                        graphView.setTitleColor(R.color.black);
                        graphView.setTitleTextSize(20);

                        // Add the series to the graph
                        graphView.addSeries(series);

                        DocumentReference limits = db.collection("Limits").document("Fields");
                        limits.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                                if (task.isSuccessful()) {
                                    DocumentSnapshot docu = task.getResult();
                                    if (docu.exists()) {
                                        Map<String, Object> map = docu.getData();
                                        bill = Integer.parseInt(map.get("Bill").toString());
                                        units = Integer.parseInt(map.get("Units").toString());
//                                Toast.makeText(Main_Page.this, ""+units, Toast.LENGTH_SHORT).show();
                                    }


                                    ///calu
                                    TextView toto = rootView.findViewById(R.id.Amount);
                                    double amount = 0;
                                    if (Total <= 500) {
                                        if (Total >= 0 && Total <= 100) {
                                            amount = 0.0;
                                        } else if (Total >= 101 && Total <= 200) {
                                            amount = (100 * 0) + ((Total - 100) * 2.25);
                                        } else if (Total >= 201 && Total <= 400) {
                                            amount = (100 * 0) + (100 * 2.25) + ((Total - 200) * 4.5);
                                        } else {
                                            amount = (100 * 0) + (100 * 2.25) + (200 * 4.5) + ((Total - 400) * 6);
                                        }
                                    } else {
                                        if (Total >= 0 && Total <= 100) {
                                            amount = 0.0;
                                        } else if (Total >= 101 && Total <= 400) {
                                            amount = (100 * 0) + ((Total - 100) * 4.5);
                                        } else if (Total >= 401 && Total <= 500) {
                                            amount = (100 * 0) + (300 * 4.5) + ((Total - 400) * 6);
                                        } else if (Total >= 501 && Total <= 600) {
                                            amount = (100 * 0) + (300 * 4.5) + (100 * 6) + ((Total - 500) * 8);
                                        } else if (Total >= 601 && Total <= 800) {
                                            amount = (100 * 0) + (300 * 4.5) + (100 * 6) + (100 * 8) + ((Total - 600) * 9);
                                        } else if (Total >= 801 && Total <= 1000) {
                                            amount = (100 * 0) + (300 * 4.5) + (100 * 6) + (100 * 8) + ((200) * 9) + ((Total - 800) * 10);
                                        } else {
                                            amount = (100 * 0) + (300 * 4.5) + (100 * 6) + (100 * 8) + ((200) * 9) + (200 * 10) + ((Total - 1000) * 11);
                                        }
                                    }

                                    toto.setText("₹" + Format.format(amount));
                                    ProgressBar Money = rootView.findViewById(R.id.Prog_Total);
                                    int mone =(int) ((amount/bill)*100);
                                    Money.setProgress(mone);

                                    TextView fina = rootView.findViewById(R.id.Final_amm);
                                    fina.setText(fina.getText()+""+Format.format(amount+30));
                                } else {
                                    Toast.makeText(getContext(), "Some Problem", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                        );
                    }
                }
            }
        });

        return rootView;
    }
}
