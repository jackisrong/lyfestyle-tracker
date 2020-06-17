package com.example.lyfestyletracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.lyfestyletracker.ui.main.SleepSectionsPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class SleepDashboard extends AppCompatActivity {

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        username = getIntent().getStringExtra("username");
        setContentView(R.layout.activity_sleep_dashboard);
        SleepSectionsPagerAdapter sleepSectionsPagerAdapter = new SleepSectionsPagerAdapter(this, getSupportFragmentManager(), username);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sleepSectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab_diet);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSEntry();
            }
        });
    }

    public void addSEntry() {
        Intent intent = new Intent(this, AddSleep.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}