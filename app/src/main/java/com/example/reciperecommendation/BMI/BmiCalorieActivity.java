package com.example.reciperecommendation.BMI;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.reciperecommendation.R;
import java.text.DecimalFormat;

public class BmiCalorieActivity extends AppCompatActivity {

    private EditText weight, height, age, waist;
    private RadioGroup genderGroup;
    private Spinner activityLevel, goal;
    private Button calculate;
    private TextView result;

    private final String[] activityOptions = {"Sedentary", "Lightly Active", "Moderately Active", "Very Active"};
    private final String[] goalOptions = {"Lose Weight", "Maintain Weight", "Gain Muscle"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bmi_layout);

        // Initialize UI Elements
        weight = findViewById(R.id.weight);
        height = findViewById(R.id.height);
        age = findViewById(R.id.age);
        waist = findViewById(R.id.waist);
        genderGroup = findViewById(R.id.genderGroup);
        activityLevel = findViewById(R.id.activityLevel);
        goal = findViewById(R.id.goal);
        calculate = findViewById(R.id.calculate);
        result = findViewById(R.id.result);

        // Set up Spinners
        ArrayAdapter<String> activityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, activityOptions);
        activityLevel.setAdapter(activityAdapter);

        ArrayAdapter<String> goalAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, goalOptions);
        goal.setAdapter(goalAdapter);

        // Button Click Listener
        calculate.setOnClickListener(view -> calculateBmiAndCalories());
    }

    private void calculateBmiAndCalories() {
        String weightText = weight.getText().toString();
        String heightText = height.getText().toString();
        String ageText = age.getText().toString();
        String waistText = waist.getText().toString();

        if (weightText.isEmpty() || heightText.isEmpty() || ageText.isEmpty() || waistText.isEmpty()) {
            Toast.makeText(this, "Please enter all values", Toast.LENGTH_SHORT).show();
            return;
        }

        float weightValue = Float.parseFloat(weightText);
        float heightValue = Float.parseFloat(heightText) / 100; // Convert cm to meters
        int ageValue = Integer.parseInt(ageText);
        float waistValue = Float.parseFloat(waistText);

        // Get selected gender
        int genderId = genderGroup.getCheckedRadioButtonId();
        boolean isMale = genderId == R.id.male;
        String genderStr = isMale ? "Male" : "Female";

        // Get activity level & goal
        String activity = activityLevel.getSelectedItem().toString();
        String userGoal = goal.getSelectedItem().toString();

        // Calculate BMI
        float bmi = weightValue / (heightValue * heightValue);
        String bmiCategory = getBmiCategory(bmi);

        // Calculate Calories
        float bmr = isMale
                ? (10 * weightValue) + (6.25f * heightValue * 100) - (5 * ageValue) + 5
                : (10 * weightValue) + (6.25f * heightValue * 100) - (5 * ageValue) - 161;

        float activityMultiplier = getActivityMultiplier(activity);
        float calories = bmr * activityMultiplier;

        if (userGoal.equals("Lose Weight")) {
            calories -= 500;
        } else if (userGoal.equals("Gain Muscle")) {
            calories += 500;
        }

        // Additional metrics
        double bodyFat = (1.20 * bmi) + (0.23 * ageValue) - (10.8 * (isMale ? 1 : 0)) - 5.4;

        String healthRisk = (waistValue > 102 && isMale) || (waistValue > 88 && !isMale)
                ? "High Risk (Abdominal Fat)" : "Normal Risk";

        // New metric: Basal Hydration Estimate (liters/day)
        double hydration = isMale
                ? (35 * weightValue) / 1000.0
                : (31 * weightValue) / 1000.0;

        // LLean body mass
        double lbm = isMale
                ? (0.407 * weightValue) + (0.267 * heightValue * 100) - 19.2
                : (0.252 * weightValue) + (0.473 * heightValue * 100) - 48.3;


        DecimalFormat df = new DecimalFormat("#.##");
        result.setText(
                "BMI: " + df.format(bmi) + " (" + bmiCategory + ")\n" +
                        "Calories: " + df.format(calories) + " kcal\n" +
                        "Estimated Body Fat: " + df.format(bodyFat) + "%\n" +
                        "Health Risk: " + healthRisk + "\n" +
                        "Recommended Daily Water Intake: " + df.format(hydration) + " liters" + "\n" +
                        "Lean Body Mass: " + df.format(lbm) + " kg\n"

        );
    }

    private String getBmiCategory(float bmi) {
        if (bmi < 18.5) return "Underweight";
        else if (bmi < 24.9) return "Normal";
        else if (bmi < 29.9) return "Overweight";
        else return "Obese";
    }

    private float getActivityMultiplier(String activity) {
        switch (activity) {
            case "Sedentary": return 1.2f;
            case "Lightly Active": return 1.375f;
            case "Moderately Active": return 1.55f;
            case "Very Active": return 1.725f;
            default: return 1.2f;
        }
    }
}
