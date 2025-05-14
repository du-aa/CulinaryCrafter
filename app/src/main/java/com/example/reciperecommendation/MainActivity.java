package com.example.reciperecommendation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reciperecommendation.Api.MainActivity1;
import com.example.reciperecommendation.BMI.BmiCalorieActivity;
import com.example.reciperecommendation.FullTrack.FullMealDetailActivity;
import com.example.reciperecommendation.Management.Login;
import com.example.reciperecommendation.MealPlanningModule.MealActivity;
import com.example.reciperecommendation.Result.MealPlanActivity;
import com.example.reciperecommendation.Result.MealPlanAdapter;
import com.example.reciperecommendation.UploadRecipie.UploadPictureOne;
import com.example.reciperecommendation.ViewMeal.RecipeFeedActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    ImageView imageView ;
    Button Logout ;
    Button Recipie  , Profile  , Calculator_btn;
    @SuppressLint({"MissingInflatedId", "NonConstantResourceId"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admindashboard);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            if(item.getItemId()== R.id.nav_home){
                startActivity(new Intent(MainActivity.this , MainActivity1.class));

            }
            if(item.getItemId()== R.id.nav_result){
                startActivity(new Intent(MainActivity.this , MealPlanActivity.class));
            }
            if(item.getItemId()== R.id.nav_profile){
                startActivity(new Intent(MainActivity.this , UpdateProfile.class));
            }

            if(item.getItemId()== R.id.track_full){
                startActivity(new Intent(MainActivity.this , FullMealDetailActivity.class));

            }


            return true;
        });
        imageView = findViewById(R.id.dashimg);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button Uploads = findViewById(R.id.uploads);
        Uploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this , UploadPictureOne.class));
            }
        });
        Calculator_btn = findViewById(R.id.calculator);
        Calculator_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this , BmiCalorieActivity.class));
            }
        });

      Button  ViewCommunity = findViewById(R.id.viewcommunity);
        ViewCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this , RecipeFeedActivity.class));
            }
        });
        Profile = findViewById(R.id.profile);
        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this , UpdateProfile.class));
            }
        });

        Recipie = findViewById(R.id.recipie);
        Recipie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this , MealActivity.class));

            }
        });



    Logout = findViewById(R.id.logout);
    Logout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Logout")
                    .setMessage("Are You Want Logout Account !")
                    .setPositiveButton("Logout",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                    mAuth.signOut();
                                    startActivity(new Intent( getApplicationContext() , Login.class));
                                    finish();
                                }
                            }
                    )
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                }
                            }
                    )
                    .create();
            alertDialog.show();
        }
    });
    }
    private void shareApp() {
        String appPackageName = getPackageName(); // Your app package name
        String shareText = "Check out this amazing app: https://play.google.com/store/apps/details?id=" + appPackageName;

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, shareText);

        startActivity(Intent.createChooser(intent, "Share via"));
    }

}

