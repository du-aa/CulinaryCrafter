package com.example.reciperecommendation.Result;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reciperecommendation.MealPlanningModule.MealPlanDetails;
import com.example.reciperecommendation.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WeeklyMealDetailActivity extends AppCompatActivity {

    private TextView mealTitle, caloriesText, carbsText, fatText, proteinText;
    private BarChart barChart;
    private DatabaseReference databaseReference;
    private String mealId;
    private double calories = 0, carbs = 0, fat = 0, protein = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_meal_detail);

        mealTitle = findViewById(R.id.mealTitle);
        caloriesText = findViewById(R.id.caloriesText);
        carbsText = findViewById(R.id.carbsText);
        fatText = findViewById(R.id.fatText);
        proteinText = findViewById(R.id.proteinText);
        barChart = findViewById(R.id.barChart); // Update XML ID

        mealId = getIntent().getStringExtra("mealId");
        mealTitle.setText(getIntent().getStringExtra("mealName") + "" +
                "\n" + getIntent().getStringExtra("Status"));

        databaseReference = FirebaseDatabase.getInstance().getReference("ConsumedMeals");

        fetchWeeklyData();
    }

    private double extractNumber(String value) {
        if (value == null || value.isEmpty()) {
            return 0.0;
        }
        try {
            String numericValue = value.replaceAll("[^0-9.]", "");
            return numericValue.isEmpty() ? 0.0 : Double.parseDouble(numericValue);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private void fetchWeeklyData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MealPlanDetails mealPlanDetails = dataSnapshot.getValue(MealPlanDetails.class);
                    if (FirebaseAuth.getInstance().getUid().equals(mealPlanDetails.getUUID()) &&
                            mealPlanDetails.getTitle().equals(getIntent().getStringExtra("mealName"))) {
                        calories += extractNumber(mealPlanDetails.getCalories());
                        carbs += extractNumber(mealPlanDetails.getCarbs());
                        fat += extractNumber(mealPlanDetails.getFat());
                        protein += extractNumber(mealPlanDetails.getProtein());
                    }
                }
                updateNutrientInfo();
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
        setupBarChart();
    }

    private void setupBarChart() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, (float) calories));
        entries.add(new BarEntry(1f, (float) carbs));
        entries.add(new BarEntry(2f, (float) fat));
        entries.add(new BarEntry(3f, (float) protein));

        BarDataSet dataSet = new BarDataSet(entries, "Nutrients");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(14f);

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        String[] labels = new String[]{"Calories", "Carbs", "Fat", "Protein"};
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(12f);
        xAxis.setDrawGridLines(false);

        barChart.getAxisLeft().setTextSize(12f);
        barChart.getAxisRight().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.animateY(1000);
        barChart.invalidate();
    }
}
