package com.example.lyfestyletracker;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lyfestyletracker.utils.TimestampUtility;
import com.example.lyfestyletracker.web.QueryExecutable;
import com.google.android.material.snackbar.Snackbar;

import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class AddWorkout extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private String username;
    private Switch cardioSwitch;
    private Switch sportSwitch;
    private String dateResult;
    private String timeResult;
    private int workoutId;
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
    private ConstraintLayout cardioLayout;
    private ConstraintLayout sportLayout;
    private HashMap<String, String> prevValues = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);

        username = getIntent().getStringExtra("username");
        typeOfAdd = getIntent().getStringExtra("type") != null ? getIntent().getStringExtra("type") : "new";
        cardioSwitch = (Switch) findViewById(R.id.cardio_switch);
        sportSwitch = (Switch) findViewById(R.id.sport_switch);
        workoutDesc = (EditText) findViewById(R.id.enter_description);
        workoutCaloriesBurnt = (EditText) findViewById(R.id.enter_calories_burnt);
        workoutLength = (EditText) findViewById(R.id.enter_workout_length);
        selectedDateLabel = (TextView) findViewById(R.id.selected_date_label);
        selectedTimeLabel = (TextView) findViewById(R.id.selected_time_label);
        cardioDistance = (EditText) findViewById(R.id.enter_cardio_distance);
        cardioAvgSpeed = (EditText) findViewById(R.id.enter_cardio_avg_speed);
        sportIntensity = (EditText) findViewById(R.id.enter_sport_intensity);
        sportType = (EditText) findViewById(R.id.enter_sport_type);
        cardioLayout = (ConstraintLayout) findViewById(R.id.cardio_layout);
        sportLayout = (ConstraintLayout) findViewById(R.id.sport_layout);
        dateResult = "";
        timeResult = "";

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

        findViewById(R.id.delete_exercise_submit_button).setVisibility(View.GONE);
        findViewById(R.id.update_exercise_submit_button).setVisibility(View.GONE);

        if (typeOfAdd.equals("update")) {
            findViewById(R.id.delete_exercise_submit_button).setVisibility(View.VISIBLE);
            findViewById(R.id.update_exercise_submit_button).setVisibility(View.VISIBLE);
            findViewById(R.id.add_exercise_submit_button).setEnabled(false);
            prefillWithExtras();
        }

        if (getIntent().getBooleanExtra("consultant", false)) {
            findViewById(R.id.add_exercise_submit_button).setEnabled(false);
            findViewById(R.id.add_date_button).setEnabled(false);
            findViewById(R.id.add_time_button).setEnabled(false);
            findViewById(R.id.delete_exercise_submit_button).setVisibility(View.GONE);
        }

        if (getIntent().getBooleanExtra("fromConsultant", false)) {
            findViewById(R.id.add_exercise_submit_button).setEnabled(true);
            findViewById(R.id.add_date_button).setEnabled(true);
            findViewById(R.id.add_time_button).setEnabled(true);
            findViewById(R.id.update_exercise_submit_button).setEnabled(false);
            findViewById(R.id.delete_exercise_submit_button).setVisibility(View.GONE);
        }

        if (typeOfAdd.equals("plan")) {
            findViewById(R.id.add_exercise_submit_button).setVisibility(View.GONE);
            findViewById(R.id.add_exercise_submit_button).setVisibility(View.GONE);
            findViewById(R.id.add_to_workout_plan).setVisibility(View.VISIBLE);
            findViewById(R.id.add_date_button).setEnabled(false);
            findViewById(R.id.add_time_button).setEnabled(false);
            findViewById(R.id.delete_exercise_submit_button).setVisibility(View.GONE);
        }
    }

    public void createDateClicked(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String monthString = month < 10 ? "0" + (month + 1) : Integer.toString(month + 1);
        String dayOfMonthString = dayOfMonth < 10 ? "0" + dayOfMonth : Integer.toString(dayOfMonth);

        dateResult = year + "-" + monthString + "-" + dayOfMonthString;
        selectedDateLabel.setText(dateResult);

        if (typeOfAdd.equals("update") && (!dateResult.equals(prevValues.get("date")) || !timeResult.equals(prevValues.get("time")))) {
            // date and time have been changed from original prefill
            findViewById(R.id.add_exercise_submit_button).setEnabled(true);
            findViewById(R.id.update_exercise_submit_button).setEnabled(false);
            findViewById(R.id.delete_exercise_submit_button).setEnabled(false);
        } else if (typeOfAdd.equals("update") && dateResult.equals(prevValues.get("date")) && timeResult.equals(prevValues.get("time"))) {
            findViewById(R.id.add_exercise_submit_button).setEnabled(false);
            findViewById(R.id.update_exercise_submit_button).setEnabled(true);
            findViewById(R.id.delete_exercise_submit_button).setEnabled(true);
        }
    }

    public void createTimeClicked(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, LocalTime.now().getHourOfDay(), LocalTime.now().getMinuteOfHour(), false);
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String hourOfDayString = hourOfDay < 10 ? "0" + hourOfDay : Integer.toString(hourOfDay);
        String minuteString = minute < 10 ? "0" + minute : Integer.toString(minute);

        timeResult = hourOfDayString + ":" + minuteString + ":00";

        int hourOfDayTwelve = hourOfDay == 0 ? 12 : hourOfDay > 12 ? hourOfDay - 12 : hourOfDay;
        String hourOfDayTwelveString = hourOfDayTwelve < 10 ? "0" + hourOfDayTwelve : Integer.toString(hourOfDayTwelve);
        String timeTwelveString = hourOfDayTwelveString + ":" + minuteString + " " + (hourOfDay >= 12 ? "PM" : "AM");

        selectedTimeLabel.setText(timeTwelveString);

        if (typeOfAdd.equals("update") && (!dateResult.equals(prevValues.get("date")) || !timeResult.equals(prevValues.get("time")))) {
            // date and time have been changed from original prefill
            findViewById(R.id.add_exercise_submit_button).setEnabled(true);
            findViewById(R.id.update_exercise_submit_button).setEnabled(false);
            findViewById(R.id.delete_exercise_submit_button).setEnabled(false);
        } else if (typeOfAdd.equals("update") && dateResult.equals(prevValues.get("date")) && timeResult.equals(prevValues.get("time"))) {
            findViewById(R.id.add_exercise_submit_button).setEnabled(false);
            findViewById(R.id.update_exercise_submit_button).setEnabled(true);
            findViewById(R.id.delete_exercise_submit_button).setEnabled(true);
        }
    }

    public void addWorkout(View view) {
        Map<String, Object> map = new LinkedHashMap<>();
        QueryExecutable qe;

        if (validityCheck(view)) {
            if (typeOfAdd.equals("update")) {
                // we wanted to update but add button was pressed aka now we want to add a duplicate entry
                if (workoutDesc.getText().toString().equals(prevValues.get("description"))) {
                    if (workoutCaloriesBurnt.getText().toString().equals(prevValues.get("caloriesBurnt"))) {
                        if (workoutLength.getText().toString().equals(prevValues.get("length"))) {
                            if (cardioSwitch.isChecked() && prevValues.get("isCardio").equals("true")) {
                                if (cardioDistance.getText().toString().equals(prevValues.get("cardioValue"))) {
                                    if (cardioAvgSpeed.getText().toString().equals(prevValues.get("cardioAvgSpeed"))) {
                                        // okay - every value is the same, use same ID
                                        addDuplicate();
                                        return;
                                    }
                                }
                            } else if (sportSwitch.isChecked() && prevValues.get("isSport").equals("true")) {
                                if (sportIntensity.getText().toString().equals(prevValues.get("sportIntensity"))) {
                                    if (sportType.getText().toString().equals(prevValues.get("sportType"))) {
                                        // okay - every value is the same, use same ID
                                        addDuplicate();
                                        return;
                                    }
                                }
                            } else if (!cardioSwitch.isChecked() && !sportSwitch.isChecked() && prevValues.get("isCardio").equals("false") && prevValues.get("isSport").equals("false")) {
                                // okay - every value is the same, use same ID
                                addDuplicate();
                                return;
                            }
                        }
                    }
                }

                // not okay - every value is not the same, use new ID aka just continue with normal code below
            }

            workoutId = findMaxWorkout();

            map.clear();
            map.put("query_type", "special_change");
            map.put("extra", "INSERT INTO workout VALUES (" + workoutId + ", '"
                    + workoutDesc.getText().toString() + "', " + workoutCaloriesBurnt.getText().toString() + ", "
                    + workoutLength.getText().toString() + ")");
            qe = new QueryExecutable(map);
            qe.run();

            if (cardioSwitch.isChecked()) {
                map.clear();
                map.put("query_type", "special_change");
                map.put("extra", "INSERT INTO cardio VALUES (" + workoutId + ", "
                        + cardioDistance.getText().toString() + ", " + cardioAvgSpeed.getText().toString() + ")");
                qe = new QueryExecutable(map);
                qe.run();
            } else if (sportSwitch.isChecked()) {
                map.clear();
                map.put("query_type", "special_change");
                map.put("extra", "INSERT INTO sport VALUES (" + workoutId + ", "
                        + sportIntensity.getText().toString() + ", '" + sportType.getText().toString() + "')");
                qe = new QueryExecutable(map);
                qe.run();
            }

            if (!typeOfAdd.equals("plan")) {
                map.clear();
                map.put("query_type", "special_change");
                map.put("extra", "INSERT INTO ExerciseLogEntry VALUES (" + workoutId
                        + ", TO_TIMESTAMP('" + dateResult + " " + timeResult + "', 'YYYY-MM-DD HH24:MI:SS'))");
                qe = new QueryExecutable(map);
                qe.run();

                map.clear();
                map.put("query_type", "special_change");
                map.put("extra", "INSERT INTO UserExerciseLog VALUES ('" + getIntent().getStringExtra("username")
                        + "', TO_TIMESTAMP('" + dateResult + " " + timeResult + "', 'YYYY-MM-DD HH24:MI:SS')," + workoutId + ")");
                qe = new QueryExecutable(map);
                qe.run();
            }

            if (typeOfAdd.equals("plan")) {
                addToPlan();
            } else {
                Toast.makeText(this, "Successfully added new exercise log entry", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private int findMaxWorkout() {
        Map<String, Object> map = new HashMap<>();
        QueryExecutable qe;
        map.put("query_type", "special");
        map.put("extra", "SELECT MAX(workoutid) FROM workout");
        qe = new QueryExecutable(map);
        JSONArray ans = qe.run();
        try {
            return Integer.parseInt(ans.getJSONObject(0).getString("MAX(WORKOUTID)")) + 1;
        } catch (JSONException e) {
            return new Random().nextInt();
        }
    }

    private void prefillWithExtras() {
        String username = getIntent().getStringExtra("username");
        workoutId = Integer.parseInt(getIntent().getStringExtra("workoutId"));
        String timestamp = getIntent().getStringExtra("timestampString");
        String exerciseType = "";

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("query_type", "special");
        map.put("extra", "SELECT w.workoutid, ele.logtime, w.description, w.caloriesburnt, w.timeworkout, c.distance, c.avgspeed FROM workout w, exerciselogentry ele, userexerciselog uel, cardio c WHERE w.workoutid = ele.workoutid AND w.workoutid = uel.workoutid AND ele.logtime = uel.logtime AND w.workoutid = c.workoutid AND uel.username = '" + username + "' AND w.workoutid = '" + workoutId + "' AND ele.logtime = TO_TIMESTAMP('" + timestamp + "', 'YYYY-MM-DD HH24:MI:SS')");
        QueryExecutable qe = new QueryExecutable(map);
        JSONArray cardioAns = qe.run();

        map.clear();
        map.put("query_type", "special");
        map.put("extra", "SELECT w.workoutid, ele.logtime, w.description, w.caloriesburnt, w.timeworkout, s.intensity, s.sporttype FROM workout w, exerciselogentry ele, userexerciselog uel, sport s WHERE w.workoutid = ele.workoutid AND w.workoutid = uel.workoutid AND ele.logtime = uel.logtime AND w.workoutid = s.workoutid AND uel.username = '" + username + "' AND w.workoutid = '" + workoutId + "' AND ele.logtime = TO_TIMESTAMP('" + timestamp + "', 'YYYY-MM-DD HH24:MI:SS')");
        qe = new QueryExecutable(map);
        JSONArray sportAns = qe.run();

        map.clear();
        map.put("query_type", "special");
        map.put("extra", "SELECT w.workoutid, ele.logtime, w.description, w.caloriesburnt, w.timeworkout FROM workout w, exerciselogentry ele, userexerciselog uel WHERE w.workoutid = ele.workoutid AND w.workoutid = uel.workoutid AND ele.logtime = uel.logtime AND uel.username = '" + username + "' AND w.workoutid = '" + workoutId + "' AND ele.logtime = TO_TIMESTAMP('" + timestamp + "', 'YYYY-MM-DD HH24:MI:SS')");
        qe = new QueryExecutable(map);
        JSONArray regularAns = qe.run();

        map.clear();
        map.put("query_type", "special");
        map.put("extra", "SELECT w.workoutid, w.description, w.caloriesburnt, w.timeworkout, c.distance, c.avgspeed From workout w, Cardio c where w.wokoutId = c.workoutID AND w.workoutId = " + workoutId);
        qe = new QueryExecutable(map);
        JSONArray planCardio = qe.run();

        map.clear();
        map.put("query_type", "special");
        map.put("extra", "SELECT w.workoutid, w.description, w.caloriesburnt, w.timeworkout, s.intensity, s.sporttype From workout w, Sport s where w.wokoutId = s.workoutID AND w.workoutId = " + workoutId);
        qe = new QueryExecutable(map);
        JSONArray planSport = qe.run();

        map.clear();
        map.put("query_type", "special");
        map.put("extra", "SELECT w.workoutid, w.description, w.caloriesburnt, w.timeworkout From workout w where w.workoutId = " + workoutId);
        qe = new QueryExecutable(map);
        JSONArray planRegular = qe.run();

        JSONArray finalAns;

        if (cardioAns.length() == 0 && sportAns.length() == 0 && regularAns.length() > 0) {
            exerciseType = "";
            finalAns = regularAns;
        } else if (cardioAns.length() != 0) {
            exerciseType = "cardio";
            finalAns = cardioAns;
        } else if (sportAns.length() != 0) {
            exerciseType = "sport";
            finalAns = sportAns;
        } else if (planCardio.length() == 0 && sportAns.length() == 0) {
            exerciseType = "";
            finalAns = planRegular;
        } else if (planCardio.length() != 0) {
            exerciseType = "cardio";
            finalAns = planCardio;
        } else if (sportAns.length() != 0) {
            exerciseType = "sport";
            finalAns = planSport;
        } else {
            // should never happen, but just in case our database is messed up
            exerciseType = "";
            finalAns = regularAns;
        }

        try {
            JSONObject o = finalAns.getJSONObject(0);
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

            if (regularAns.length() > 0) {
                LocalDateTime ldt = TimestampUtility.parseDatabaseTimestamp(o.getString("LOGTIME"));
                dateResult = ldt.toString("yyyy-MM-dd", Locale.ENGLISH);
                selectedDateLabel.setText(dateResult);
                timeResult = ldt.toString("HH:mm:00", Locale.ENGLISH);
                selectedTimeLabel.setText(ldt.toString("hh:mm aa", Locale.ENGLISH));
            }

            prevValues.put("description", o.getString("DESCRIPTION"));
            prevValues.put("caloriesBurnt", o.getString("CALORIESBURNT"));
            prevValues.put("length", o.getString("TIMEWORKOUT"));
            prevValues.put("date", dateResult);
            prevValues.put("time", timeResult);
            prevValues.put("isCardio", cardioSwitch.isChecked() ? "true" : "false");
            prevValues.put("cardioDistance", cardioSwitch.isChecked() ? o.getString("DISTANCE") : "");
            prevValues.put("cardioAvgSpeed", cardioSwitch.isChecked() ? o.getString("AVGSPEED") : "");
            prevValues.put("isSport", sportSwitch.isChecked() ? "true" : "false");
            prevValues.put("sportIntensity", sportSwitch.isChecked() ? o.getString("INTENSITY") : "");
            prevValues.put("sportType", sportSwitch.isChecked() ? o.getString("SPORTTYPE") : "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void deleteExercise(View view) {
        Map<String, Object> map = new LinkedHashMap<>();
        QueryExecutable qe;

        map.put("query_type", "special_change");
        map.put("extra", "DELETE FROM cardio WHERE workoutid = " + workoutId);
        qe = new QueryExecutable(map);
        qe.run();

        map.clear();
        map.put("query_type", "special_change");
        map.put("extra", "DELETE FROM sport WHERE workoutid = " + workoutId);
        qe = new QueryExecutable(map);
        qe.run();

        map.clear();
        map.put("query_type", "special_change");
        map.put("extra", "DELETE FROM UserExerciseLog WHERE workoutid = " + workoutId
                + " AND logtime = TO_TIMESTAMP('" + dateResult + " " + timeResult + "', 'YYYY-MM-DD HH24:MI:SS') AND username = '"
                + username + "'");
        qe = new QueryExecutable(map);
        qe.run();

        map.clear();
        map.put("query_type", "special_change");
        map.put("extra", "DELETE FROM ExerciseLogEntry WHERE workoutid = " + workoutId
                + " AND logtime = TO_TIMESTAMP('" + dateResult + " " + timeResult + "', 'YYYY-MM-DD HH24:MI:SS')");
        qe = new QueryExecutable(map);
        qe.run();

        Toast.makeText(this, "Successfully deleted exercise log entry", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void updateWorkout(View view) {
        Map<String, Object> map = new LinkedHashMap<>();
        QueryExecutable qe;

        if (validityCheck(view)) {
            map.put("query_type", "special_change");
            map.put("extra", "DELETE FROM cardio WHERE workoutid = " + workoutId);
            qe = new QueryExecutable(map);
            qe.run();

            map.clear();
            map.put("query_type", "special_change");
            map.put("extra", "DELETE FROM sport WHERE workoutid = " + workoutId);
            qe = new QueryExecutable(map);
            qe.run();

            map.clear();
            map.put("query_type", "special_change");
            map.put("extra", "UPDATE Workout SET description = '" + workoutDesc.getText().toString() + "', caloriesburnt = " + workoutCaloriesBurnt.getText().toString() + ", timeworkout = " + workoutLength.getText().toString() + " WHERE workoutid = " + workoutId);
            qe = new QueryExecutable(map);
            qe.run();

            if (cardioSwitch.isChecked()) {
                map.clear();
                map.put("query_type", "special_change");
                map.put("extra", "INSERT INTO cardio VALUES (" + workoutId + ", "
                        + cardioDistance.getText().toString() + ", " + cardioAvgSpeed.getText().toString() + ")");
                qe = new QueryExecutable(map);
                qe.run();
            } else if (sportSwitch.isChecked()) {
                map.clear();
                map.put("query_type", "special_change");
                map.put("extra", "INSERT INTO sport VALUES (" + workoutId + ", "
                        + sportIntensity.getText().toString() + ", '" + sportType.getText().toString() + "')");
                qe = new QueryExecutable(map);
                qe.run();
            }

            Toast.makeText(this, "Successfully updated exercise log entry", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private boolean validityCheck(View view) {
        if (workoutDesc.getText().toString().equals("")) {
            Snackbar.make(view, "Invalid Workout Description", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return false;
        } else if (workoutCaloriesBurnt.getText().toString().equals("")) {
            Snackbar.make(view, "Invalid Number of Calories Burnt", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return false;
        } else if (workoutLength.getText().toString().equals("")) {
            Snackbar.make(view, "Invalid Workout Length", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return false;
        } else if (dateResult.equals("") && !typeOfAdd.equals("plan")) {
            Snackbar.make(view, "Invalid Date", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return false;
        } else if (timeResult.equals("") && !typeOfAdd.equals("plan")) {
            Snackbar.make(view, "Invalid Time", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return false;
        } else if (cardioSwitch.isChecked() && (cardioDistance.getText().toString().equals("") || cardioAvgSpeed.getText().toString().equals(""))) {
            Snackbar.make(view, "Invalid Cardio Input", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return false;
        } else if (sportSwitch.isChecked() && (sportIntensity.getText().toString().equals("") || sportType.getText().toString().equals(""))) {
            Snackbar.make(view, "Invalid Sport Input", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return false;
        }

        return true;
    }

    private void addDuplicate() {
        Map<String, Object> map = new LinkedHashMap<>();
        QueryExecutable qe;

        map.clear();
        map.put("query_type", "special_change");
        map.put("extra", "INSERT INTO ExerciseLogEntry VALUES (" + workoutId
                + ", TO_TIMESTAMP('" + dateResult + " " + timeResult + "', 'YYYY-MM-DD HH24:MI:SS'))");
        qe = new QueryExecutable(map);
        qe.run();

        map.clear();
        map.put("query_type", "special_change");
        map.put("extra", "INSERT INTO UserExerciseLog VALUES ('" + getIntent().getStringExtra("username")
                + "', TO_TIMESTAMP('" + dateResult + " " + timeResult + "', 'YYYY-MM-DD HH24:MI:SS')," + workoutId + ")");
        qe = new QueryExecutable(map);
        qe.run();

        Toast.makeText(this, "Successfully added new exercise log entry", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void addToPlan() {
        Map<String, Object> map = new HashMap<>();
        map.clear();

        map.put("query_type", "special_change");
        map.put("extra", "INSERT INTO WorkoutPlanContainsWorkout VALUES(" + getIntent().getIntExtra("planID", -1) + ", " + workoutId + ")");
        QueryExecutable qe = new QueryExecutable(map);
        qe.run();

        Toast.makeText(this, "Successfully added to plan", Toast.LENGTH_SHORT).show();
        finish();
    }
}