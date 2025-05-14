package com.example.reciperecommendation.MealPlanningModule;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.reciperecommendation.Management.UserModelClass;
import com.example.reciperecommendation.R;
import com.example.reciperecommendation.Utils.DateUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MealAdapter adapter;
    String Status = "";
    private List<Meal> mealList = new ArrayList<>();
    private Button dailyBtn, monthlyBtn, yearlyBtn;
    private static final String API_KEY = "44479338b9954d4ca340b984102073ae";
    DatabaseReference databaseReference;
    boolean isveg = false;
    String preference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);
        databaseReference = FirebaseDatabase.getInstance().getReference("users1");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                isveg = false;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);
                    if (userModelClass.getUUID().equals(FirebaseAuth.getInstance().getUid())) {
                        preference = userModelClass.getDietary_preferences();
                    }
                }
                if (preference.toLowerCase().trim().equals("Vegetarian".toLowerCase().trim())) {
                    isveg = true;

                } else {
                    isveg = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


//        List<Meal> mealList1adapter1 = new ArrayList<>();
//        DatabaseReference ShowMealRef = FirebaseDatabase.getInstance().getReference("mealdaily").child(FirebaseAuth.getInstance().getUid());
//        ShowMealRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                mealList1adapter1.clear();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    Meal meal = dataSnapshot.getValue(Meal.class);
//                    mealList1adapter1.add(meal);
//                }
//                MealAdapter mealAdapter = new MealAdapter(MealActivity.this, mealList1adapter1, null);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });


        dailyBtn = findViewById(R.id.dailyBtn);
        monthlyBtn = findViewById(R.id.monthlyBtn);
        yearlyBtn = findViewById(R.id.yearlyBtn);
        adapter = new MealAdapter(this, mealList, meal -> {
            mealList.remove(meal);
            fetchMealNutrition(meal);
//            Toast.makeText(this, "default adapter call", Toast.LENGTH_SHORT).show();

            adapter.notifyDataSetChanged();
            Toast.makeText(MealActivity.this, meal.getTitle() + " consumed!", Toast.LENGTH_SHORT).show();
        });
        DatabaseReference ShowMealRef = FirebaseDatabase.getInstance().getReference("mealdaily").child(FirebaseAuth.getInstance().getUid());
        List<Meal> mealList1adapter = new ArrayList<>();
        ShowMealRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mealList1adapter.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Meal meal = dataSnapshot.getValue(Meal.class);
                    mealList1adapter.add(meal);
                }
                MealAdapter mealAdapter = new MealAdapter(MealActivity.this, mealList1adapter, meal -> {
                    mealList.remove(meal);
                    Status = "daily";
//                    Toast.makeText(MealActivity.this, "call database ref", Toast.LENGTH_SHORT).show();
                    fetchMealNutrition(meal);
                    adapter.notifyDataSetChanged();
                    ShowMealRef.child(meal.getId()+"").removeValue();
                    Toast.makeText(MealActivity.this, meal.getTitle() + " consumed!", Toast.LENGTH_SHORT).show();
                });
                recyclerView.setLayoutManager(new LinearLayoutManager(MealActivity.this));
                recyclerView.setAdapter(mealAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        recyclerView.setAdapter(adapter);
        dailyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Meal> meals = fetchMealsDaily(isveg, "daily");
                MealAdapter.count = 0;
            }
        });

        monthlyBtn.setOnClickListener(v -> fetchMeals(isveg, "weekly"));
        yearlyBtn.setOnClickListener(v -> fetchMeals(isveg, "monthly"));

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        swipeRefreshLayout.setOnRefreshListener(() -> {
//            fetchMeals(isveg, Status);



            swipeRefreshLayout.setRefreshing(false);
        });

    }

    private List<Meal> fetchMealsDaily(boolean isVegetarian, String str) {

        DatabaseReference ShowMealRef = FirebaseDatabase.getInstance().getReference("mealdaily").child(FirebaseAuth.getInstance().getUid());

        Status = str;
        int number = 0;
        if (str.equals("daily")) {
            number = 3;
            adapter.setStatus("daily");

        }
        if (str.equals("weekly")) {
            number = 21;
            adapter.setStatus("weekly");

        }

        if (str.equals("monthly")) {
            number = 21;
            adapter.setStatus("montly");
        }


        Status = str;

        String diet = isVegetarian ? "vegetarian" : null;

        if (isVegetarian) {
            String excludeIngredients = isVegetarian ? null : "tofu,broccoli,lentils";

            excludeIngredients = isVegetarian ? "chicken,beef,pork,fish,shrimp,egg,ribs,bacon,lamb,turkey,ham,duck,goose,tuna" : null; // Exclude all meat
            RetrofitClient.getApi().getFilteredMeals(API_KEY, diet, number)
                    .enqueue(new Callback<MealResponse>() {
                        @Override
                        public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                List<Meal> meals = response.body().getRecipes();

                                if (meals != null && !meals.isEmpty()) {
                                    mealList.clear();
                                    mealList.addAll(meals);
                                    recyclerView.setAdapter(adapter);
                                    ShowMealRef.removeValue();
                                    for (Meal meal : meals) {
                                        ShowMealRef.child(meal.getId() + "").setValue(meal);
                                        Toast.makeText(MealActivity.this, "saved", Toast.LENGTH_SHORT).show();
                                    }
                                    adapter.notifyDataSetChanged();
                                    Log.d("API_SUCCESS", "Fetched " + meals.size() + " meals.");
                                } else {
                                    Log.e("API_ERROR", "No recipes found in response.");
                                    Toast.makeText(MealActivity.this, "No meals found.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.e("API_ERROR", "Response Code: " + response.code() + " | Message: " + response.message());
                                Toast.makeText(MealActivity.this, "Failed to fetch meals. Code: " + response.code(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MealResponse> call, Throwable t) {
                            Log.e("API_ERROR", "Request Failed: " + t.getMessage(), t);
                            Toast.makeText(MealActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            RetrofitClient.getApi().getFilteredMeals(API_KEY, diet, number)
                    .enqueue(new Callback<MealResponse>() {
                        @Override
                        public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                List<Meal> meals = response.body().getRecipes();

                                if (meals != null && !meals.isEmpty()) {
                                    mealList.clear();
                                    mealList.addAll(meals);
                                    recyclerView.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                    for (Meal meal : meals) {
                                        ShowMealRef.child(meal.getId() + "").setValue(meal);
                                        Toast.makeText(MealActivity.this, "saved", Toast.LENGTH_SHORT).show();
                                    }

                                    Log.d("API_SUCCESS", "Fetched " + meals.size() + " meals.");
                                } else {
                                    Log.e("API_ERROR", "No recipes found in response.");
                                    Toast.makeText(MealActivity.this, "No meals found.", Toast.LENGTH_SHORT).show();
                                }


                            } else {
                                Log.e("API_ERROR", "Response Code: " + response.code() + " | Message: " + response.message());
                                Toast.makeText(MealActivity.this, "Failed to fetch meals. Code: " + response.code(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MealResponse> call, Throwable t) {
                            Log.e("API_ERROR", "Request Failed: " + t.getMessage(), t);
                            Toast.makeText(MealActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }

        return null;
    }

    private void fetchMeals(String str) {

        Status = str;
        RetrofitClient.getApi().getRandomMeals(3, API_KEY).enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mealList.clear();
                    mealList.addAll(response.body().getRecipes());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MealActivity.this, "Failed to fetch meals", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable t) {
                Toast.makeText(MealActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchMealNutrition(Meal meal) {
        RetrofitClient.getApi().getMealNutrition(meal.getId(), API_KEY).enqueue(new Callback<MealNutrition>() {
            @Override
            public void onResponse(Call<MealNutrition> call, Response<MealNutrition> response) {
                if (response.isSuccessful() && response.body() != null) {
                    saveToFirebase(meal, response.body());
                } else {
                    Toast.makeText(MealActivity.this, "Failed to fetch nutrition details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MealNutrition> call, Throwable t) {
                Toast.makeText(MealActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveToFirebase(Meal meal, MealNutrition nutrition) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ConsumedMeals");
        String mealId = databaseReference.push().getKey();
        if (mealId != null) {


            MealPlanDetails mealCombine = new MealPlanDetails(mealId, meal.getTitle(), meal.getImage(), nutrition.getCalories(), nutrition.getCarbs(), nutrition.getFat()
                    , nutrition.getProtein(), nutrition.getBad(), nutrition.getGood(), Status, FirebaseAuth.getInstance().getUid(), DateUtils.getCurrentDate(), meal.getId());
            databaseReference.child(mealId).setValue(mealCombine)
                    .addOnSuccessListener(aVoid -> {
                        mealList.remove(meal);
                        adapter.notifyDataSetChanged();

                        Toast.makeText(MealActivity.this, meal.getTitle() + " consumed & saved!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Toast.makeText(MealActivity.this, "Failed to save meal", Toast.LENGTH_SHORT).show());
        }
    }

    private void fetchMeals(boolean isVegetarian, String str) {
        Status = str;
        int number = 0;
        if (str.equals("daily")) {
            number = 3;
            adapter.setStatus("daily");

        }
        if (str.equals("weekly")) {
            number = 21;
            adapter.setStatus("weekly");

        }

        if (str.equals("monthly")) {
            number = 21;
            adapter.setStatus("montly");
        }


        Status = str;

        String diet = isVegetarian ? "vegetarian" : null;

        if (isVegetarian) {
            String excludeIngredients = isVegetarian ? null : "tofu,broccoli,lentils";

            excludeIngredients = isVegetarian ? "chicken,beef,pork,fish,shrimp,egg,bacon,lamb,turkey,ham,duck,goose" : null; // Exclude all meat
            RetrofitClient.getApi().getFilteredMeals(API_KEY, diet, number)
                    .enqueue(new Callback<MealResponse>() {
                        @Override
                        public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                List<Meal> meals = response.body().getRecipes();

                                if (meals != null && !meals.isEmpty()) {
                                    mealList.clear();
                                    mealList.addAll(meals);
                                    recyclerView.setAdapter(adapter);

                                    adapter.notifyDataSetChanged();
                                    Log.d("API_SUCCESS", "Fetched " + meals.size() + " meals.");
                                } else {
                                    Log.e("API_ERROR", "No recipes found in response.");
                                    Toast.makeText(MealActivity.this, "No meals found.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.e("API_ERROR", "Response Code: " + response.code() + " | Message: " + response.message());
                                Toast.makeText(MealActivity.this, "Failed to fetch meals. Code: " + response.code(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MealResponse> call, Throwable t) {
                            Log.e("API_ERROR", "Request Failed: " + t.getMessage(), t);
                            Toast.makeText(MealActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            RetrofitClient.getApi().getFilteredMeals(API_KEY, diet, number)
                    .enqueue(new Callback<MealResponse>() {
                        @Override
                        public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                List<Meal> meals = response.body().getRecipes();

                                if (meals != null && !meals.isEmpty()) {
                                    mealList.clear();
                                    mealList.addAll(meals);
                                    recyclerView.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                    Log.d("API_SUCCESS", "Fetched " + meals.size() + " meals.");
                                } else {
                                    Log.e("API_ERROR", "No recipes found in response.");
                                    Toast.makeText(MealActivity.this, "No meals found.", Toast.LENGTH_SHORT).show();
                                }


                            } else {
                                Log.e("API_ERROR", "Response Code: " + response.code() + " | Message: " + response.message());
                                Toast.makeText(MealActivity.this, "Failed to fetch meals. Code: " + response.code(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MealResponse> call, Throwable t) {
                            Log.e("API_ERROR", "Request Failed: " + t.getMessage(), t);
                            Toast.makeText(MealActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }

    }


}