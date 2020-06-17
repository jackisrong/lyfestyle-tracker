package com.example.lyfestyletracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.lyfestyletracker.ui.main.ExerciseSectionsPagerAdapter;
import com.example.lyfestyletracker.web.QueryExecutable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.LinkedHashMap;
import java.util.Map;

public class ExerciseDashboard extends AppCompatActivity {

    private String username;
    private boolean currentTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        username = getIntent().getStringExtra("username");
        setContentView(R.layout.activity_exercise_dashboard);
        ExerciseSectionsPagerAdapter exerciseSectionsPagerAdapter = new ExerciseSectionsPagerAdapter(this, getSupportFragmentManager(), getIntent().getStringExtra("username"));
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(exerciseSectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab_diet);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TabLayout tb = (TabLayout) findViewById(R.id.tabs);
                if (tb.getSelectedTabPosition() == 0) {
                    Intent intent = new Intent(ExerciseDashboard.this, AddWorkout.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ExerciseDashboard.this, WorkoutPlan.class);
                    intent.putExtra("username", username);

                    MaxPlan mp = new MaxPlan();
                    Integer maxInt = mp.getMaxPlan();
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("query_type", "special_change");
                    map.put("extra", "insert into Plan Values(" + maxInt + ", '" + username + "')");
                    QueryExecutable qe = new QueryExecutable(map);
                    qe.run();

                    map.clear();
                    map.put("query_type", "special_change");
                    map.put("extra", "insert into WorkoutPlan Values(" + maxInt + ", 0)");

                    qe = new QueryExecutable(map);
                    qe.run();

                    intent.putExtra("workoutId", maxInt);
                    startActivity(intent);
                }
            }
        });
    }
}