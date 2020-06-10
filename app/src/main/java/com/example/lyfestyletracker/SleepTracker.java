package com.example.lyfestyletracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.json.JSONArray;

import java.util.concurrent.*;

public class SleepTracker extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_tracker);
        QueryExecutable qe = new QueryExecutable("https://www.students.cs.ubc.ca/~luigi28/hello.php?query_type=select&columns=all&table=userperson&username=bob123&password=12345;)");
        JSONArray res = qe.run();
        qe.stop();
        System.out.println(res);


    }
}