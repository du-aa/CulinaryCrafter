package com.example.reciperecommendation.Api;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reciperecommendation.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Details extends AppCompatActivity {
    TextView tvTitle, tvSource, tvDate, tvDesc, tvIngredients, tvSummary, tvInstructions;
    ImageView i;
    ProgressBar progressBar;
    ScrollView recipeContent;
    final String apiKey = "f7e1b1d2a23543818b66d6c23709ba38";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Bind views
        tvTitle = findViewById(R.id.tvTitle);
        tvDesc = findViewById(R.id.tvDesc);
        tvSource = findViewById(R.id.tvSource);
        tvDate = findViewById(R.id.tvDate);
        tvIngredients = findViewById(R.id.tvIngredients);
        tvSummary = findViewById(R.id.tvSummary);
        tvInstructions = findViewById(R.id.tvInstructions);
        i = findViewById(R.id.imageV);
        progressBar = findViewById(R.id.webloader);
        recipeContent = findViewById(R.id.recipeContent);

        // Show loader initially
        progressBar.setVisibility(View.VISIBLE);
        recipeContent.setVisibility(View.GONE);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String imageUrl = intent.getStringExtra("imageUrl");
        String id = intent.getStringExtra("id");

        tvTitle.setText(title);
        Picasso.get().load(imageUrl).into(i);

        fetchData(id, apiKey);
    }

    private void fetchData(String id, String apikey) {
        String url = "https://api.spoonacular.com/recipes/" + id + "/information?apiKey=" + apikey;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get().build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(Details.this, "Error loading recipe", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String resp = response.body().string();
                    runOnUiThread(() -> {
                        try {
                            JSONObject jsonObject = new JSONObject(resp);
                            String sourceName = jsonObject.optString("sourceName", "N/A");
                            String likes = jsonObject.optString("aggregateLikes", "0");
                            String ready = jsonObject.optString("readyInMinutes", "0");
                            String summary = jsonObject.optString("summary", "");
                            String instructionsRaw = jsonObject.optString("instructions", "");

                            // Parse ingredients
                            JSONArray ingredientsArray = jsonObject.getJSONArray("extendedIngredients");
                            StringBuilder ingredientsList = new StringBuilder();
                            for (int i = 0; i < ingredientsArray.length(); i++) {
                                JSONObject ingredient = ingredientsArray.getJSONObject(i);
                                String original = ingredient.getString("original");
                                ingredientsList.append("• ").append(original).append("\n");
                            }

                            // Bullet instructions
                            String[] instructionsLines = instructionsRaw.replaceAll("<.*?>", "").split("\\.");
                            StringBuilder bulletInstructions = new StringBuilder();
                            for (String line : instructionsLines) {
                                String trimmed = line.trim();
                                if (!trimmed.isEmpty()) {
                                    bulletInstructions.append("• ").append(trimmed).append(".\n");
                                }
                            }

                            // Update UI
                            tvDesc.setText("Ready in: " + ready + " minutes");
                            tvSource.setText("Source: " + sourceName);
                            tvDate.setText("Likes: " + likes);
                            tvSummary.setText(summary.replaceAll("<.*?>", ""));
                            tvIngredients.setText(ingredientsList.toString());
                            tvInstructions.setText(bulletInstructions.toString());

                            progressBar.setVisibility(View.GONE);
                            recipeContent.setVisibility(View.VISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Details.this, "Parsing error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
