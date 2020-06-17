package com.example.lyfestyletracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.lyfestyletracker.web.QueryExecutable;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class WorkoutPlan extends AppCompatActivity implements View.OnClickListener {


    private String username;
    private String searchTerm = "";
    private String sortBy = "w.workoutId";
    private String sortByOrder = "DESC";
    int workoutPlanID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_plan);

        username = getIntent().getStringExtra("username");
        workoutPlanID = getIntent().getIntExtra("workoutId", 0);

        setTitle("Workout Plan #" + workoutPlanID);


        populateTable();

        findViewById(R.id.workout_plan_row_container).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                findViewById(R.id.workout_plan_search_view).clearFocus();
                view.performClick();
                return true;
            }
        });

        findViewById(R.id.workout_plan_search_view).setOnClickListener(this);

        ((SearchView) findViewById(R.id.workout_plan_search_view)).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchTerm = s;
                populateTable();
                findViewById(R.id.workout_plan_search_view).clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.equals("")) {
                    searchTerm = s;
                    populateTable();
                    return true;
                }
                return false;
            }
        });

        findViewById(R.id.workout_plan_id).setOnClickListener(this);
        findViewById(R.id.workout_plan_description).setOnClickListener(this);
        findViewById(R.id.workout_plan_calories_burnt).setOnClickListener(this);
        findViewById(R.id.workout_plan_time).setOnClickListener(this);

        findViewById(R.id.fab_workout_plan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WorkoutPlan.this, AddWorkout.class);
                intent.putExtra("username", username);
                intent.putExtra("planID", workoutPlanID);
                intent.putExtra("type", "plan");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.workout_plan_search_view) {
            SearchView search = (SearchView) view;
            search.onActionViewExpanded();
        } else {
            findViewById(R.id.workout_plan_search_view).clearFocus();
        }

        if (view.getId() == R.id.workout_plan_id) {
            TextView v = (TextView) view;
            if (v.getTag().equals("desc")) {
                // currently descending, want to sort ascending
                clearHeaderDrawablesAndTags();
                v.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.arrow_up_float, 0, 0, 0);
                v.setTag("asc");
                sortByOrder = "ASC";
            } else {
                // current ascending, want to sort descending OR default (ie. just started to sort by time)
                clearHeaderDrawablesAndTags();
                v.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.arrow_down_float, 0, 0, 0);
                v.setTag("desc");
                sortByOrder = "DESC";
            }
            sortBy = "w.workoutId";
            populateTable();
        } else if (view.getId() == R.id.workout_plan_description || view.getId() == R.id.workout_plan_calories_burnt || view.getId() == R.id.workout_plan_time) {
            TextView v = (TextView) view;
            if (v.getTag().equals("asc")) {
                // current ascending, want to sort descending
                clearHeaderDrawablesAndTags();
                v.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.arrow_down_float, 0, 0, 0);
                v.setTag("desc");
                sortByOrder = "DESC";
            } else {
                // currently descending, want to sort ascending OR default (ie. just started to sort by description)
                clearHeaderDrawablesAndTags();
                v.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.arrow_up_float, 0, 0, 0);
                v.setTag("asc");
                sortByOrder = "ASC";
            }

            if (view.getId() == R.id.workout_plan_description) {
                sortBy = "w.description";
            } else if (view.getId() == R.id.workout_plan_calories_burnt) {
                sortBy = "w.caloriesburnt";
            } else if (view.getId() == R.id.workout_plan_time) {
                sortBy = "w.timeworkout";
            }

            populateTable();
        }

        if (view.getClass().equals(TableRow.class)) {
            String workoutId = (String) view.getTag();
            LocalDateTime ld = new LocalDateTime();



            Intent intent = new Intent(this, AddWorkout.class);
            intent.putExtra("username", username);
            intent.putExtra("type", "update");
            intent.putExtra("workoutId", workoutId);
            intent.putExtra("consultant", getIntent().getBooleanExtra("consultant", false));
            intent.putExtra("timestampString", ld.toString("yyyy-MM-dd HH:mm:ss"));

            if (getIntent().getBooleanExtra("fromConsultant", false)){
                intent.putExtra("fromConsultant", true);
            }
            startActivity(intent);

        }
    }


    private void populateTable() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("query_type", "special");
        map.put("extra", "SELECT w.workoutid, w.description, w.caloriesburnt, w.timeworkout FROM workout w, WorkoutPlanContainsWorkout wpc WHERE wpc.workoutPlanId = " + workoutPlanID + " AND w.workoutID = wpc.workoutId AND LOWER(w.description) LIKE '%" + searchTerm.toLowerCase() + "%' ORDER BY " + sortBy + " " + sortByOrder);

        QueryExecutable qe = new QueryExecutable(map);
        JSONArray ans = qe.run();
        System.out.println(ans);

        TableLayout mainTable = this.findViewById(R.id.workout_plan_main_table);
        mainTable.removeAllViews();

        if (ans == null) {
            return;
        }

        for (int i = 0; i < ans.length(); i++) {
            try {
                JSONObject o = ans.getJSONObject(i);

                TableRow row = new TableRow(this);
                row.setWeightSum(1.0f);
                row.setPadding(0, 10, 0, 10);
                row.setOnClickListener(this);
                row.setTag(o.getString("WORKOUTID"));
                if (i % 2 == 0) {
                    row.setBackgroundColor(this.getColor(R.color.table_light1));
                } else {
                    row.setBackgroundColor(this.getColor(R.color.table_light2));
                }

                TableRow.LayoutParams paramsTime = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.28f);
                TableRow.LayoutParams paramsDescription = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.36f);
                TableRow.LayoutParams paramsCaloriesBurnt = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.18f);
                TableRow.LayoutParams paramsLength = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.18f);

                TextView textWorkoutID = new TextView(this);
                textWorkoutID.setText(o.getString("WORKOUTID"));
                textWorkoutID.setLayoutParams(paramsTime);
                textWorkoutID.setGravity(Gravity.CENTER_HORIZONTAL);

                TextView textDescription = new TextView(this);
                textDescription.setText(o.getString("DESCRIPTION"));
                textDescription.setLayoutParams(paramsDescription);

                TextView textCaloriesBurnt = new TextView(this);
                textCaloriesBurnt.setText(o.getString("CALORIESBURNT"));
                textCaloriesBurnt.setLayoutParams(paramsCaloriesBurnt);
                textCaloriesBurnt.setGravity(Gravity.CENTER_HORIZONTAL);

                TextView textLength = new TextView(this);
                textLength.setText(o.getString("TIMEWORKOUT"));
                textLength.setLayoutParams(paramsLength);
                textLength.setGravity(Gravity.CENTER_HORIZONTAL);

                row.addView(textWorkoutID);
                row.addView(textDescription);
                row.addView(textCaloriesBurnt);
                row.addView(textLength);
                mainTable.addView(row);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // utility method for sorting
    private void clearHeaderDrawablesAndTags() {
        TextView id = findViewById(R.id.workout_plan_id);
        TextView description = findViewById(R.id.workout_plan_description);
        TextView caloriesBurnt = findViewById(R.id.workout_plan_calories_burnt);
        TextView length = findViewById(R.id.workout_plan_time);

        id.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        description.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        caloriesBurnt.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        length.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        id.setTag("");
        description.setTag("");
        caloriesBurnt.setTag("");
        length.setTag("");
    }

    @Override
    public void onResume() {
        super.onResume();
        populateTable();
    }
}