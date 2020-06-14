package com.example.lyfestyletracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.lyfestyletracker.web.QueryExecutable;
import com.google.android.material.snackbar.Snackbar;

import org.joda.time.LocalDateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class AddWorkout extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    //private ArrayList<EditText> cardioTexts;
    //private ArrayList<EditText> sportTexts;
    private Switch cardioSwitch;
    private Switch sportSwitch;
    private String dateResult;
    private String timeResult;
    private EditText workoutId;
    private EditText workoutDesc;
    private EditText workoutCaloriesBurnt;
    private EditText workoutLength;
    private String typeOfAdd;
    private TextView selectedDateLabel;
    private TextView selectedTimeLabel;
    private EditText cardioDistance;
    private EditText cardioAvgSpeed;
    private EditText sportIntensity;
    private EditText sportType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);

        typeOfAdd = getIntent().getStringExtra("type");
        cardioSwitch = (Switch) findViewById(R.id.cardio_switch);
        sportSwitch = (Switch) findViewById(R.id.sport_switch);
        workoutId = (EditText) findViewById(R.id.enter_workout_id);
        workoutDesc = (EditText) findViewById(R.id.enter_description);
        workoutCaloriesBurnt = (EditText) findViewById(R.id.enter_calories_burnt);
        workoutLength = (EditText) findViewById(R.id.enter_workout_length);
        selectedDateLabel = (TextView) findViewById(R.id.selected_date_label);
        selectedTimeLabel = (TextView) findViewById(R.id.selected_time_label);
        cardioDistance = (EditText) findViewById(R.id.enter_cardio_distance);
        cardioAvgSpeed = (EditText) findViewById(R.id.enter_cardio_avg_speed);
        sportIntensity = (EditText) findViewById(R.id.enter_sport_intensity);
        sportType = (EditText) findViewById(R.id.enter_sport_type);
        dateResult = "";
        timeResult = "";

        final ConstraintLayout cardioLayout = (ConstraintLayout) findViewById(R.id.cardio_layout);
        final ConstraintLayout sportLayout = (ConstraintLayout) findViewById(R.id.sport_layout);

        cardioLayout.setVisibility(View.GONE);
        sportLayout.setVisibility(View.GONE);

        cardioSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    cardioLayout.setVisibility(View.VISIBLE);
                    if (sportSwitch.isChecked()) {
                        sportSwitch.setChecked(false);
                    }
                } else {
                    cardioLayout.setVisibility(View.GONE);
                }
            }
        });

        sportSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    sportLayout.setVisibility(View.VISIBLE);
                    if (cardioSwitch.isChecked()) {
                        cardioSwitch.setChecked(false);
                    }
                } else {
                    sportLayout.setVisibility(View.GONE);
                }
            }
        });

        if (typeOfAdd != null && typeOfAdd.equals("prefill")) {
            prefillWithExtras();
        }
    }

    public void createDateClicked(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String monthString = Integer.toString(month + 1);
        String dayOfMonthString = Integer.toString(dayOfMonth);

        if (month < 10) {
            monthString = "0" + monthString;
        }

        if (dayOfMonth < 10) {
            dayOfMonthString = "0" + dayOfMonthString;
        }

        dateResult = year + "-" + monthString + "-" + dayOfMonthString;
        selectedDateLabel.setText(dateResult);
    }

    public void createTimeClicked(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                this, 0, 0, true);
        timePickerDialog.show();
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String hourOfDayString = Integer.toString(hourOfDay);
        String minuteString = Integer.toString(minute);

        if (hourOfDay < 10) {
            hourOfDayString = "0" + hourOfDayString;
        }

        if (minute < 10) {
            hourOfDayString = "0" + minuteString;
        }

        timeResult = hourOfDayString + ":" + minuteString + ":" + "00";
        selectedTimeLabel.setText(hourOfDayString + ":" + minuteString);
    }

    public void addWorkout(View view) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("query_type", "special_change");

        if (workoutId.getText().toString().equals("")) {
            Snackbar.make(view, "Invalid WorkoutId", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else if (workoutDesc.getText().toString().equals("")) {
            Snackbar.make(view, "Invalid Workout Description", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else if (workoutCaloriesBurnt.getText().toString().equals("")) {
            Snackbar.make(view, "Invalid number of Calories Burnt", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else if (workoutLength.getText().toString().equals("")) {
            Snackbar.make(view, "Invalid Workout Length", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else if (dateResult.equals("") && typeOfAdd != "plan") {
            Snackbar.make(view, "Invalid Date", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else if (timeResult.equals("") && typeOfAdd != "plan") {
            Snackbar.make(view, "Invalid Time", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else {
            if (cardioSwitch.isChecked()) {
                if (cardioDistance.getText().toString().equals("") || cardioAvgSpeed.getText().toString().equals("")) {
                    Snackbar.make(view, "Invalid Cardio Input", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }

            if (sportSwitch.isChecked()) {
                if (sportIntensity.getText().toString().equals("") || sportType.getText().toString().equals("")) {
                    Snackbar.make(view, "Invalid Sport Input", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
            }

            map.put("extra", "Insert Into Workout Values (" + workoutId.getText().toString() + ", '"
                    + workoutDesc.getText().toString() + "', " + workoutCaloriesBurnt.getText().toString() + ", "
                    + workoutLength.getText().toString() + ")");

            QueryExecutable qe = new QueryExecutable(map);
            JSONArray res = qe.run();


            if (cardioSwitch.isChecked()) {
                map.clear();
                map.put("query_type", "special_change");
                map.put("extra", "Insert Into Cardio Values(" + workoutId.getText().toString() + ", "
                        + cardioDistance.getText().toString() + ", " + cardioAvgSpeed.getText().toString() + ")");
                qe = new QueryExecutable(map);
                res = qe.run();

            }

            if (sportSwitch.isChecked()) {
                map.clear();
                map.put("query_type", "special_change");
                map.put("extra", "Insert Into SPORT Values(" + workoutId.getText().toString() + ", "
                        + sportIntensity.getText().toString() + ", '" + sportType.getText().toString() + "')");
                qe = new QueryExecutable(map);
                res = qe.run();
            }

            if (typeOfAdd != "plan") {
                map.clear();
                map.put("query_type", "special_change");
                map.put("extra", "Insert into ExerciseLogEntry Values(" + workoutId.getText().toString()
                        + ", TO_TIMESTAMP('" + dateResult + " " + timeResult + "', 'YYYY-MM-DD HH24:MI:SS'))");

                qe = new QueryExecutable(map);
                res = qe.run();

                map.clear();
                map.put("query_type", "special_change");
                map.put("extra", "Insert into UserExerciseLog Values('" + getIntent().getStringExtra("username")
                        + "', TO_TIMESTAMP('" + dateResult + " " + timeResult + "', 'YYYY-MM-DD HH24:MI:SS')," + workoutId.getText().toString() + ")");

                qe = new QueryExecutable(map);
                res = qe.run();
            } else {
                addToPlan();
            }

            Snackbar.make(view, "Successfully added the workout", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        }

    }

    private void prefillWithExtras() {
        String username = getIntent().getStringExtra("username");
        String id = getIntent().getStringExtra("workoutId");
        String timestamp = getIntent().getStringExtra("timestampString");
        String exerciseType = "";

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("query_type", "special");
        map.put("extra", "SELECT w.workoutid, ele.logtime, w.description, w.caloriesburnt, w.timeworkout, c.distance, c.avgspeed FROM workout w, exerciselogentry ele, userexerciselog uel, cardio c WHERE w.workoutid = ele.workoutid AND w.workoutid = uel.workoutid AND ele.logtime = uel.logtime AND w.workoutid = c.workoutid AND uel.username = '" + username + "' AND w.workoutid = '" + id + "' AND ele.logtime = TO_TIMESTAMP('" + timestamp + "', 'YYYY-MM-DD HH24:MI:SS')");
        QueryExecutable qe = new QueryExecutable(map);
        JSONArray cardioAns = qe.run();

        map = new LinkedHashMap<>();
        map.put("query_type", "special");
        map.put("extra", "SELECT w.workoutid, ele.logtime, w.description, w.caloriesburnt, w.timeworkout, s.intensity, s.sporttype FROM workout w, exerciselogentry ele, userexerciselog uel, sport s WHERE w.workoutid = ele.workoutid AND w.workoutid = uel.workoutid AND ele.logtime = uel.logtime AND w.workoutid = s.workoutid AND uel.username = '" + username + "' AND w.workoutid = '" + id + "' AND ele.logtime = TO_TIMESTAMP('" + timestamp + "', 'YYYY-MM-DD HH24:MI:SS')");
        qe = new QueryExecutable(map);
        JSONArray sportAns = qe.run();

        map = new LinkedHashMap<>();
        map.put("query_type", "special");
        map.put("extra", "SELECT w.workoutid, ele.logtime, w.description, w.caloriesburnt, w.timeworkout FROM workout w, exerciselogentry ele, userexerciselog uel WHERE w.workoutid = ele.workoutid AND w.workoutid = uel.workoutid AND ele.logtime = uel.logtime AND uel.username = '" + username + "' AND w.workoutid = '" + id + "' AND ele.logtime = TO_TIMESTAMP('" + timestamp + "', 'YYYY-MM-DD HH24:MI:SS')");
        qe = new QueryExecutable(map);
        JSONArray regularAns = qe.run();

        System.out.println("CARDIO: " + cardioAns);
        System.out.println("SPORT: " + sportAns);
        System.out.println("REGULAR:" + regularAns);

        JSONArray finalAns;

        if (cardioAns.length() == 0 && sportAns.length() == 0) {
            exerciseType = "";
            finalAns = regularAns;
        } else if (cardioAns.length() != 0) {
            exerciseType = "cardio";
            finalAns = cardioAns;
        } else if (sportAns.length() != 0) {
            exerciseType = "sport";
            finalAns = sportAns;
        } else {
            // should never happen, but just in case our database is messed up
            exerciseType = "";
            finalAns = regularAns;
        }

        try {
            JSONObject o = finalAns.getJSONObject(0);
            workoutId.setText(id);
            workoutDesc.setText(o.getString("DESCRIPTION"));
            workoutCaloriesBurnt.setText(o.getString("CALORIESBURNT"));
            workoutLength.setText(o.getString("TIMEWORKOUT"));

            if (exerciseType.equals("cardio")) {
                cardioSwitch.setChecked(true);
                cardioDistance.setText(o.getString("DISTANCE"));
                cardioAvgSpeed.setText(o.getString("AVGSPEED"));
            } else if (exerciseType.equals("sport")) {
                sportSwitch.setChecked(true);
                sportIntensity.setText(o.getString("INTENSITY"));
                sportType.setText(o.getString("SPORTTYPE"));
            }

            LocalDateTime ldt = TimestampUtility.parseDatabaseTimestamp(o.getString("LOGTIME"));
            dateResult = ldt.toString("yyyy-MM-dd", Locale.ENGLISH);
            selectedDateLabel.setText(dateResult);
            timeResult = ldt.toString("HH:mm:00", Locale.ENGLISH);
            selectedTimeLabel.setText(ldt.toString("HH:mm", Locale.ENGLISH));

            ((TextView) findViewById(R.id.add_exercise_submit_button)).setText(R.string.update_existing_exercise);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void addToPlan() {
        //ADD THIS
    }


}