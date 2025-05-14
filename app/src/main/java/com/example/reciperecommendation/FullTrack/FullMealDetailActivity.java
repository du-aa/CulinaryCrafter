package com.example.reciperecommendation.FullTrack;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reciperecommendation.MealPlanningModule.MealPlanDetails;
import com.example.reciperecommendation.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class FullMealDetailActivity extends AppCompatActivity {

    private Button dailyBtn, monthlyBtn, yearlyBtn;
    String status = " ";
    private TextView mealTitle, caloriesText, carbsText, fatText, proteinText;
    private BarChart barChart;
    private DatabaseReference databaseReference;
    private double calories = 0, carbs = 0, fat = 0, protein = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_detail_result);

        mealTitle = findViewById(R.id.mealTitle);
        caloriesText = findViewById(R.id.caloriesText);
        carbsText = findViewById(R.id.carbsText);
        fatText = findViewById(R.id.fatText);
        proteinText = findViewById(R.id.proteinText);
        barChart = findViewById(R.id.barChart); // Update your layout to use BarChart

        dailyBtn = findViewById(R.id.dailyBtn);
        monthlyBtn = findViewById(R.id.monthlyBtn);
        yearlyBtn = findViewById(R.id.yearlyBtn);

        databaseReference = FirebaseDatabase.getInstance().getReference("ConsumedMeals");

        dailyBtn.setOnClickListener(v -> {
            resetValues();
            status = "daily";
            FetchFullDetail();
            mealTitle.setText(status);
        });

        monthlyBtn.setOnClickListener(v -> {
            resetValues();
            status = "weekly";
            FetchFullDetail();
            mealTitle.setText(status);
        });

        yearlyBtn.setOnClickListener(v -> {
            resetValues();
            status = "monthly";
            FetchFullDetail();
            mealTitle.setText(status);
        });

        FetchFullDetail();
    }

    private void resetValues() {
        calories = 0;
        carbs = 0;
        fat = 0;
        protein = 0;
    }

    private double extractNumber(String value) {
        if (value == null || value.isEmpty()) return 0.0;
        try {
            String numericValue = value.replaceAll("[^0-9.]", "");
            return numericValue.isEmpty() ? 0.0 : Double.parseDouble(numericValue);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private void FetchFullDetail() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MealPlanDetails mealPlanDetails = dataSnapshot.getValue(MealPlanDetails.class);
                    if (FirebaseAuth.getInstance().getUid().equals(mealPlanDetails.getUUID())
                            && mealPlanDetails.getStatus().equals(status)) {
                        calories += extractNumber(mealPlanDetails.getCalories());
                        carbs += extractNumber(mealPlanDetails.getCarbs());
                        fat += extractNumber(mealPlanDetails.getFat());
                        protein += extractNumber(mealPlanDetails.getProtein()); // Fix protein here
                    }
                }
                updateNutrientInfo();
                setupBarChart();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void updateNutrientInfo() {
        caloriesText.setText("Calories: " + calories + " kcal");
        carbsText.setText("Carbs: " + carbs + " g");
        fatText.setText("Fat: " + fat + " g");
        proteinText.setText("Protein: " + protein + " g");
    }

    private void setupBarChart() {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, (float) calories));
        entries.add(new BarEntry(1f, (float) carbs));
        entries.add(new BarEntry(2f, (float) fat));
        entries.add(new BarEntry(3f, (float) protein));

        BarDataSet dataSet = new BarDataSet(entries, "Nutrient Breakdown");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(14f);
        dataSet.setValueTextColor(Color.BLACK);

        BarData data = new BarData(dataSet);
        barChart.setData(data);

        String[] labels = {"Calories", "Carbs", "Fat", "Protein"};
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);

        barChart.getAxisRight().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.animateY(1000);
        barChart.invalidate();
    }
}
