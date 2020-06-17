package com.example.lyfestyletracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.lyfestyletracker.utils.TimestampUtility;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FoodLog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodLog extends Fragment implements View.OnClickListener {

    private String username;
    private View thisView;
    private String searchTerm = "";
    private String sortBy = "uml.logtime";
    private String sortByOrder = "DESC";

    public FoodLog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param username username.
     * @return A new instance of fragment FoodLog.
     */
    // TODO: Rename and change types and number of parameters
    public static FoodLog newInstance(String username) {
        FoodLog fragment = new FoodLog();
        Bundle args = new Bundle();
        args.putString("username", username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString("username");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        thisView = inflater.inflate(R.layout.fragment_food_log, container, false);
        populateTable();

        thisView.findViewById(R.id.food_log_container).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                thisView.findViewById(R.id.food_log_search_view).clearFocus();
                view.performClick();
                return true;
            }
        });

        thisView.findViewById(R.id.food_log_search_view).setOnClickListener(this);

        ((SearchView) thisView.findViewById(R.id.food_log_search_view)).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchTerm = s;
                populateTable();
                thisView.findViewById(R.id.food_log_search_view).clearFocus();
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

        thisView.findViewById(R.id.food_log_header_time).setOnClickListener(this);
        thisView.findViewById(R.id.food_log_header_description).setOnClickListener(this);
        thisView.findViewById(R.id.food_log_header_num_servings).setOnClickListener(this);
        thisView.findViewById(R.id.food_log_type).setOnClickListener(this);

        return thisView;
    }

    public void populateTable() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("query_type", "special");
        map.put("extra", "SELECT m.mealid, uml.logTime, m.description, mle.numberOfServings, m.type FROM userMealLog uml, Meal m, MealLogEntry mle WHERE uml.username = '" + username + "' AND mle.mealId = m.mealID AND uml.mealId = mle.mealID AND uml.logTime = mle.logTime AND LOWER(m.description) LIKE '%" + searchTerm.toLowerCase() + "%' ORDER BY " + sortBy + " " + sortByOrder);

        QueryExecutable qe = new QueryExecutable(map);
        JSONArray ans = qe.run();
        System.out.println(ans);

        TableLayout mainTable = thisView.findViewById(R.id.food_log_main_table);
        mainTable.removeAllViews();
        if (ans == null) {
            return;
        }

        for (int i = 0; i < ans.length(); i++) {
            try {
                JSONObject o = ans.getJSONObject(i);
                LocalDateTime timestamp = TimestampUtility.parseDatabaseTimestamp(o.getString("LOGTIME"));

                TableRow row = new TableRow(getContext());
                row.setWeightSum(1.0f);
                row.setPadding(0, 10, 0, 10);
                row.setOnClickListener(this);
                row.setTag(o.getString("MEALID"));
                if (i % 2 == 0) {
                    row.setBackgroundColor(getContext().getColor(R.color.table_light1));
                } else {
                    row.setBackgroundColor(getContext().getColor(R.color.table_light2));
                }

                TableRow.LayoutParams paramsTime = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.28f);
                TableRow.LayoutParams paramsDescription = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.36f);
                TableRow.LayoutParams paramsServings = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.18f);
                TableRow.LayoutParams paramsType = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.18f);

                TextView textTime = new TextView(getContext());
                textTime.setText(timestamp.toString("MMM dd yyyy\nhh:mm aa", Locale.ENGLISH));
                textTime.setLayoutParams(paramsTime);
                textTime.setGravity(Gravity.CENTER_HORIZONTAL);

                TextView textDescription = new TextView(getContext());
                textDescription.setText(o.getString("DESCRIPTION"));
                textDescription.setLayoutParams(paramsDescription);

                TextView textservings = new TextView(getContext());
                textservings.setText(o.getString("NUMBEROFSERVINGS"));
                textservings.setLayoutParams(paramsServings);
                textservings.setGravity(Gravity.CENTER_HORIZONTAL);

                TextView texttype = new TextView(getContext());
                texttype.setText(o.getString("TYPE"));
                texttype.setLayoutParams(paramsType);
                texttype.setGravity(Gravity.CENTER_HORIZONTAL);

                row.addView(textTime);
                row.addView(textDescription);
                row.addView(textservings);
                row.addView(texttype);
                mainTable.addView(row);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.food_log_search_view) {
            SearchView search = (SearchView) view;
            search.onActionViewExpanded();
        } else {
            thisView.findViewById(R.id.food_log_search_view).clearFocus();
        }

        if (view.getId() == R.id.food_log_header_time) {
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
            sortBy = "uml.logtime";
            populateTable();
        } else if (view.getId() == R.id.food_log_header_description || view.getId() == R.id.food_log_header_num_servings || view.getId() == R.id.food_log_type) {
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

            if (view.getId() == R.id.food_log_header_description) {
                sortBy = "m.description";
            } else if (view.getId() == R.id.food_log_header_num_servings) {
                sortBy = "mle.numberOfServings";
            } else if (view.getId() == R.id.food_log_type) {
                sortBy = "m.type";
            }

            populateTable();
        }

        if (view.getClass().equals(TableRow.class)) {
            String mealId = (String) view.getTag();
            TextView time = (TextView) ((TableRow) view).getChildAt(0);
            String rawTime = time.getText().toString().replace("\n", " ");
            LocalDateTime ldt = LocalDateTime.parse(rawTime, DateTimeFormat.forPattern("MMM dd yyyy hh:mm aa").withLocale(Locale.ENGLISH));
            Timestamp ts = new Timestamp(ldt.toDateTime().getMillis());
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(ts);

            Intent intent = new Intent(getActivity(), AddMeal.class);
            intent.putExtra("username", username);
            intent.putExtra("type", "update");
            intent.putExtra("mealId", mealId);
            intent.putExtra("timestampString", timestamp);
            startActivity(intent);
        }
    }

    // utility method for sorting
    private void clearHeaderDrawablesAndTags() {
        TextView time = thisView.findViewById(R.id.food_log_header_time);
        TextView description = thisView.findViewById(R.id.food_log_header_description);
        TextView totalCalories = thisView.findViewById(R.id.food_log_header_num_servings);
        TextView type = thisView.findViewById(R.id.food_log_type);

        time.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        description.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        totalCalories.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        type.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        time.setTag("");
        description.setTag("");
        totalCalories.setTag("");
        type.setTag("");
    }

    @Override
    public void onResume() {
        super.onResume();
        populateTable();
    }
}