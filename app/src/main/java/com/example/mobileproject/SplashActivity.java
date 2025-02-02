package com.example.mobileproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.auth0.android.jwt.JWT;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences credentials_sp;

    String password, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        credentials_sp = getSharedPreferences("credentials", MODE_PRIVATE);

        // during the splash the sharef pref is checked
        // for user password and token
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(credentials_sp.contains("token") && credentials_sp.contains("password")) {
                    token = credentials_sp.getString("token", "");
                    password = credentials_sp.getString("password", "");
                    Log.i("the token", token);
                }
                if (token != null && password != null) {
                    // Check password against database if token exists
                    Log.i("signal", "ping");
                    if(validateToken(token)) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    } else {
                        // get new token
                        // the time limit of the token is 1 month so this is not
                        // a matter of concern now
                    }
                } else {
                    // No login info found, proceed to login screen
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }, 2000); // 2-second splash delay
    }

    private boolean validateToken(String token) {
        try {
            JWT jwt = new JWT(token);

            // Check the expiry date of the token
            long expTime = jwt.getExpiresAt().getTime() / 1000; // Expiry in seconds
            long currentTime = System.currentTimeMillis() / 1000; // Current time in seconds

            // If current time is before expiry time, the token is valid
            return expTime > currentTime;
        } catch (Exception e) {
            return false; // Invalid token
        }
    }
}
