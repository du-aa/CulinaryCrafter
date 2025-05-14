package com.example.reciperecommendation.Api;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.reciperecommendation.Api.Model.All;
import com.example.reciperecommendation.Api.Model.Results;
import com.example.reciperecommendation.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity1 extends AppCompatActivity {
    final String apiKey = "f7e1b1d2a23543818b66d6c23709ba38";
    boolean isReversed = false;
    String preference = "";

    RecyclerView recyclerView;
    adapter adapter;
    List<Results> foods = new ArrayList<>();
    EditText editText;
    Button button;
    SwipeRefreshLayout swipeRefreshLayout;
    LottieAnimationView lottieAnimationView;

    private final List<String> nonVegKeywords = Arrays.asList("chicken", "beef", "lamb", "fish", "prawn", "mutton", "egg", "bacon", "ham", "shrimp", "meat");

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sort_low_high || id == R.id.sort_high_low) {
            sortListByCalories();
            return true;
        } else if (id == R.id.filter_by_cuisine) {
            showCuisineFilterDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showCuisineFilterDialog() {
        final String[] cuisines = {
                "African", "American", "British", "Cajun", "Caribbean",
                "Chinese", "Eastern European", "European", "French", "German",
                "Greek", "Indian", "Irish", "Italian", "Japanese", "Jewish",
                "Korean", "Latin American", "Mediterranean", "Mexican", "Middle Eastern",
                "Nordic", "Pakistani", "Southern", "Spanish", "Thai", "Vietnamese"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose a Cuisine");
        builder.setItems(cuisines, (dialog, which) -> {
            String selectedCuisine = cuisines[which];
            Toast.makeText(this, selectedCuisine, Toast.LENGTH_SHORT).show();
            Log.d("Cuisine", "Selected Cuisine: " + selectedCuisine);
            retrieveJsonByCuisine(apiKey, selectedCuisine);
        });

        builder.show();
    }

    public void retrieveJsonByCuisine(String apiKey, String cuisine) {
        swipeRefreshLayout.setRefreshing(true);
        Call<All> call = ApiClient.getInstance().getApi().getAllWithCuisine(apiKey, cuisine);
        call.enqueue(new Callback<All>() {
            @Override
            public void onResponse(Call<All> call, Response<All> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful() && response.body() != null) {
                    foods.clear();
                    foods.addAll(response.body().getResultsList());
                    if (adapter == null) {
                        adapter = new adapter(MainActivity1.this, foods);
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.updateList(foods);
                    }
                    adapter.notifyDataSetChanged();
                    lottieAnimationView.setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(MainActivity1.this, "No results for selected cuisine.", Toast.LENGTH_SHORT).show();
                    lottieAnimationView.setVisibility(View.VISIBLE);
                    lottieAnimationView.playAnimation();
                }
            }

            @Override
            public void onFailure(Call<All> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(MainActivity1.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                lottieAnimationView.setVisibility(View.VISIBLE);
                lottieAnimationView.playAnimation();
            }
        });
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        editText = findViewById(R.id.edtQuery);
        button = findViewById(R.id.search);
        swipeRefreshLayout = findViewById(R.id.swipe);
        recyclerView = findViewById(R.id.recyclerView);
        lottieAnimationView = findViewById(R.id.lottie);
        lottieAnimationView.setVisibility(View.INVISIBLE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        button.setOnClickListener(v -> {
            String queryText = editText.getText().toString().trim();
            if (!queryText.isEmpty()) {
                fetchUserDietAndSearch(queryText);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            fetchUserDietAndSearch(editText.getText().toString());
        });
    }

    private boolean containsNonVegKeyword(String query) {
        for (String keyword : nonVegKeywords) {
            if (Pattern.compile("\\b" + keyword + "\\b", Pattern.CASE_INSENSITIVE).matcher(query).find()) {
                return true;
            }
        }
        return false;
    }

    private void fetchUserDietAndSearch(String query) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users1");
        userRef.child(FirebaseAuth.getInstance().getUid())
                .child("dietary_preferences")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String diet = snapshot.getValue(String.class);
                        if (diet != null && !diet.isEmpty()) {
                            if ((diet.equalsIgnoreCase("vegetarian") || diet.equalsIgnoreCase("vegan")) &&
                                    containsNonVegKeyword(query)) {
                                Toast.makeText(MainActivity1.this, "Your diet is set to " + diet + ".", Toast.LENGTH_LONG).show();
                                lottieAnimationView.setVisibility(View.VISIBLE);
                                lottieAnimationView.playAnimation();
                                return; // Prevents the API call
                            }
                            retrieveJsonfilter(apiKey, query, "calories", "asc", diet.toLowerCase().trim());
                        } else {
                            retrieveJsonfilter(apiKey, query, "calories", "asc", null);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        retrieveJsonfilter(apiKey, query, "calories", "asc", null);
                    }
                });
    }

    public void retrieveJsonfilter(String apikey, String query, String sort, String sortDirection, String diet) {
        swipeRefreshLayout.setRefreshing(true);
        Call<All> call = ApiClient.getInstance().getApi().getAllSort(apikey, query, sort, sortDirection, diet);
        call.enqueue(new Callback<All>() {
            @Override
            public void onResponse(Call<All> call, Response<All> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful() && response.body() != null && response.body().getResultsList() != null) {
                    lottieAnimationView.setVisibility(View.INVISIBLE);
                    foods.clear();
                    foods.addAll(response.body().getResultsList());
                    if (adapter == null) {
                        adapter = new adapter(MainActivity1.this, foods);
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.updateList(foods);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    lottieAnimationView.setVisibility(View.VISIBLE);
                    lottieAnimationView.playAnimation();
                    Toast.makeText(MainActivity1.this, "No resource in our database", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<All> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(MainActivity1.this, "Request failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sortListByCalories() {
        fetchUserDietAndSearch(editText.getText().toString());
        Toast.makeText(this, isReversed ? "Sorted: High to Low" : "Sorted: Low to High", Toast.LENGTH_SHORT).show();
        isReversed = !isReversed;
    }
}
