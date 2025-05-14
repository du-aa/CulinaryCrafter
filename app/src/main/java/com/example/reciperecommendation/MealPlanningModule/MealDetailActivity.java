package com.example.reciperecommendation.MealPlanningModule;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.reciperecommendation.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MealDetailActivity extends AppCompatActivity {
    private ImageView mealImage;
    private TextView mealTitle, mealCalories, mealProtein, mealFat, mealCarbs;
    private TextView mealTime, mealLikes, mealSummary, mealIngredients, mealInstructions;

    private static final String API_KEY = "f7e1b1d2a23543818b66d6c23709ba38"; // same as Details.java

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_detail);

        // UI binding
        mealImage = findViewById(R.id.mealImage);
        mealTitle = findViewById(R.id.mealTitle);
        mealCalories = findViewById(R.id.mealCalories);
        mealProtein = findViewById(R.id.mealProtein);
        mealFat = findViewById(R.id.mealFat);
        mealCarbs = findViewById(R.id.mealCarbs);
        mealTime = findViewById(R.id.mealTime);
        mealLikes = findViewById(R.id.mealLikes);
        mealSummary = findViewById(R.id.mealSummary);
        mealIngredients = findViewById(R.id.mealIngredients);
        mealInstructions = findViewById(R.id.mealInstructions);

        // Intent data
        Intent intent = getIntent();
        int mealId = intent.getIntExtra("meal_id", -1);
        String title = intent.getStringExtra("meal_title");
        String image = intent.getStringExtra("meal_image");

        mealTitle.setText(title);
        Glide.with(this).load(image).into(mealImage);

        if (mealId != -1) {
            fetchMealNutrition(mealId);
            fetchRecipeDetails(mealId);
        } else {
            Toast.makeText(this, "Meal details not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchMealNutrition(int mealId) {
        RetrofitClient.getApi().getMealNutrition(mealId, API_KEY).enqueue(new retrofit2.Callback<MealNutrition>() {
            @Override
            public void onResponse(retrofit2.Call<MealNutrition> call, retrofit2.Response<MealNutrition> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MealNutrition nutrition = response.body();
                    mealCalories.setText("Calories: " + nutrition.getCalories());
                    mealProtein.setText("Protein: " + nutrition.getProtein());
                    mealFat.setText("Fat: " + nutrition.getFat());
                    mealCarbs.setText("Carbs: " + nutrition.getCarbs());
                } else {
                    Toast.makeText(MealDetailActivity.this, "Failed to fetch nutrition details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<MealNutrition> call, Throwable t) {
                Toast.makeText(MealDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchRecipeDetails(int mealId) {
        String url = "https://api.spoonacular.com/recipes/" + mealId + "/information?apiKey=" + API_KEY;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).get().build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(MealDetailActivity.this, "Recipe detail fetch failed", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String resp = response.body().string();
                    runOnUiThread(() -> {
                        try {
                            JSONObject jsonObject = new JSONObject(resp);
                            String ready = jsonObject.getString("readyInMinutes");
                            String likes = jsonObject.getString("aggregateLikes");
                            String summary = jsonObject.getString("summary");
                            String instructions = jsonObject.getString("instructions");

                            // Ingredients
                            JSONArray extendedIngredients = jsonObject.getJSONArray("extendedIngredients");
                            StringBuilder ingredientsList = new StringBuilder();
                            for (int i = 0; i < extendedIngredients.length(); i++) {
                                JSONObject ingredient = extendedIngredients.getJSONObject(i);
                                String original = ingredient.getString("original");
                                ingredientsList.append("• ").append(original).append("\n");
                            }

                            // Set data to views
                            mealTime.setText("Ready in: " + ready + " minutes");
                            mealLikes.setText("Likes: " + likes);
                            mealSummary.setText("Summary:\n" + summary.replaceAll("<.*?>", ""));
                            mealIngredients.setText("Ingredients:\n" + ingredientsList);
                            mealInstructions.setText("Instructions:\n" + instructions.replaceAll("<.*?>", ""));

                        } catch (JSONException e) {
                            Toast.makeText(MealDetailActivity.this, "Parse error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
