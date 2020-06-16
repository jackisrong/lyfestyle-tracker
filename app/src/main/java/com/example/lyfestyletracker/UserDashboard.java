package com.example.lyfestyletracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lyfestyletracker.web.QueryExecutable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class UserDashboard extends AppCompatActivity {
    private String username;

    private EditText age;
    private EditText weight;
    private EditText height;
    private TextView weeklySum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        username = getIntent().getStringExtra("username");

        age = (EditText) findViewById(R.id.enter_age);
        weight = (EditText) findViewById(R.id.enter_weight);
        height = (EditText) findViewById(R.id.enter_height);
        weeklySum = (TextView) findViewById(R.id.weekly_calSum);

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

        map.clear();
        map.put("query_type", "special");
        map.put("extra", "Select SUM(caloriesPerServing) from UserMealLog u, Meal m, MealCalories c WHERE u.username = '"+ username + "' AND u.logTime >= TRUNC(SYSDATE, 'DAY') AND u.logTime < TRUNC(SYSDATE, 'DAY') + 7 " +
                "AND u.mealID = m.mealID AND m.carbohydrates = c.carbohydrates AND m.fat = c.fat AND m.protein = c.protein");
        QueryExecutable qe2 = new QueryExecutable(map);
        JSONArray res2 = qe2.run();
        String sumCal;
        try {
            sumCal = res2.getJSONObject(0).getString("SUM(CALORIESPERSERVING)");
        } catch (JSONException e) {
            sumCal = "Not Available";
            e.printStackTrace();
        }
        if (sumCal.equals("null")) {
            sumCal = "0";
        }
        weeklySum.setText("Your current calories: " + sumCal);
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
        map.put("extra", "SELECT age, weight, height FROM UserPerson WHERE username = '" + username + "'");
        QueryExecutable qe = new QueryExecutable(map);
        JSONArray res = qe.run();

        try{
            JSONObject o = res.getJSONObject(0);
            age.setText(o.getString("AGE"));
            weight.setText(o.getString("WEIGHT"));
            height.setText(o.getString("HEIGHT"));
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void updateUserDetails(View view){
        if (age.getText().toString().equals("") || weight.getText().toString().equals("") || height.getText().toString().equals("")) {
            return;
        }

        Map<String,Object> map = new LinkedHashMap<>();
        map.put("query_type", "special_change");
        map.put("extra", "UPDATE userPerson SET age = " + age.getText().toString() + ", weight = " + weight.getText().toString() + ", height = " + height.getText().toString() + " WHERE username = '" + username + "'");
        QueryExecutable qe = new QueryExecutable(map);
        qe.run();

        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
        getCurrentFocus().clearFocus();

        Toast.makeText(this, "Successfully updated user details!", Toast.LENGTH_SHORT).show();
    }

    public void logOut(View view){
        SharedPreferences sp = getSharedPreferences(getString(R.string.user_login_data_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor spe = sp.edit();
        spe.putBoolean("loggedIn", false);
        spe.putString("type", "");
        spe.putString("username", "");
        spe.putString("password", "");
        spe.commit();

        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

}