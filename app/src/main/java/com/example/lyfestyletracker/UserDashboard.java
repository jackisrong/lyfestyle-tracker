package com.example.lyfestyletracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class UserDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
    }

    public void navigation(View view) {
        int buttonId = view.getId();
        Intent intent;
        if (buttonId == R.id.nav_button_food) {
            intent = new Intent(this, UserDashboard.class);
            startActivity(intent);
        } else if (buttonId == R.id.nav_button_workouts) {
            intent = new Intent(this, UserDashboard.class);
            startActivity(intent);
        } else if (buttonId == R.id.nav_button_sleep) {
            intent = new Intent(this, SleepTracker.class);
            startActivity(intent);
        } else if (buttonId == R.id.nav_button_consultants) {
            intent = new Intent(this, UserDashboard.class);
            startActivity(intent);
        }
    }
}