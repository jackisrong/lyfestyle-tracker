package com.example.lyfestyletracker;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.example.lyfestyletracker.ui.main.FoodSectionsPagerAdapter;

public class FoodDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_dashboard);
        FoodSectionsPagerAdapter foodSectionsPagerAdapter = new FoodSectionsPagerAdapter(this, getSupportFragmentManager(), getIntent().getStringExtra("username"));
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(foodSectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoodDashboard.this, AddMeal.class);
                intent.putExtra("username", getIntent().getStringExtra("username"));
                startActivity(intent);
            }
        });
    }
}