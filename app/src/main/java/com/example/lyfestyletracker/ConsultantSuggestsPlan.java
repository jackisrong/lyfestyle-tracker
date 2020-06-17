package com.example.lyfestyletracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.lyfestyletracker.web.QueryExecutable;
import com.google.android.material.snackbar.Snackbar;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConsultantSuggestsPlan extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String username;
    private Spinner userSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant_suggests_plan);
        username = getIntent().getStringExtra("username");

        userSpinner =  (Spinner) findViewById(R.id.spinner_hired_users);
        userSpinner.setOnItemSelectedListener(this);
        populateUserSpinner();
    }

    private void populatePlanSpinner(){

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ((Spinner)findViewById(R.id.spinner_possible_plans)).setAdapter(spinnerAdapter);
        Map<String, Object> map = new LinkedHashMap<>();

        String [] strings = userSpinner.getSelectedItem().toString().split(" ");
        String userUsername;

        if (strings.length > 0){
            userUsername = strings[0];
        }else{
            userUsername = "";
        }

        map.put("query_type", "special");
        map.put("extra", "Select planId FROM Plan p WHERE createdByUsername = '" + username + "' AND planID NOT IN (SELECT planID from ConsultantSuggestsPlan WHERE userUsername = '" + userUsername +"' AND consultantUsername = '" + username +"')");
        QueryExecutable qe = new QueryExecutable(map);
        JSONArray res = qe.run();

        try{
            for(int i = 0; i<res.length(); i ++){
                JSONObject o = res.getJSONObject(i);
                spinnerAdapter.add("plan id:" + o.getString("PLANID"));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        spinnerAdapter.notifyDataSetChanged();

    }

    private void populateUserSpinner(){

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userSpinner.setAdapter(spinnerAdapter);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("query_type", "special");
        map.put("extra", "Select u.username, u.age, u.weight, u.height From UserPerson u, UserHiresConsultant uhc where u.username = uhc.userUsername AND uhc.consultantUsername = '" + username + "'");

        QueryExecutable qe = new QueryExecutable(map);
        JSONArray res = qe.run();

        try{
            for(int i = 0; i<res.length(); i ++){
                JSONObject o = res.getJSONObject(i);
                spinnerAdapter.add(o.getString("USERNAME") + " - age: " + o.getString("AGE") + " - weight :" + o.getString("WEIGHT") + " - height  :" + o.getString("HEIGHT"));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        spinnerAdapter.notifyDataSetChanged();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        populatePlanSpinner();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //
    }

    public void addSuggestedPlan(View view){
        if (userSpinner.getSelectedItem() == null){
            Snackbar.make(view, "Invalid User", Snackbar.LENGTH_LONG).setAction("Action", null).show();

            return;

        }else if (((Spinner)findViewById(R.id.spinner_possible_plans)).getSelectedItem() == null){
            Snackbar.make(view, "Invalid Plan", Snackbar.LENGTH_LONG).setAction("Action", null).show();

            return;

        }

        String [] strings = userSpinner.getSelectedItem().toString().split(" ");
        String userUsername = strings[0];

        strings = ((Spinner)findViewById(R.id.spinner_possible_plans)).getSelectedItem().toString().split(":");
        String plan = strings[1];


        LocalDateTime ld = new LocalDateTime();

        String formattedDate = ld.toString("dd-MM-yyyy HH:mm:ss");





        Map<String, Object> map = new LinkedHashMap<>();
        map.put("query_type", "special_change");
        map.put("extra", "Insert Into ConsultantSuggestsPlan Values('" + userUsername + "', '" + username + "', " + plan + ", TO_TIMESTAMP('" + formattedDate + "',  'YYYY-MM-DD HH24:MI:SS'))");
        QueryExecutable qe = new QueryExecutable(map);
        JSONArray res = qe.run();

        Snackbar.make(view, "Suggested Plan", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        populatePlanSpinner();
    }
}