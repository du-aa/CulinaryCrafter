package com.example.reciperecommendation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchDarkMode, switchNotifications;
    private TextView tvSelectedLanguage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchDarkMode = findViewById(R.id.switchDarkMode);
        switchNotifications = findViewById(R.id.switchNotifications);
        tvSelectedLanguage = findViewById(R.id.tvSelectedLanguage);

        // Load preferences
        SharedPreferences prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);
        switchDarkMode.setChecked(prefs.getBoolean("DarkMode", false));
        switchNotifications.setChecked(prefs.getBoolean("Notifications", true));

        // Handle Dark Mode toggle
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("DarkMode", isChecked);
            editor.apply();
        });

        // Handle Notifications toggle
        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("Notifications", isChecked);
            editor.apply();
        });

        // Handle Language Selection (Mock)
        tvSelectedLanguage.setOnClickListener(v -> {
            tvSelectedLanguage.setText("French"); // Change to language selection dialog if needed
        });
    }
}
