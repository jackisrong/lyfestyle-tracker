package com.example.lyfestyletracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lyfestyletracker.web.QueryExecutable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class UserDashboard extends AppCompatActivity {
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        username = getIntent().getStringExtra("username");

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("query_type", "special");
        map.put("extra", "SELECT name FROM people WHERE username = '" + username + "'");
        QueryExecutable qe = new QueryExecutable(map);
        String welcomeString = "Welcome!";
        try {
            welcomeString = "Welcome " + qe.run().getJSONObject(0).getString("NAME") + "!";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ((TextView) findViewById(R.id.user_dashboard_welcome_prompt)).setText(welcomeString);

        String[] subgreetings = {"Did you eat your fiber today?", "Do you like pie?", "What are your thoughts on golf?",
        "Is pasta better than pizza?", "Did you exercise today?", "Stop drinking so much bubble tea",
        "Bubble tea isn't a personality", "Bubble tea is not a replacement for water", "Damn you looking good!",
        "Guac is extra", "Guac shouldn't be extra, change my mind", "Did you know water is good for you?"};
        Random random = new Random();
        ((TextView) findViewById(R.id.user_dashboard_subgreeting)).setText(subgreetings[random.nextInt(subgreetings.length)]);

        setDetails();
    }

    public void navigation(View view) {
        int buttonId = view.getId();
        Intent intent;
        if (buttonId == R.id.nav_button_food) {
            intent = new Intent(this, FoodDashboard.class);
        } else if (buttonId == R.id.nav_button_clients) {
            intent = new Intent(this, ExerciseDashboard.class);
        } else if (buttonId == R.id.nav_button_plans) {
            intent = new Intent(this, SleepDashboard.class);
        } else if (buttonId == R.id.nav_button_consultants) {
            intent = new Intent(this, UserConsultantsDashboard.class);
        } else {
            // this should never happen, but just in case...
            intent = new Intent(this, UserDashboard.class);
        }

        intent.putExtra("username", getIntent().getStringExtra("username"));
        startActivity(intent);


    }

    private void setDetails(){
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("query_type", "special");
        map.put("extra", "Select age, weight, height from UserPerson where username = '" + username + "'");
        QueryExecutable qe = new QueryExecutable(map);
        JSONArray res = qe.run();

        TextView age = (TextView) findViewById(R.id.age_text);
        TextView weight = (TextView) findViewById(R.id.weight_text);
        TextView height = (TextView) findViewById(R.id.height_text);

        try{
            JSONObject o = res.getJSONObject(0);
            age.setText("Age: " + o.getString("AGE"));
            weight.setText("Weight: " + o.getString("WEIGHT") + "lbs");
            height.setText("Height: " + o.getString("HEIGHT") + "cm");

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void updateUserDetails(View view){
        int buttonId = view.getId();

        String column;
        String setTo;

        if (buttonId == R.id.age_button){
            column = "age";
            setTo = ((EditText) findViewById(R.id.enter_age)).getText().toString();
        }else if (buttonId == R.id.weight_button){
            column = "weight";
            setTo = ((EditText) findViewById(R.id.enter_weight)).getText().toString();
        }else if (buttonId == R.id.height_button){
            column = "height";
            setTo = ((EditText) findViewById(R.id.enter_height)).getText().toString();
        }else {
            return;
        }

        if (setTo.equals("")) {
            return;
        }

        Map<String,Object> map = new LinkedHashMap<>();
        map.put("query_type", "special_change");
        map.put("extra", "UPDATE userPerson SET " + column + " = " + setTo + " where username = '" + username + "'");
        QueryExecutable qe = new QueryExecutable(map);
        qe.run();

        setDetails();
    }

    public void log_out(View view){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

}