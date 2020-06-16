package com.example.lyfestyletracker;

import android.content.Intent;
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
import org.w3c.dom.Text;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExerciseWorkoutPlans#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExerciseWorkoutPlans extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String username;
    private String type;

    private View thisView;
    private String searchTerm = "";
    private String sortBy = "planID";
    private String sortByOrder = "DESC";
    private String searchQuery;
    private int numOfTables;
    private ArrayList<String> colNames;

    public ExerciseWorkoutPlans() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExerciseWorkoutPlans.
     */
    // TODO: Rename and change types and number of parameters
    public static ExerciseWorkoutPlans newInstance(String param1, String param2) {
        ExerciseWorkoutPlans fragment = new ExerciseWorkoutPlans();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        colNames = new ArrayList<>();
        if (getArguments() != null) {
            username = getArguments().getString(ARG_PARAM1);
            type = getArguments().getString(ARG_PARAM2);
        }

        if (type.equals("workout")){
            numOfTables = 3;
            colNames.add("planID");
            colNames.add("createdByUsername");
            colNames.add("exercisePerWeek");
            searchQuery = "SELECT planId, createdByUsername, exercisePerWeek FROM Plan, WorkoutPlan WHERE createdByUsername = '" + username + "' AND planId = workoutPlanId AND LOWER(createdByUsername)";

        }else if (type.equals("diet")){
            numOfTables = 3;
            colNames.add("planID");
            colNames.add("createdByUsername");
            colNames.add("weeklyCalories");
            searchQuery = "SELECT planId, createdByUsername, weeklyCalories FROM Plan, Diet WHERE createdByUsername = '" + username + "' AND planId = dietId AND LOWER(createdByUsername)";


        }else if (type.equals("suggested")){
            numOfTables = 3;
            colNames.add("planID");
            colNames.add("createdByUsername");
            colNames.add("logTime");
            sortBy = "logTime";

            searchQuery = "SELECT planID, consultantUsername as createdByUsername, logTime FROM ConsultantSuggestsPlan WHERE userUsername = '" + username + "' AND LOWER(consultantUsername) ";

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        thisView = inflater.inflate(R.layout.fragment_exercise_workout_plans, container, false);
        populateTable();

        thisView.findViewById(R.id.exercise_plans_container).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                thisView.findViewById(R.id.exercise_plans_search_view).clearFocus();
                view.performClick();
                return true;
            }
        });

        thisView.findViewById(R.id.exercise_plans_search_view).setOnClickListener(this);

        ((SearchView) thisView.findViewById(R.id.exercise_plans_search_view)).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchTerm = s;
                populateTable();
                thisView.findViewById(R.id.exercise_plans_search_view).clearFocus();
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

        thisView.findViewById(R.id.exercise_plans_header_1).setOnClickListener(this);
        thisView.findViewById(R.id.exercise_plans_header_2).setOnClickListener(this);
        thisView.findViewById(R.id.exercise_plans_header_3).setOnClickListener(this);

        ((TextView)thisView.findViewById(R.id.exercise_plans_header_1)).setText(colNames.get(0));
        ((TextView)thisView.findViewById(R.id.exercise_plans_header_2)).setText(colNames.get(1));
        ((TextView)thisView.findViewById(R.id.exercise_plans_header_3)).setText(colNames.get(2));

        return thisView;
    }

    private void populateTable() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("query_type", "special");
        map.put("extra", searchQuery + "LIKE '%" + searchTerm.toLowerCase() + "%' ORDER BY " + sortBy + " " + sortByOrder);
        QueryExecutable qe = new QueryExecutable(map);
        JSONArray ans = qe.run();


        System.out.println(ans);

        TableLayout mainTable = thisView.findViewById(R.id.exercise_plans_main_table);
        mainTable.removeAllViews();

        if (ans == null) {
            return;
        }


        for (int i = 0; i < ans.length(); i++) {
            try {
                JSONObject o = ans.getJSONObject(i);

                TableRow row = new TableRow(getContext());
                row.setWeightSum(1.0f);
                row.setPadding(0, 10, 0, 10);
                row.setOnClickListener(this);
                row.setTag(o.getString(colNames.get(0).toUpperCase()));
                if (i % 2 == 0) {
                    row.setBackgroundColor(getContext().getColor(R.color.table_light1));
                } else {
                    row.setBackgroundColor(getContext().getColor(R.color.table_light2));
                }

                TableRow.LayoutParams params1 = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.28f);
                TableRow.LayoutParams params2 = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.36f);
                TableRow.LayoutParams params3 = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.18f);



                TextView text1= new TextView(getContext());
                text1.setText(o.getString(colNames.get(0).toUpperCase()));
                text1.setLayoutParams(params1);

                TextView text2 = new TextView(getContext());
                text2.setText(o.getString(colNames.get(1).toUpperCase()));
                text2.setLayoutParams(params2);
                text2.setGravity(Gravity.CENTER_HORIZONTAL);


                TextView text3 = new TextView(getContext());
                if (type.equals("suggested")){
                    LocalDateTime timestamp = TimestampUtility.parseDatabaseTimestamp(o.getString(colNames.get(2).toUpperCase()));
                    text3.setText(timestamp.toString("MMM dd yyyy\nhh:mm aa", Locale.ENGLISH));

                }else {
                    text3.setText(o.getString(colNames.get(2).toUpperCase()));
                }

                text3.setLayoutParams(params3);
                text3.setGravity(Gravity.CENTER_HORIZONTAL);



                row.addView(text1);
                row.addView(text2);
                row.addView(text3);
                mainTable.addView(row);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    // utility method for sorting
    private void clearHeaderDrawablesAndTags() {
        TextView t1 = thisView.findViewById(R.id.exercise_plans_header_1);
        TextView t2 = thisView.findViewById(R.id.exercise_plans_header_2);
        TextView t3 = thisView.findViewById(R.id.exercise_plans_header_3);

        t1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        t2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        t3.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        t1.setTag("");
        t2.setTag("");
        t3.setTag("");
    }

    @Override
    public void onResume() {
        super.onResume();
        populateTable();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.exercise_plans_search_view) {
            SearchView search = (SearchView) view;
            search.onActionViewExpanded();
        } else {
            thisView.findViewById(R.id.exercise_plans_search_view).clearFocus();
        }

        if (view.getId() == R.id.exercise_plans_header_1) {
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
            sortBy = colNames.get(0);
            populateTable();
        } else if (view.getId() == R.id.exercise_plans_header_2 || view.getId() == R.id.exercise_plans_header_3) {
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

            if (view.getId() == R.id.exercise_plans_header_2) {
                sortBy = colNames.get(1);
            } else if (view.getId() == R.id.exercise_plans_header_3) {
                sortBy = colNames.get(2);
            }

            populateTable();
        }

        if (view.getClass().equals(TableRow.class)) {

            if (type.equals("workout")){

                Intent intent = new Intent(getActivity(), WorkoutPlan.class);
                intent.putExtra("username", username);
                intent.putExtra("workoutId", Integer.parseInt((String) view.getTag()));
                startActivity(intent);
            }else if(type.equals("diet")){
                Intent intent = new Intent(getActivity(), DietPlan.class);
                intent.putExtra("username", username);
                intent.putExtra("dietId", Integer.parseInt((String) view.getTag()));
                startActivity(intent);

            }else {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("query_type", "special");
                map.put("extra", "Select * from Diet where dietId =" + view.getTag());
                QueryExecutable qe = new QueryExecutable(map);
                JSONArray ans = qe.run();

                if (ans.length() > 0){
                    Intent intent = new Intent(getActivity(), DietPlan.class);
                    intent.putExtra("username", username);
                    intent.putExtra("dietId", Integer.parseInt((String) view.getTag()));
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), WorkoutPlan.class);
                    intent.putExtra("username", username);
                    intent.putExtra("workoutId", Integer.parseInt((String) view.getTag()));
                    startActivity(intent);
                }
            }

        }
    }
}