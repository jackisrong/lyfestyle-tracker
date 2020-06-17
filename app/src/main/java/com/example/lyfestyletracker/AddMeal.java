package com.example.lyfestyletracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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

public class AddMeal extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private String username;
    private String dateResult;
    private String timeResult;
    private int mealId;
    private EditText mealDesc;
    private EditText mealType;
    private EditText mealServingSize;
    private EditText mealCarb;
    private EditText mealProtein;
    private EditText mealFat;
    private String typeOfAdd;
    private EditText mealServingNum;
    private TextView selectedDateLabel;
    private TextView selectedTimeLabel;
    private HashMap<String, String> prevValues = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);

        username = getIntent().getStringExtra("username");
        typeOfAdd = getIntent().getStringExtra("type") != null ? getIntent().getStringExtra("type") : "new";

        dateResult = "";
        timeResult = "";
        mealDesc = (EditText) findViewById(R.id.enter_description);
        mealType = (EditText) findViewById(R.id.enter_meal_type);
        mealServingSize = (EditText) findViewById(R.id.enter_meal_serving_size);
        mealCarb = (EditText) findViewById(R.id.enter_meal_carbohydrates);
        mealProtein = (EditText) findViewById(R.id.enter_meal_protein);
        mealFat = (EditText) findViewById(R.id.enter_meal_fat);
        mealServingNum = (EditText) findViewById(R.id.enter_meal_serving_num);
        selectedDateLabel = findViewById(R.id.add_meal_selected_date);
        selectedTimeLabel = findViewById(R.id.add_meal_selected_time);

        findViewById(R.id.delete_meal_button).setVisibility(View.GONE);
        findViewById(R.id.update_meal_button).setVisibility(View.GONE);

        if (typeOfAdd.equals("update")) {
            findViewById(R.id.delete_meal_button).setVisibility(View.VISIBLE);
            findViewById(R.id.update_meal_button).setVisibility(View.VISIBLE);
            findViewById(R.id.add_meal_button).setEnabled(false);
            prefillWithExtras();
        }

        if (typeOfAdd.equals("update")) {
            findViewById(R.id.delete_meal_button).setVisibility(View.VISIBLE);
            findViewById(R.id.update_meal_button).setVisibility(View.VISIBLE);
            findViewById(R.id.add_date_button_meal).setEnabled(false);
            findViewById(R.id.add_time_button_meal).setEnabled(false);
            findViewById(R.id.add_meal_button).setEnabled(false);
            prefillWithExtras();
        }

        if (getIntent().getBooleanExtra("consultant", false)){
            findViewById(R.id.add_meal_button).setEnabled(false);
            findViewById(R.id.delete_meal_button).setVisibility(View.GONE);

        }

        if (getIntent().getBooleanExtra("fromConsultant", false)){
            findViewById(R.id.add_meal_button).setEnabled(true);
            findViewById(R.id.add_date_button_meal).setEnabled(true);
            findViewById(R.id.add_time_button_meal).setEnabled(true);
            findViewById(R.id.update_meal_button).setEnabled(false);
            findViewById(R.id.delete_meal_button).setVisibility(View.GONE);
        }

        if (typeOfAdd.equals("plan")){
            findViewById(R.id.delete_meal_button).setVisibility(View.GONE);
            findViewById(R.id.update_meal_button).setVisibility(View.GONE);
            findViewById(R.id.add_meal_button).setVisibility(View.GONE);
            findViewById(R.id.add_to_diet).setVisibility(View.VISIBLE);
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
            findViewById(R.id.add_meal_button).setEnabled(true);
            findViewById(R.id.update_meal_button).setEnabled(false);
            findViewById(R.id.delete_meal_button).setEnabled(false);
        } else if (typeOfAdd.equals("update") && dateResult.equals(prevValues.get("date")) && timeResult.equals(prevValues.get("time"))) {
            findViewById(R.id.add_meal_button).setEnabled(false);
            findViewById(R.id.update_meal_button).setEnabled(true);
            findViewById(R.id.delete_meal_button).setEnabled(true);
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
            findViewById(R.id.add_meal_button).setEnabled(true);
            findViewById(R.id.update_meal_button).setEnabled(false);
            findViewById(R.id.delete_meal_button).setEnabled(false);
        } else if (typeOfAdd.equals("update") && dateResult.equals(prevValues.get("date")) && timeResult.equals(prevValues.get("time"))) {
            findViewById(R.id.add_meal_button).setEnabled(false);
            findViewById(R.id.update_meal_button).setEnabled(true);
            findViewById(R.id.delete_meal_button).setEnabled(true);
        }
    }


    public void addMeal(View view) {
        Map<String, Object> map = new LinkedHashMap<>();
        QueryExecutable qe = new QueryExecutable(map);

        if (validityCheck(view)) {
            if (typeOfAdd.equals("update")) {
                // we wanted to update but add button was pressed aka now we want to add a duplicate entry
                if (mealType.getText().toString().equals(prevValues.get("type"))) {
                    if (mealDesc.getText().toString().equals(prevValues.get("description"))) {
                        if (mealServingSize.getText().toString().equals(prevValues.get("servingSize"))) {
                            if (mealCarb.getText().toString().equals(prevValues.get("carbohydrates"))) {
                                if (mealFat.getText().toString().equals(prevValues.get("fat"))) {
                                    if (mealProtein.getText().toString().equals(prevValues.get("protein"))) {
                                        if (mealServingNum.getText().toString().equals(prevValues.get("numberOfServings"))) {
                                            // okay - every value is the same, use same ID
                                            addDuplicate();
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            map.clear();
            map.put("query_type", "special");
            map.put("extra", "SELECT MAX(mealid) FROM meal");
            qe = new QueryExecutable(map);
            JSONArray ans = qe.run();
            try {
                mealId = Integer.parseInt(ans.getJSONObject(0).getString("MAX(MEALID)")) + 1;
                System.out.println(mealId);
            } catch (JSONException e) {
                mealId = new Random().nextInt();
                e.printStackTrace();
            }

            int protein = Integer.parseInt(mealProtein.getText().toString());
            int carbs = Integer.parseInt(mealCarb.getText().toString());
            int fat = Integer.parseInt(mealFat.getText().toString());
            int calories = calculateCalories(protein, fat, carbs);

            map.clear();
            map.put("query_type", "special_change");
            map.put("extra", "INSERT INTO MealCalories Values(" + carbs + ", " + fat + ", " + protein + ", " + calories + ")");
            qe = new QueryExecutable(map);
            qe.run();

            map.clear();
            map.put("query_type", "special_change");
            map.put("extra", "INSERT INTO Meal Values(" + mealId + ", '" + mealType.getText().toString() + "', '" +
                    mealDesc.getText().toString() + "', " + mealServingSize.getText().toString() + ", " + carbs + ", " + fat + ", " + protein + ")");
            qe = new QueryExecutable(map);
            qe.run();

            map.clear();
            map.put("query_type", "special_change");
            map.put("extra", "INSERT INTO MealLogEntry Values(" + mealId +
                    ", TO_TIMESTAMP('" + dateResult + " " + timeResult + "', 'YYYY-MM-DD HH24:MI:SS'), " + mealServingNum.getText().toString() + ")");
            qe = new QueryExecutable(map);
            qe.run();

            if (!typeOfAdd.equals("plan")) {
                map.clear();
                map.put("query_type", "special_change");
                map.put("extra", "INSERT INTO UserMealLog Values('" + username +
                        "', TO_TIMESTAMP('" + dateResult + " " + timeResult + "', 'YYYY-MM-DD HH24:MI:SS'), " + mealId + ")");
                qe = new QueryExecutable(map);
                qe.run();
            } else {
                addToPlan(Integer.toString(mealId));
            }

            if (!typeOfAdd.equals("plan")) {
                Toast.makeText(this, "Successfully added new meal log entry", Toast.LENGTH_SHORT).show();
                finish();
            }

            // not okay - every value is not the same, use new ID aka just continue with normal code below
        }


    }

    private int calculateCalories(int protein, int fat, int carbs) {
        return 4 * protein + 4 * carbs + 9 * fat;
    }

    private void prefillWithExtras() {
        String username = getIntent().getStringExtra("username");
        mealId = Integer.parseInt(getIntent().getStringExtra("mealId"));
        String timestamp = getIntent().getStringExtra("timestampString");

        Map<String, Object> map = new LinkedHashMap<>();
        QueryExecutable qe = new QueryExecutable(map);
        map.clear();
        map.put("query_type", "special");
        map.put("extra", "SELECT m.mealid, m.type, m.description, m.servingsizegrams, m.carbohydrates, m.fat, m.protein, mle.logtime, mle.numberofservings FROM meal m, meallogentry mle, usermeallog uml WHERE m.mealid = mle.mealid AND m.mealid = uml.mealid AND mle.logtime = uml.logtime AND uml.username = '" + username + "' AND m.mealid = '" + mealId + "' AND mle.logtime = TO_TIMESTAMP('" + timestamp + "', 'YYYY-MM-DD HH24:MI:SS')");
        qe = new QueryExecutable(map);
        JSONArray ans = qe.run();

        System.out.println(ans);

        if (ans.length() == 0){
            map.clear();
            map.put("query_type", "special");
            map.put("extra", "SELECT m.mealid, m.type, m.description, m.servingsizegrams, m.carbohydrates, m.fat, m.protein, mle.logtime, mle.numberofservings FROM meal m, meallogentry mle WHERE m.mealid = mle.mealid  AND m.mealid = " + mealId + " AND mle.logtime = TO_TIMESTAMP('" + timestamp + "', 'YYYY-MM-DD HH24:MI:SS')");
            qe = new QueryExecutable(map);
            ans = qe.run();
        }

        try {
            JSONObject o = ans.getJSONObject(0);
            mealType.setText(o.getString("TYPE"));
            mealDesc.setText(o.getString("DESCRIPTION"));
            mealServingSize.setText(o.getString("SERVINGSIZEGRAMS"));
            mealCarb.setText(o.getString("CARBOHYDRATES"));
            mealFat.setText(o.getString("FAT"));
            mealProtein.setText(o.getString("PROTEIN"));
            mealServingNum.setText(o.getString("NUMBEROFSERVINGS"));

            LocalDateTime ldt = TimestampUtility.parseDatabaseTimestamp(o.getString("LOGTIME"));
            dateResult = ldt.toString("yyyy-MM-dd", Locale.ENGLISH);
            selectedDateLabel.setText(dateResult);
            timeResult = ldt.toString("HH:mm:00", Locale.ENGLISH);
            selectedTimeLabel.setText(ldt.toString("hh:mm aa", Locale.ENGLISH));

            prevValues.put("type", o.getString("TYPE"));
            prevValues.put("description", o.getString("DESCRIPTION"));
            prevValues.put("servingSize", o.getString("SERVINGSIZEGRAMS"));
            prevValues.put("carbohydrates", o.getString("CARBOHYDRATES"));
            prevValues.put("fat", o.getString("FAT"));
            prevValues.put("protein", o.getString("PROTEIN"));
            prevValues.put("numberOfServings", o.getString("NUMBEROFSERVINGS"));
            prevValues.put("date", dateResult);
            prevValues.put("time", timeResult);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void deleteMeal(View view) {
        Map<String, Object> map = new LinkedHashMap<>();
        QueryExecutable qe;

        map.clear();
        map.put("query_type", "special_change");
        map.put("extra", "DELETE FROM UserMealLog WHERE mealid = " + mealId
                + " AND logtime = TO_TIMESTAMP('" + dateResult + " " + timeResult + "', 'YYYY-MM-DD HH24:MI:SS') AND username = '"
                + username + "'");
        qe = new QueryExecutable(map);
        qe.run();

        map.clear();
        map.put("query_type", "special_change");
        map.put("extra", "DELETE FROM MealLogEntry WHERE mealid = " + mealId
                + " AND logtime = TO_TIMESTAMP('" + dateResult + " " + timeResult + "', 'YYYY-MM-DD HH24:MI:SS')");
        qe = new QueryExecutable(map);
        qe.run();

        Toast.makeText(this, "Successfully deleted meal log entry", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void updateMeal(View view) {
        Map<String, Object> map = new LinkedHashMap<>();
        QueryExecutable qe;

        if (validityCheck(view)) {
            int carbs = Integer.parseInt(mealCarb.getText().toString());
            int fat = Integer.parseInt(mealFat.getText().toString());
            int protein = Integer.parseInt(mealProtein.getText().toString());


            map.clear();
            map.put("query_type", "special_change");
            map.put("extra", "INSERT INTO MealCalories Values(" + carbs + ", " + fat + ", " + protein + ", " + calculateCalories(protein, fat, carbs) + ")");
            qe = new QueryExecutable(map);
            qe.run();

            map.clear();
            map.put("query_type", "special_change");
            map.put("extra", "UPDATE Meal SET type = '" + mealType.getText().toString() + "', description = '" + mealDesc.getText().toString() + "', servingSizeGrams = " + mealServingSize.getText().toString() + ", carbohydrates = " + mealCarb.getText().toString() + ", fat = " + mealFat.getText().toString() + ", protein = " + mealProtein.getText().toString() + " WHERE mealid = " + mealId);
            qe = new QueryExecutable(map);
            qe.run();

            map.clear();
            map.put("query_type", "special_change");
            map.put("extra", "UPDATE MealLogEntry SET numberOfServings = " + mealServingNum.getText().toString() + " WHERE mealid = " + mealId + " AND logtime = TO_TIMESTAMP('" + dateResult + " " + timeResult + "', 'YYYY-MM-DD HH24:MI:SS')");
            qe = new QueryExecutable(map);
            qe.run();

            Toast.makeText(this, "Successfully updated exercise log entry", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private boolean validityCheck(View view) {
        if (mealDesc.getText().toString().equals("")) {
            Snackbar.make(view, "Invalid Description", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return false;
        } else if (mealType.getText().toString().equals("")) {
            Snackbar.make(view, "Invalid Meal Type", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return false;
        } else if (mealServingSize.getText().toString().equals("")) {
            Snackbar.make(view, "Invalid Serving size", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return false;
        } else if (mealFat.getText().toString().equals("")) {
            Snackbar.make(view, "Invalid Fat Amount", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return false;
        } else if (mealCarb.getText().toString().equals("")) {
            Snackbar.make(view, "Invalid Carbohydrate Amount", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return false;
        } else if (mealProtein.getText().toString().equals("")) {
            Snackbar.make(view, "Invalid Protein Amount", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return false;
        } else if (dateResult.equals("")) {
            Snackbar.make(view, "Invalid Date", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return false;
        } else if (timeResult.equals("")) {
            Snackbar.make(view, "Invalid Time", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return false;
        }

        return true;
    }

    private void addDuplicate() {
        Map<String, Object> map = new LinkedHashMap<>();
        QueryExecutable qe;

        map.clear();
        map.put("query_type", "special_change");
        map.put("extra", "INSERT INTO MealLogEntry VALUES (" + mealId
                + ", TO_TIMESTAMP('" + dateResult + " " + timeResult + "', 'YYYY-MM-DD HH24:MI:SS'), " + mealServingNum.getText().toString() + ")");
        qe = new QueryExecutable(map);
        qe.run();

        map.clear();
        map.put("query_type", "special_change");
        map.put("extra", "INSERT INTO UserMealLog VALUES ('" + username
                + "', TO_TIMESTAMP('" + dateResult + " " + timeResult + "', 'YYYY-MM-DD HH24:MI:SS')," + mealId + ")");
        qe = new QueryExecutable(map);
        qe.run();

        Toast.makeText(this, "Successfully added new exercise log entry", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void addToPlan(String meal) {
        Map<String, Object> map = new HashMap<>();
        map.clear();

        map.put("query_type", "special_change");
        map.put("extra", "INSERT INTO DietContainsMealLog Values("+ getIntent().getIntExtra("planID", -1) + ", TO_TIMESTAMP('" + dateResult + " " + timeResult + "', 'YYYY-MM-DD HH24:MI:SS'), " + meal + ")");

        System.out.println("INSERT INTO DietContainsMealLog Values("+ getIntent().getIntExtra("planID", -1) + ", TO_TIMESTAMP('" + dateResult + " " + timeResult + "', 'YYYY-MM-DD HH24:MI:SS'), " + meal + ")");
        QueryExecutable qe = new QueryExecutable(map);
        qe.run();

        Toast.makeText(this, "Successfully added to plan", Toast.LENGTH_SHORT).show();
        finish();
    }
}