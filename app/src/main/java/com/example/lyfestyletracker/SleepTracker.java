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


        Map<String,Object> map = new LinkedHashMap<>();

        map.put("query_type", "special");
        map.put("columns", "all");
        map.put("table", "userPerson");
        map.put("username", "bob123");
        map.put("password", "12345;)");
        map.put("extra", "Select up.username, us.sleepTime from userPerson up, UserSleepEntry us WHERE up.username = us.username");
        //map.put("extra", "UPDATE People SET name = 'Luis E' WHERE username = 'Luis'");

        //"https://www.students.cs.ubc.ca/~luigi28/hello.php?query_type=select&columns=all&table=userperson&username=bob123&password=12345;)"
        QueryExecutable qe = new QueryExecutable(map);
        JSONArray res = qe.run();
        //qe.stop();
        System.out.println(res);


    }
}