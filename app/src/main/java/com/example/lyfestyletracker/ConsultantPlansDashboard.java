package com.example.lyfestyletracker;

import android.content.Intent;
import android.os.Bundle;

import com.example.lyfestyletracker.web.QueryExecutable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.example.lyfestyletracker.ui.main.ConsultantPlansPagerAdapter;

import org.json.JSONArray;

import java.util.LinkedHashMap;
import java.util.Map;

public class ConsultantPlansDashboard extends AppCompatActivity {
    private String username = "";

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

        username = getIntent().getStringExtra("username");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TabLayout tb = (TabLayout) findViewById(R.id.tabs);
                if (tb.getSelectedTabPosition() == 0){
                    Intent intent = new Intent(ConsultantPlansDashboard.this, DietPlan.class);
                    intent.putExtra("username", username);


                    MaxPlan mp = new MaxPlan();
                    Integer maxInt = mp.getMaxPlan();
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("query_type", "special_change");
                    map.put("extra", "insert into Plan Values(" + maxInt + ", '" + username + "')");
                    QueryExecutable qe = new QueryExecutable(map);
                    JSONArray ans = qe.run();

                    map.clear();
                    map.put("query_type", "special_change");
                    map.put("extra", "insert into Diet Values(" + maxInt + ", 0)");

                    qe = new QueryExecutable(map);
                    ans = qe.run();



                    intent.putExtra("dietId", maxInt);
                    intent.putExtra("consultant", true);
                    startActivity(intent);

                }else{
                    Intent intent = new Intent(ConsultantPlansDashboard.this, WorkoutPlan.class);
                    intent.putExtra("username", username);


                    MaxPlan mp = new MaxPlan();
                    Integer maxInt = mp.getMaxPlan();
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("query_type", "special_change");
                    map.put("extra", "insert into Plan Values(" + maxInt + ", '" + username + "')");
                    QueryExecutable qe = new QueryExecutable(map);
                    JSONArray ans = qe.run();

                    map.clear();
                    map.put("query_type", "special_change");
                    map.put("extra", "insert into WorkoutPlan Values(" + maxInt + ", 0)");

                    qe = new QueryExecutable(map);
                    ans = qe.run();



                    intent.putExtra("workoutId", maxInt);
                    intent.putExtra("consultant", true);
                    startActivity(intent);
                }
            }
        });
    }
}