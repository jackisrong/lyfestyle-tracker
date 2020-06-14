package com.example.lyfestyletracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.lyfestyletracker.web.QueryExecutable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class HireConsultant extends AppCompatActivity {
    private String username;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hire_consultant);
        username = getIntent().getStringExtra("username");
        spinner = (Spinner) findViewById(R.id.spinner_available_consultants);
        populateSpinner();
    }

    private void populateSpinner(){
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);


        Map<String,Object> map = new LinkedHashMap<>();
        map.put("query_type", "special");
        map.put("extra", "Select c.username, c.result, co.name FROM Consultant c, Company co, ConsultantWorksForCompany cw WHERE cw.consultantUsername = c.username AND co.companyID = cw.companyId AND c.username NOT IN (SELECT consultantUsername FROM UserHiresConsultant WHERE userUsername = '" + username +"') ORDER BY c.result");



        QueryExecutable qe = new QueryExecutable(map);
        JSONArray ans = qe.run();

        try{
            for(int i = 0; i<ans.length(); i ++){
                JSONObject o = ans.getJSONObject(i);
                spinnerAdapter.add(o.getString("USERNAME") + " - rating:" + o.getString("RESULT") + " - company:" + o.getString("NAME"));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        spinnerAdapter.notifyDataSetChanged();

    }

    public void hirePerson(View view){

    }
}