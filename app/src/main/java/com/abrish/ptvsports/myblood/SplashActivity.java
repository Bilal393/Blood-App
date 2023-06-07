package com.abrish.ptvsports.myblood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Check if login details are available
        if (isLoginDetailsAvailable()) {
            // Start MainActivity
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        } else {
            // Start RegistrationActivity
            startActivity(new Intent(SplashActivity.this, RegistrationActivity.class));
        }

        finish(); // Finish the splash activity and prevent going back to it
    }

    private boolean isLoginDetailsAvailable() {
        String savedEmail = sharedPreferences.getString("email", "");
        String savedPassword = sharedPreferences.getString("password", "");
        return !TextUtils.isEmpty(savedEmail) && !TextUtils.isEmpty(savedPassword);
    }
}
