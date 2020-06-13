package com.example.lyfestyletracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class UserDashboard extends AppCompatActivity {
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        username = getIntent().getStringExtra("username");
    }

    public void navigation(View view) {
        int buttonId = view.getId();
        Intent intent;
        if (buttonId == R.id.nav_button_food) {
            intent = new Intent(this, FoodDashboard.class);
        } else if (buttonId == R.id.nav_button_clients) {
            intent = new Intent(this, ExerciseDashboard.class);
        } else if (buttonId == R.id.nav_button_plans) {
            intent = new Intent(this, SleepDashboard.class);
        } else if (buttonId == R.id.nav_button_consultants) {
            intent = new Intent(this, UserConsultantsDashboard.class);
        } else {
            // this should never happen, but just in case...
            intent = new Intent(this, UserDashboard.class);
        }

        intent.putExtra("username", getIntent().getStringExtra("username"));
        startActivity(intent);


    }

    private void deleteAccount(View view){

    }

}