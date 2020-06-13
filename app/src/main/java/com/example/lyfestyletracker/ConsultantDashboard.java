package com.example.lyfestyletracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.lyfestyletracker.web.QueryExecutable;

import org.json.JSONArray;

import java.util.LinkedHashMap;
import java.util.Map;

public class ConsultantDashboard extends AppCompatActivity {
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant_dashboard);
        username = getIntent().getStringExtra("username");
    }

    public void deleteAccount(View view) {
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("query_type", "special_change");
        map.put("extra", "DELETE FROM People WHERE username = '"+ username + "'");
        QueryExecutable qe = new QueryExecutable(map);
        JSONArray res = qe.run();

        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    public void navigate(View view){
        int buttonId = view.getId();
        Intent intent;

        if (buttonId == R.id.nav_button_clients) {
            intent = new Intent(this, ConsultantUserDashboard.class);
        } else if(buttonId == R.id.nav_button_plans){
            intent = new Intent(this, ConsultantPlansDashboard.class);
        } else {
            // this should never happen, but just in case...
            intent = new Intent(this, ConsultantDashboard.class);  ///FIX THISS
        }

        intent.putExtra("username", getIntent().getStringExtra("username"));
        startActivity(intent);
    }
}