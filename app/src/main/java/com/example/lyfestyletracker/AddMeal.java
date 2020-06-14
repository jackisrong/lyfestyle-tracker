package com.example.lyfestyletracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.lyfestyletracker.web.QueryExecutable;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

public class AddMeal extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private String dateResult;
    private String timeResult;
    private EditText mealId;
    private EditText mealDesc;
    private EditText mealType;
    private EditText mealServingSize;
    private EditText mealCarb;
    private EditText mealProtein;
    private EditText mealFat;
    private String typeOfAdd;
    private EditText mealServingNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);

        typeOfAdd = getIntent().getStringExtra("type");

        dateResult = "";
        timeResult = "";
        mealId = (EditText) findViewById(R.id.enter_meal_id);
        mealDesc = (EditText) findViewById(R.id.enter_description);
        mealType = (EditText) findViewById(R.id.enter_meal_type);
        mealServingSize = (EditText) findViewById(R.id.enter_meal_serving_size);
        mealCarb = (EditText) findViewById(R.id.enter_meal_carbohydrates);
        mealProtein = (EditText) findViewById(R.id.enter_meal_protein);
        mealFat = (EditText) findViewById(R.id.enter_meal_fat);
        mealServingNum = (EditText) findViewById(R.id.enter_meal_serving_num);


        if (typeOfAdd == "insertSame"){
            mealId.setText(getIntent().getIntExtra("mealId", 0));
            mealDesc.setText(getIntent().getStringExtra("mealDescription"));
            mealType.setText(getIntent().getStringExtra("mealType"));
            mealServingSize.setText(getIntent().getIntExtra("mealServingSize", 0));
            mealCarb.setText(getIntent().getIntExtra("mealCarbohydrates", 0));
            mealProtein.setText(getIntent().getIntExtra("mealProtein", 0));
            mealFat.setText(getIntent().getIntExtra("mealFat", 0));
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

        dateResult = year + "-" + monthS + "-" + dayOfMonthS;
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
    }



    public void addMeal(View view){
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("query_type", "special_change");

        if (mealId.getText().toString().equals("")){
            Snackbar.make(view, "Invalid Meal ID", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }else if (mealDesc.getText().toString().equals("")){
            Snackbar.make(view, "Invalid Description", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }else if (mealType.getText().toString().equals("")){
            Snackbar.make(view, "Invalid Meal Type", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }else if (mealServingSize.getText().toString().equals("")){
            Snackbar.make(view, "Invalid Serving size", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }else if (mealFat.getText().toString().equals("")){
            Snackbar.make(view, "Invalid Fat Amount", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }else if (mealCarb.getText().toString().equals("")){
            Snackbar.make(view, "Invalid Carbohydrate Amount", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }else if (mealProtein.getText().toString().equals("")){
            Snackbar.make(view, "Invalid Protein Amount", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }else if (dateResult.equals("")){
            Snackbar.make(view, "Invalid Date", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }else if (timeResult.equals("")){
            Snackbar.make(view, "Invalid Time", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }else {
            int protein = Integer.parseInt(mealProtein.getText().toString());
            int carbs = Integer.parseInt(mealCarb.getText().toString());
            int fat = Integer.parseInt(mealFat.getText().toString());
            int calories = calculateCalories(protein, fat, carbs);


            map.put("extra", "INSERT INTO MealCalories Values(" + carbs + ", " + fat + ", " + protein + ", " + calories + ")");
            QueryExecutable qe = new QueryExecutable(map);
            qe.run();

            map.clear();
            map.put("query_type", "special_change");
            map.put("extra", "INSERT INTO MEAL Values(" + mealId.getText().toString() + ", '" + mealType.getText().toString() + "', '" +
                    mealDesc.getText().toString() + "', " + mealServingSize.getText().toString() + ", " + carbs + ", " + fat + ", " + protein + ")");
            qe = new QueryExecutable(map);
            qe.run();


            map.clear();
            map.put("query_type", "special_change");
            map.put("extra", "INSERT INTO MealLogEntry Values(" + mealId.getText().toString() +
                    ", TO_TIMESTAMP('" + dateResult + " " + timeResult + "', 'YYYY-MM-DD HH24:MI:SS'), " + mealServingNum.getText().toString() + ")");
            qe = new QueryExecutable(map);
            qe.run();

            if( typeOfAdd != "plan") {
                map.clear();
                map.put("query_type", "special_change");
                map.put("extra", "INSERT INTO UserMealLog Values('" + getIntent().getStringExtra("username") +
                        "', TO_TIMESTAMP('" + dateResult + " " + timeResult + "', 'YYYY-MM-DD HH24:MI:SS'), " + mealId.getText().toString() + ")");
                qe = new QueryExecutable(map);
                qe.run();
            }else{
               addToPlan();
            }
            Snackbar.make(view, "Successfully added the meal", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        }
    }

    private int calculateCalories(int protein, int fat, int carbs){
        return 4*protein + 4*carbs + 9*fat;
    }

    private void addToPlan(){
        //DO THIS
    }
}