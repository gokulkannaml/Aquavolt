package com.example.login_registor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Map;

public class Water extends Fragment {

    private double kit = 0, wash = 0, Lts = 0, other = 0, day = 0;
    private double totalKit = 0, totalWash = 0, totalOth = 0;
    private ProgressBar kitchenProgressBar, washroomProgressBar, otherProgressBar, totalMonthProgressBar;

    public Water() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_water, container, false);

        // Initialize your progress bars
        kitchenProgressBar = rootView.findViewById(R.id.Ki);
        washroomProgressBar = rootView.findViewById(R.id.Wash);
        otherProgressBar = rootView.findViewById(R.id.other);
//        totalMonthProgressBar = rootView.findViewById(R.id.Total_month);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Fetch limits and calculate totals
        DocumentReference limitsRef = db.collection("Limits").document("Fields");
        limitsRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot limitDoc = task.getResult();
                    if (limitDoc.exists()) {
                        Map<String, Object> limitData = limitDoc.getData();
                        Lts = Double.parseDouble(limitData.get("Lts").toString());
                        totalKit = Lts * 0.4;
                        totalWash = Lts * 0.4;
                        totalOth = Lts * 0.2;
                        Toast.makeText(getContext(), "" + Lts, Toast.LENGTH_SHORT).show();
                        fetchAndDisplayWaterUsages(db, rootView);
                    }
                }
            }
        });

        return rootView;
    }

    private void fetchAndDisplayWaterUsages(FirebaseFirestore db, View rootView) {
        fetchAndDisplayWaterUsage(db, "Kitchen", kitchenProgressBar, R.id.a1, kit, totalKit, rootView);
        fetchAndDisplayWaterUsage(db, "Washroom", washroomProgressBar, R.id.a2, wash, totalWash, rootView);
        fetchAndDisplayWaterUsage(db, "Others", otherProgressBar, R.id.a3, other, totalOth, rootView);
//        fetchAndDisplayWaterUsage(db, "Values", totalMonthProgressBar, R.id.a5, day, Lts, rootView);
    }

    private void fetchAndDisplayWaterUsage(FirebaseFirestore db, String collectionName, ProgressBar progressBar, int textViewId, double usage, double total, View rootView) {
        final double[] finalUsage = {usage};
        DocumentReference usageRef = db.collection("Litre").document(collectionName);
        usageRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot usageDoc = task.getResult();
                    if (usageDoc.exists()) {
                        finalUsage[0] = 0; // Modify finalUsage here if needed
                        Map<String, Object> usageData = usageDoc.getData();
                        for (Map.Entry<String, Object> entry : usageData.entrySet()) {
                            finalUsage[0] += Double.parseDouble(entry.getValue().toString());
                        }
                    }
                    TextView textView = rootView.findViewById(textViewId);
                    textView.setText(finalUsage[0] + " Lts");
                    int progress = (int) ((finalUsage[0] / total) * 100);
                    progressBar.setProgress(progress);
                }
            }
        });
    }
}
