package com.example.lyfestyletracker;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lyfestyletracker.web.QueryExecutable;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

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

    private void populateSpinner() {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("query_type", "special");
        map.put("extra", "Select c.username, c.result FROM Consultant c WHERE c.username NOT IN (SELECT consultantUsername FROM UserHiresConsultant WHERE userUsername = '" + username + "') ORDER BY c.result");

        QueryExecutable qe = new QueryExecutable(map);
        JSONArray ans = qe.run();

        try {
            for (int i = 0; i < ans.length(); i++) {
                JSONObject o = ans.getJSONObject(i);
                spinnerAdapter.add(o.getString("USERNAME") + " - Rating: " + o.getString("RESULT"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        spinnerAdapter.notifyDataSetChanged();
    }

    public void hirePerson(View view) {
        String spinnerContent = spinner.getSelectedItem().toString();
        String[] split = spinnerContent.split(" ");
        String usernameC = "";

        if (split.length > 0) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("query_type", "special_change");

            usernameC = split[0];

            Integer rand = new Random().nextInt(Integer.MAX_VALUE);

            System.out.println(usernameC);
            System.out.println(username);

            map.put("extra", "Insert Into UserHiresConsultant Values('" + username + "', '" + usernameC + "', " + rand + ")");

            QueryExecutable qe = new QueryExecutable(map);
            qe.run();
            finish();
        } else {
            Snackbar.make(view, "Invalid Consultant", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }
}