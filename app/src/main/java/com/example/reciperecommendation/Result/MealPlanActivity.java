package com.example.reciperecommendation.Result;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.reciperecommendation.MealPlanningModule.MealPlanDetails;
import com.example.reciperecommendation.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MealPlanActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MealPlanAdapter adapter;
    private List<MealPlanDetails> mealList = new ArrayList<>();
    private DatabaseReference databaseReference;
    private String selectedFilter = "Daily"; // Default filter
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button btnDaily, btnWeekly, btnMonthly;
    private ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth ;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);

        firebaseAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        btnDaily = findViewById(R.id.dailyBtn);
        btnWeekly = findViewById(R.id.weeklyBtn);
        btnMonthly = findViewById(R.id.monthlyBtn);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MealPlanAdapter (this  , mealList , selectedFilter);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("ConsumedMeals");

        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        btnDaily.setOnClickListener(view -> {
            selectedFilter = "Daily";
            showLoading();
            fetchMeals();
        });

        btnWeekly.setOnClickListener(view -> {
            selectedFilter = "Weekly";
            showLoading();
            fetchMeals();
        });

        btnMonthly.setOnClickListener(view -> {
            selectedFilter = "Monthly";
            showLoading();
            fetchMeals();
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            showLoading();
            fetchMeals();
        });

        fetchMeals();
    }

    private void fetchMeals() {
        swipeRefreshLayout.setRefreshing(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mealList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MealPlanDetails meal = dataSnapshot.getValue(MealPlanDetails.class);

                    if(firebaseAuth.getUid().equals(meal.getUUID())){
                    if (meal != null && meal.getStatus().equalsIgnoreCase(selectedFilter)) {
                        mealList.add(meal);
                    }
                    }

                }
                adapter.notifyDataSetChanged();
                hideLoading();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                hideLoading();
                Toast.makeText(MealPlanActivity.this, "Error loading data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading() {
        progressDialog.show();
    }

    private void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
