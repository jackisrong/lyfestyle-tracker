package com.example.lyfestyletracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.lyfestyletracker.web.QueryExecutable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class SleepTracker extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_tracker);

    }
}