package com.example.reciperecommendation.MealPlanningModule;



import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reciperecommendation.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class GenerateIngrediants extends AppCompatActivity {
    private RecyclerView recyclerView;
    private IngredientAdapter adapter;
    private static final String API_KEY = "44479338b9954d4ca340b984102073ae";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generate_ingrediants );

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        int mealId = getIntent().getIntExtra("meal_id", 0);
        if (mealId > 0) {
            fetchIngredients(mealId);
        } else {
            Toast.makeText(this, "Invalid Meal ID", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchIngredients(int mealId) {

        SpoonacularApi apiService = RetrofitClient.getApi();

        Call<RecipeResponse> call = apiService.getRecipeInfo(mealId, false, API_KEY);

        call.enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Ingredient> ingredients = response.body().getIngredients();
                    adapter = new IngredientAdapter(ingredients);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(GenerateIngrediants.this, "Failed to get data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                Toast.makeText(GenerateIngrediants.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
