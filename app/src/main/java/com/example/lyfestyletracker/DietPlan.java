package com.example.lyfestyletracker;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class DietPlan extends AppCompatActivity implements View.OnClickListener {


    private String username;
    private String searchTerm = "";
    private String sortBy = "ml.logTime";
    private String sortByOrder = "DESC";
    int dietPlanID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_plan);

        username = getIntent().getStringExtra("username");
        dietPlanID = getIntent().getIntExtra("dietId", 0);

        setTitle("Diet #" + dietPlanID);


        populateTable();

        findViewById(R.id.diet_plan_row_container).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                findViewById(R.id.diet_plan_search_view).clearFocus();
                view.performClick();
                return true;
            }
        });

        findViewById(R.id.diet_plan_search_view).setOnClickListener(this);

        ((SearchView) findViewById(R.id.diet_plan_search_view)).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchTerm = s;
                populateTable();
                findViewById(R.id.diet_plan_search_view).clearFocus();
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

        findViewById(R.id.diet_header_time).setOnClickListener(this);
        findViewById(R.id.diet_plan_description).setOnClickListener(this);
        findViewById(R.id.diet_plan_num_servings).setOnClickListener(this);
        findViewById(R.id.diet_plan_log_type).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.diet_plan_search_view) {
            SearchView search = (SearchView) view;
            search.onActionViewExpanded();
        } else {
            findViewById(R.id.diet_plan_search_view).clearFocus();
        }

        if (view.getId() == R.id.diet_header_time) {
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
            sortBy = "ml.logTime";
            populateTable();
        } else if (view.getId() == R.id.diet_plan_description || view.getId() == R.id.diet_plan_num_servings || view.getId() == R.id.diet_plan_log_type) {
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

            if (view.getId() == R.id.diet_plan_description) {
                sortBy = "m.description";
            } else if (view.getId() == R.id.diet_plan_num_servings) {
                sortBy = "m.servingSizeGrams";
            } else if (view.getId() == R.id.diet_plan_log_type) {
                sortBy = "m.type";
            }

            populateTable();
        }

        if (view.getClass().equals(TableRow.class)) {

            /*
            String workoutId = (String) view.getTag();
            TextView time = (TextView) ((TableRow) view).getChildAt(0);
            String rawTime = time.getText().toString().replace("\n", " ");
            LocalDateTime ldt = LocalDateTime.parse(rawTime, DateTimeFormat.forPattern("MMM dd yyyy hh:mm aa").withLocale(Locale.ENGLISH));
            Timestamp ts = new Timestamp(ldt.toDateTime().getMillis());
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(ts);

            Intent intent = new Intent(this, AddWorkout.class);
            intent.putExtra("username", username);
            intent.putExtra("type", "update");
            intent.putExtra("workoutId", workoutId);
            intent.putExtra("timestampString", timestamp);
            startActivity(intent);\

             */


        }
    }


    private void populateTable() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("query_type", "special");
        map.put("extra", "Select m.mealId, ml.logTime, m.description, m.servingSizeGrams, m.type From DietContainsMealLog dcm, Meal m, MealLogEntry ml WHERE dcm.dietID = " + dietPlanID +" AND ml.logTime = dcm.logTime AND ml.mealId = dcm.mealId AND LOWER(m.description) LIKE '%" + searchTerm.toLowerCase() + "%' ORDER BY " + sortBy + " " + sortByOrder);

        QueryExecutable qe = new QueryExecutable(map);
        JSONArray ans = qe.run();
        System.out.println(ans);
        System.out.println("Select m.mealId, ml.logTime, m.description, m.servingSizeGrams, m.type From DietContainsMealLog dcm, Meal m, MealLogEntry ml WHERE dcm.dietID = " + dietPlanID +" AND ml.logTime = dcm.logTime AND ml.mealId = dcm.mealId AND LOWER(m.description) LIKE '%" + searchTerm.toLowerCase() + "%' ORDER BY " + sortBy + " " + sortByOrder);

        TableLayout mainTable = this.findViewById(R.id.diet_plan_main_table);
        mainTable.removeAllViews();

        if (ans == null) {
            return;
        }

        for (int i = 0; i < ans.length(); i++) {
            try {
                JSONObject o = ans.getJSONObject(i);

                LocalDateTime timestamp = parseTimestamp(o.getString("LOGTIME"));

                TableRow row = new TableRow(this);
                row.setWeightSum(1.0f);
                row.setPadding(0, 10, 0, 10);
                row.setOnClickListener(this);
                row.setTag(o.getString("MEALID"));
                if (i % 2 == 0) {
                    row.setBackgroundColor(this.getColor(R.color.table_light1));
                } else {
                    row.setBackgroundColor(this.getColor(R.color.table_light2));
                }

                TableRow.LayoutParams paramsTime = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.28f);
                TableRow.LayoutParams paramsDescription = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.36f);
                TableRow.LayoutParams paramsCaloriesBurnt = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.18f);
                TableRow.LayoutParams paramsLength = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.18f);

                TextView textTime = new TextView(this);
                textTime.setText(timestamp.toString("MMM dd yyyy\nhh:mm aa", Locale.ENGLISH));
                textTime.setLayoutParams(paramsTime);
                textTime.setGravity(Gravity.CENTER_HORIZONTAL);

                TextView textDescription = new TextView(this);
                textDescription.setText(o.getString("DESCRIPTION"));
                textDescription.setLayoutParams(paramsDescription);

                TextView textServingSize = new TextView(this);
                textServingSize.setText(o.getString("SERVINGSIZEGRAMS"));
                textServingSize.setLayoutParams(paramsCaloriesBurnt);
                textServingSize.setGravity(Gravity.CENTER_HORIZONTAL);

                TextView textType = new TextView(this);
                textType.setText(o.getString("TYPE"));
                textType.setLayoutParams(paramsLength);
                textType.setGravity(Gravity.CENTER_HORIZONTAL);

                row.addView(textTime);
                row.addView(textDescription);
                row.addView(textServingSize);
                row.addView(textType);
                mainTable.addView(row);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // utility method for sorting
    private void clearHeaderDrawablesAndTags() {
        TextView id = findViewById(R.id.diet_header_time);
        TextView description = findViewById(R.id.diet_plan_description);
        TextView caloriesBurnt = findViewById(R.id.diet_plan_num_servings);
        TextView length = findViewById(R.id.diet_plan_log_type);

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

    public LocalDateTime parseTimestamp(String s) {
        return LocalDateTime.parse(s, DateTimeFormat.forPattern("dd-MMM-yy hh.mm.ss.SSSSSS aa").withLocale(Locale.ENGLISH));
    }
}