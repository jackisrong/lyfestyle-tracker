package com.example.lyfestyletracker;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lyfestyletracker.web.QueryExecutable;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

public class AddSleep extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final int REQUEST_CODE = 152;
    private TextView dateResult;
    private String username;
    private Spinner s1;
    private Spinner s2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sleep);
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

    public void createButtonClicked(View view) {
        if (!dateResult.getText().equals(getString(R.string.no_date_set_label))) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("query_type", "special_change");
            map.put("extra", "INSERT INTO userSleepEntry VALUES ('" + username + "', TO_DATE('"
                    + dateResult.getText() + "', 'YYYY-MM-DD'), " + s1.getSelectedItem().toString() + ", "
                    + s2.getSelectedItem().toString() + ")");

            QueryExecutable qe = new QueryExecutable(map);
            qe.run();

            Toast.makeText(this, "Successfully added sleep entry", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Snackbar.make(view, "Invalid Date", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    public void createDateClicked(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String monthString = month < 10 ? "0" + (month + 1) : Integer.toString(month + 1);
        String dayOfMonthString = dayOfMonth < 10 ? "0" + dayOfMonth : Integer.toString(dayOfMonth);

        String date = year + "-" + monthString + "-" + dayOfMonthString;
        dateResult.setText(date);
    }
}