package com.example.reciperecommendation.MealPlanningModule;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.reciperecommendation.R;
import com.example.reciperecommendation.Utils.DateUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {
    private List<Meal> mealList;
    private  Context context1;

    List<String> daylist ;
    private OnMealConsumeListener listener;

    String Status = "" ;
    public void setStatus(String str) {
    Status = str ;
    }

    public interface OnMealConsumeListener {
        void onMealConsumed(Meal meal);
    }

    static int count = 0 ;
    public MealAdapter(Context context , List<Meal> mealList, OnMealConsumeListener listener) {
        this.mealList = mealList;
        this.listener = listener;
        context1 = context
        ;
        daylist = new ArrayList<>();
        daylist.add("monday");
        daylist.add("tuesday");
        daylist.add("wednesday");
        daylist.add("thursday");
        daylist.add("friday");
        daylist.add("saturday");
        daylist.add("sunday");
    count = 0 ;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal meal = mealList.get(position);
        if(position%3==0 && Status.equals("daily")){
            try {
                holder.mealTitle.setText(DateUtils.getDayName(DateUtils.getCurrentDate()) + "  " +
                        "\n\n\t\t\t " + meal.getTitle());
                applyColor(holder);
            }catch (Exception e){
                Toast.makeText(context1, "daily " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        }

       else  if(position%3==0 &&  Status.equals("weekly")){
            try {
                int daysToAdd = (position / 3);

                String day = DateUtils.getNextDate(DateUtils.getCurrentDate() , daysToAdd+1);
                holder.mealTitle.setText("\n\n\t\t\t" + DateUtils.getDayName(day) + "  " +
                        "\n"+ meal.getTitle());
                applyColor(holder);
            }catch (Exception e){
                Toast.makeText(context1, "weekly " + e.toString(), Toast.LENGTH_SHORT).show();

            }
        }


        else  if(position%3==0 &&  Status.equals("monthly")){
            try {
                int daysToAdd = (position / 3);

                String day = DateUtils.getNextDate(DateUtils.getCurrentDate() , daysToAdd+1);
                holder.mealTitle.setText("\n\n\t\t\t" +DateUtils.getDayName(day) + "  " +
                        "\n"+ meal.getTitle());

                applyColor(holder);


            }catch (Exception e){
                Toast.makeText(context1, "weekly " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        else{
            holder.mealTitle.setText( meal.getTitle());

        }




        Glide.with(holder.itemView.getContext()).load(meal.getImage()).into(holder.mealImage);
        holder.consumeButton.setOnClickListener(v -> listener.onMealConsumed(meal));



        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context1, MealDetailActivity.class);
            intent.putExtra("meal_id", meal.getId());
            intent.putExtra("meal_title", meal.getTitle());
            intent.putExtra("meal_image", meal.getImage());
            context1.startActivity(intent);

        });

        holder.Generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context1, GenerateIngrediants.class);
                intent.putExtra("meal_id", meal.getId())
                ;
                context1.startActivity(intent);
            }
        });

//        holder.consumeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DatabaseReference ShowMealRef = FirebaseDatabase.getInstance().getReference("mealdaily").child(FirebaseAuth.getInstance().getUid());
//                ShowMealRef.child(meal.getId()+"").removeValue();
//                notifyDataSetChanged();
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    public static class MealViewHolder extends RecyclerView.ViewHolder {
        TextView mealTitle;
        ImageView mealImage;
        Button consumeButton;
        Button Generate;

        public MealViewHolder(View itemView) {
            super(itemView);
            mealTitle = itemView.findViewById(R.id.mealTitle);
            mealImage = itemView.findViewById(R.id.mealImage);
            consumeButton = itemView.findViewById(R.id.consumeButton);
            Generate = itemView.findViewById(R.id.generate);
        }
    }
    public  void applyColor( MealViewHolder holder ){
        try {
            String text = holder.mealTitle.getText().toString();

            if (text != null && !text.isEmpty()) {
                SpannableString spannableString = new SpannableString(text);

                int firstPartLength = Math.min(10, text.length());

                spannableString.setSpan(new ForegroundColorSpan(Color.RED),
                        0, firstPartLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                holder.mealTitle.setText(spannableString);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
