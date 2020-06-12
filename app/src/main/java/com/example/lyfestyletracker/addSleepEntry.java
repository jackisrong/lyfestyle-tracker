package com.example.lyfestyletracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lyfestyletracker.web.QueryExecutable;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

public class addSleepEntry extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    public static final int REQUEST_CODE = 152;
    private TextView dateResult;
    private  String username;
    private  Spinner s1;
    private  Spinner s2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sleep_entry);
        s1 = (Spinner) findViewById(R.id.sleep_quality_spinner);
        s2 = (Spinner) findViewById(R.id.sleep_time_spinner);
        dateResult = findViewById(R.id.DateResultView);

        username = getIntent().getStringExtra("username");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        s1.setAdapter(adapter);
        s2.setAdapter(adapter);
    }

    public void createButtonClicked(View view){
        if (dateResult.getText() != ""){

            Snackbar.make(view, "Added Entry", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            Map<String,Object> map = new LinkedHashMap<>();
            map.put("query_type", "special_change");
            map.put(
                    "extra",
                    "INSERT INTO userSleepEntry VALUES ('"
                            + username
                            + "', TO_DATE('"
                            + dateResult.getText()
                            +"', 'YYYY-MM-DD'), "
                            + s1.getSelectedItem().toString() + ", " + s2.getSelectedItem().toString() +")");

            System.out.println("INSERT INTO userSleepEntry VALUES ("
                    + username
                    + ", TO_DATE('"
                    + dateResult.getText()
                    +"', 'YYYY-MM-DD'), "
                    + s1.getSelectedItem().toString() + ", " + s1.getSelectedItem().toString() +")");
            QueryExecutable qe = new QueryExecutable(map);
            JSONArray res = qe.run();

        }else{
            Snackbar.make(view, "Invalid Date", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

    }

    public void createDateClicked(View view){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String monthS = Integer.toString(month + 1);
        String dayOfMonthS = Integer.toString(dayOfMonth);

        if (month < 10){
            monthS = "0" + monthS;
        }
        if (dayOfMonth < 10){
            dayOfMonthS = "0" + dayOfMonthS;
        }

        String date = year + "-" + monthS + "-" + dayOfMonthS;
        dateResult.setText(date);
    }
}