package com.example.lyfestyletracker;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExerciseLog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExerciseLog extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters
    private String username;

    private View thisView;

    public ExerciseLog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param username Username of current user.
     * @return A new instance of fragment ExerciseLog.
     */
    // TODO: Rename and change types and number of parameters
    public static ExerciseLog newInstance(String username) {
        ExerciseLog fragment = new ExerciseLog();
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
        thisView = inflater.inflate(R.layout.fragment_exercise_log, container, false);
        populateTable("");

        thisView.findViewById(R.id.exercise_log_container).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                thisView.findViewById(R.id.exercise_log_search_view).clearFocus();
                view.performClick();
                return true;
            }
        });

        thisView.findViewById(R.id.exercise_log_search_view).setOnClickListener(this);

        ((SearchView) thisView.findViewById(R.id.exercise_log_search_view)).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                loadSearch(s);
                thisView.findViewById(R.id.exercise_log_search_view).clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                loadSearch(s);
                return true;

            }
        });

        return thisView;
    }

    private void populateTable(String queryCondition) {
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("query_type", "special");
        map.put("extra", "SELECT ele.logtime, w.description, w.caloriesburnt, w.timeworkout FROM workout w, exerciselogentry ele, userexerciselog uel WHERE w.workoutid = ele.workoutid AND w.workoutid = uel.workoutid AND ele.logtime = uel.logtime AND uel.username = '"+ username + "' " + queryCondition + " ORDER BY uel.logtime DESC");

        QueryExecutable qe = new QueryExecutable(map);
        JSONArray ans = qe.run();
        System.out.println(ans);

        TableLayout mainTable = thisView.findViewById(R.id.exercise_log_main_table);
        mainTable.removeAllViews();

        if (ans == null) {
            return;
        }

        for (int i = 0; i < ans.length(); i++) {
            try {
                JSONObject o = ans.getJSONObject(i);
                LocalDateTime timestamp = parseTimestamp(o.getString("LOGTIME"));

                TableRow row = new TableRow(getContext());
                row.setWeightSum(1.0f);
                row.setPadding(0, 10, 0, 10);
                row.setOnClickListener(this);
                if (i % 2 == 0) {
                    row.setBackgroundColor(getContext().getColor(R.color.table_light1));
                } else {
                    row.setBackgroundColor(getContext().getColor(R.color.table_light2));
                }

                TableRow.LayoutParams paramsTime = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.28f);
                TableRow.LayoutParams paramsDescription = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.42f);
                TableRow.LayoutParams paramsCaloriesBurnt = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.15f);
                TableRow.LayoutParams paramsLength = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.15f);

                TextView textTime = new TextView(getContext());
                textTime.setText(timestamp.toString("MMM dd yyyy\nhh:mm aa", Locale.ENGLISH));
                textTime.setLayoutParams(paramsTime);
                textTime.setGravity(Gravity.CENTER_HORIZONTAL);

                TextView textDescription = new TextView(getContext());
                textDescription.setText(o.getString("DESCRIPTION"));
                textDescription.setLayoutParams(paramsDescription);

                TextView textCaloriesBurnt = new TextView(getContext());
                textCaloriesBurnt.setText(o.getString("CALORIESBURNT"));
                textCaloriesBurnt.setLayoutParams(paramsCaloriesBurnt);
                textCaloriesBurnt.setGravity(Gravity.CENTER_HORIZONTAL);

                TextView textLength = new TextView(getContext());
                textLength.setText(o.getString("TIMEWORKOUT"));
                textLength.setLayoutParams(paramsLength);
                textLength.setGravity(Gravity.CENTER_HORIZONTAL);

                row.addView(textTime);
                row.addView(textDescription);
                row.addView(textCaloriesBurnt);
                row.addView(textLength);
                mainTable.addView(row);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // parse method to parse timestamp from SQL query into a LocalDateTime object
    public LocalDateTime parseTimestamp(String s) {
        return LocalDateTime.parse(s, DateTimeFormat.forPattern("dd-MMM-yy hh.mm.ss.SSSSSS aa").withLocale(Locale.ENGLISH));
    }

    private void loadSearch(String s) {
        populateTable("AND LOWER(w.description) LIKE '%" + s.toLowerCase() + "%'");
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.exercise_log_search_view) {
            SearchView search = (SearchView) view;
            search.onActionViewExpanded();
        } else {
            System.out.println("BYE" + view.getClass());
            thisView.findViewById(R.id.exercise_log_search_view).clearFocus();
        }
    }
}













