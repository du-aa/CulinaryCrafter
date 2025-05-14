package com.example.reciperecommendation.ViewMeal;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.reciperecommendation.CommunityDetail;
import com.example.reciperecommendation.FirebaseModel.RecipieModel;
import com.example.reciperecommendation.R;
import com.example.reciperecommendation.Rating.RatingBarActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RecipeAdapter1 extends RecyclerView.Adapter<RecipeAdapter1.ViewHolder> {

    private Context context;
    private List<RecipieModel> recipeList;

    public RecipeAdapter1(Context context, List<RecipieModel> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recipe_commuity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecipieModel recipe = recipeList.get(position);

        holder.recipeName.setText(recipe.getName());
        holder.recipeDesc.setText(recipe.getDesciption());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {context.startActivity(new Intent(context ,
                    RatingBarActivity.class)
                    .putExtra("itemkey" , recipe.getRECID())
                    );

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new android.app.AlertDialog.Builder(context)
                        .setTitle("Delete Recipe")
                        .setMessage("Are you sure you want to delete this recipe?")
                        .setPositiveButton("Yes", new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(android.content.DialogInterface dialog, int which) {

                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("recipe");
                                databaseReference.child(recipe.getRECID()).removeValue();
                                Toast.makeText(context, "Recipe deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();

                return true;
            }
        });


        holder.recipeDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context ,
                        CommunityDetail.class)
                        .putExtra("uri" , recipe.getPicture())
                        .putExtra("title" , recipe.getName())
                        .putExtra("desc" , recipe.getDesciption())



                );

            }
        });
        // Load image using Glide
        Glide.with(context).load(recipe.getPicture()).into(holder.recipeImage);
    }
    @Override
    public int getItemCount() {
        return recipeList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView recipeName, recipeDesc;
        ImageView recipeImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipeName);
            recipeDesc = itemView.findViewById(R.id.recipeDesc);
            recipeImage = itemView.findViewById(R.id.recipeImage);
        }
    }
}
