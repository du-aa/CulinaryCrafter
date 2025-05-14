package com.example.reciperecommendation.Result;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.reciperecommendation.MealPlanningModule.MealPlanDetails;
import com.example.reciperecommendation.R;
import com.example.reciperecommendation.Utils.DateUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MealPlanAdapter extends RecyclerView.Adapter<MealPlanAdapter.MealViewHolder> {

    private List<MealPlanDetails> mealList;
    Context context ;
    String selectedFilterPasser ;

    public MealPlanAdapter(Context ctx ,
                           List<MealPlanDetails> mealList, String pass) {
        selectedFilterPasser = pass;
        this.mealList = mealList;
        context = ctx ;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal1 , parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        MealPlanDetails meal = mealList.get(position);
        holder.title.setText(meal.getTitle());
        holder.calories.setText("Calories: " + meal.getCalories() );
        Glide.with(holder.imageView.getContext()).load(meal.getImage()).into(holder.imageView);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, WeeklyMealDetailActivity.class);
            intent.putExtra("mealId", meal.getId());
            intent.putExtra("mealName", meal.getTitle());
            intent.putExtra("Status", selectedFilterPasser);
            context.startActivity(intent);
        });
        holder.viewchart.setOnClickListener(view -> {
            Intent intent = new Intent(context, WeeklyMealDetailActivity.class);
            intent.putExtra("mealId", meal.getId());
            intent.putExtra("mealName", meal.getTitle());
            intent.putExtra("Status", selectedFilterPasser);
            context.startActivity(intent);
        });

        holder.DateTime.setText(meal.getDate() + "    "  + DateUtils.getDayName(meal.getDate()));

        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference =
                        FirebaseDatabase.getInstance().getReference("ConsumedMeals");
                databaseReference.child(meal.getId()).removeValue();
                Toast.makeText(context, "Deleted Successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder {
        TextView title, calories , viewchart , DateTime ;
        ImageView imageView;

        ImageView Delete ;
        public MealViewHolder(View itemView) {
            super(itemView);
            viewchart = itemView.findViewById(R.id.chart);
            title = itemView.findViewById(R.id.mealTitle);
            calories = itemView.findViewById(R.id.mealCalories);
            imageView = itemView.findViewById(R.id.mealImage);

            Delete = itemView.findViewById(R.id.delete);
            DateTime = itemView.findViewById(R.id.date);
        }
    }
}
