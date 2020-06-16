package com.example.lyfestyletracker;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.example.lyfestyletracker.ui.main.ConsultantPlansPagerAdapter;

public class ConsultantPlansDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant_plans);
        ConsultantPlansPagerAdapter sectionsPagerAdapter = new ConsultantPlansPagerAdapter(this, getSupportFragmentManager(), getIntent().getStringExtra("username"));
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab_diet);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TabLayout tb = (TabLayout) findViewById(R.id.tabs);
                if (tb.getSelectedTabPosition() == 0){

                }else{

                }
            }
        });
    }
}