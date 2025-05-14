package com.example.reciperecommendation.ViewMeal;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reciperecommendation.FirebaseModel.RecipieModel;
import com.example.reciperecommendation.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RecipeFeedActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecipeAdapter1 adapter;
    private List<RecipieModel> recipeList;
    private DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_feed);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseReference = FirebaseDatabase.getInstance().getReference("recipe");
        recipeList = new ArrayList<>();

        loadRecipesFromFirebase();
    }

    private void loadRecipesFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recipeList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    RecipieModel recipe = dataSnapshot.getValue(RecipieModel.class);
                        recipeList.add(recipe);
                }


                if(recipeList.size()==0){
                    Toast.makeText(RecipeFeedActivity.this, "No recipe found!", Toast.LENGTH_SHORT).show();
                }
                adapter = new RecipeAdapter1(RecipeFeedActivity.this, recipeList);
                recyclerView.setAdapter(adapter);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
