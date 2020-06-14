package com.example.lyfestyletracker;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TimePicker;

import com.example.lyfestyletracker.web.QueryExecutable;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

public class AddWorkout extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private ArrayList<EditText> cardioTexts;
    private ArrayList<EditText> sportTexts;
    private Switch cardioSwitch;
    private Switch sportSwitch;
    private String dateResult;
    private String timeResult;
    private EditText workoutId;
    private EditText workoutDesc;
    private EditText workoutCaloriesBurnt;
    private EditText workoutLength;
    private String typeOfAdd;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);

        cardioTexts = new ArrayList<>();
        sportTexts = new ArrayList<>();

        typeOfAdd = getIntent().getStringExtra("type");

        cardioSwitch = (Switch) findViewById(R.id.cardio_switch);
        sportSwitch = (Switch) findViewById(R.id.sport_switch);
        workoutId = (EditText) findViewById(R.id.enter_workout_id);
        workoutDesc = (EditText) findViewById(R.id.enter_description);
        workoutCaloriesBurnt = (EditText) findViewById(R.id.enter_calories_burnt);
        workoutLength = (EditText) findViewById(R.id.enter_workout_length);
        dateResult = "";
        timeResult = "";

        final LinearLayout cardioLayout = (LinearLayout) findViewById(R.id.cardio_layout);
        final LinearLayout sportLayout = (LinearLayout) findViewById(R.id.sport_layout);

        cardioSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            //creates and destroys relevant text boxes when checked and unchecked
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()){
                    String[] str = {"Enter WorkoutDistance", "Enter avg Speed"};
                    for (int i = 0; i < 2; i++){
                        EditText et = new EditText(AddWorkout.this);

                        et.setId(i + 1);
                        et.setTextSize(14);
                        et.setHint(str[i]);
                        et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                        cardioLayout.addView(et);
                        cardioTexts.add(et);
                    }

                }else{
                    for (EditText s: cardioTexts){
                        cardioLayout.removeView(s);
                    }
                    cardioTexts.clear();
                }
            }
        });

        sportSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()){
                    String[] str = {"Enter Sport Intensity", "Enter Sport Type"};
                    int[] ints = {InputType.TYPE_CLASS_NUMBER, InputType.TYPE_CLASS_TEXT};
                    for (int i = 0; i < 2; i++){
                        EditText et = new EditText(AddWorkout.this);
                        et.setId(i + 1);
                        et.setTextSize(14);
                        et.setHint(str[i]);
                        et.setInputType(ints[i]);
                        sportLayout.addView(et);
                        sportTexts.add(et);
                    }

                }else{
                    for (EditText s: sportTexts){
                        sportLayout.removeView(s);
                    }
                    sportTexts.clear();
                }
            }
        });


        if (typeOfAdd == "insertSame"){
            workoutId.setText(getIntent().getIntExtra("workoutId",0));
            workoutDesc.setText(getIntent().getStringExtra("workoutDescription"));
            workoutCaloriesBurnt.setText(getIntent().getIntExtra("workoutCalories",0));
            workoutLength.setText(getIntent().getIntExtra("workoutLength",0));

            if (getIntent().getBooleanExtra("cardio", false)){
                cardioSwitch.setChecked(true);
                cardioTexts.get(0).setText(((Double) getIntent().getDoubleExtra("distance", 0.0)).toString());
                cardioTexts.get(1).setText(((Double) getIntent().getDoubleExtra("avgSpeed", 0.0)).toString());
            }

            if (getIntent().getBooleanExtra("sport", false)){
                sportSwitch.setChecked(true);
                sportTexts.get(0).setText(((Integer)getIntent().getIntExtra("intensity",0)).toString());
                sportTexts.get(1).setText(getIntent().getStringExtra("sportType"));
            }
        }



    }

    public void createDateClicked(View view){
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
        String monthS = Integer.toString(month + 1);
        String dayOfMonthS = Integer.toString(dayOfMonth);

        if (month < 10){
            monthS = "0" + monthS;
        }
        if (dayOfMonth < 10){
            dayOfMonthS = "0" + dayOfMonthS;
        }

        String date = year + "-" + monthS + "-" + dayOfMonthS;
        dateResult = date;
        System.out.println(date + "=====================");
    }

    public void createTimeClicked(View view){

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                this, 0,0,true);

        timePickerDialog.show();
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String ShourOfDay = Integer.toString(hourOfDay);
        String Sminute = Integer.toString(minute);

        if (hourOfDay < 10){
            ShourOfDay = "0" + ShourOfDay;
        }

        if (minute < 10){
            ShourOfDay = "0" + Sminute;
        }

        timeResult = ShourOfDay+":"+Sminute+":"+"00";
        System.out.println(timeResult + "=====================");
    }

    public void addWorkout(View view){
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("query_type", "special_change");

        if (workoutId.getText().toString().equals("")){
            Snackbar.make(view, "Invalid WorkoutId", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }else if (workoutDesc.getText().toString().equals("")){
            Snackbar.make(view, "Invalid Workout Description", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }else if (workoutCaloriesBurnt.getText().toString().equals("")){
            Snackbar.make(view, "Invalid number of Calories Burnt", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }else if (workoutLength.getText().toString().equals("")){
            Snackbar.make(view, "Invalid Workout Length", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }else if (dateResult.equals("") && typeOfAdd != "plan"){
            Snackbar.make(view, "Invalid Date", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }else if (timeResult.equals("") && typeOfAdd != "plan"){
            Snackbar.make(view, "Invalid Time", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }else{

            if (cardioSwitch.isChecked()){
                for (EditText e: cardioTexts){
                    if (e.getText().toString().equals("")){
                        Snackbar.make(view, "Invalid Cardio Input", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        return;
                    }
                }

            }
            if (sportSwitch.isChecked()){
                for (EditText e: sportTexts){
                    if (e.getText().toString().equals("")){
                        Snackbar.make(view, "Invalid Sport Input", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        return;
                    }
                }
            }

            map.put("extra", "Insert Into Workout Values (" + workoutId.getText().toString() + ", '"
                    + workoutDesc.getText().toString() + "', " + workoutCaloriesBurnt.getText().toString() + ", "
                    + workoutLength.getText().toString() + ")");

            QueryExecutable qe = new QueryExecutable(map);
            JSONArray res = qe.run();


            if (cardioSwitch.isChecked()){
                map.clear();
                map.put("query_type", "special_change");
                map.put("extra", "Insert Into Cardio Values(" + workoutId.getText().toString() + ", "
                        + cardioTexts.get(0).getText().toString() + ", " + cardioTexts.get(1).getText().toString() + ")");
                qe = new QueryExecutable(map);
                res = qe.run();

            }

            if (sportSwitch.isChecked()){
                map.clear();
                map.put("query_type", "special_change");
                map.put("extra", "Insert Into SPORT Values(" + workoutId.getText().toString() + ", "
                        + sportTexts.get(0).getText().toString() + ", '" + sportTexts.get(1).getText().toString() + "')");
                qe = new QueryExecutable(map);
                res = qe.run();
            }

            if (typeOfAdd != "plan"){
                map.clear();
                map.put("query_type", "special_change");
                map.put("extra", "Insert into ExerciseLogEntry Values(" + workoutId.getText().toString()
                        + ", TO_TIMESTAMP('" + dateResult + " " + timeResult + "', 'YYYY-MM-DD HH24:MI:SS'))");


                qe = new QueryExecutable(map);
                res = qe.run();

                map.clear();
                map.put("query_type", "special_change");
                map.put("extra", "Insert into UserExerciseLog Values('" + getIntent().getStringExtra("username")
                        + "', TO_TIMESTAMP('" + dateResult + " " + timeResult + "', 'YYYY-MM-DD HH24:MI:SS')," + workoutId.getText().toString()+")");


                qe = new QueryExecutable(map);
                res = qe.run();
            }else {
               addToPlan();
            }


            Snackbar.make(view, "Successfully added the workout", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        }

    }


    private void addToPlan(){
        //ADD THIS
    }



}