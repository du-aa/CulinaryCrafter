package com.example.reciperecommendation;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class CommunityDetail extends AppCompatActivity {

    ImageView imageView;
    TextView nameText, descText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_recipe_commuity_detail);


        imageView = findViewById(R.id.recipe_image);
        nameText = findViewById(R.id.recipe_name);
        descText = findViewById(R.id.recipe_description);


        Picasso.get().load(getIntent().getStringExtra("uri")).into(imageView);
        nameText.setText(getIntent().getStringExtra("title"));
        descText.setText(getIntent().getStringExtra("desc"));


    }
}

