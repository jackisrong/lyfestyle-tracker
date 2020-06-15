package com.example.lyfestyletracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.lyfestyletracker.web.QueryExecutable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class ConsultantDashboard extends AppCompatActivity {
    private String username;
    private Switch addCompanySwitch;
    private ArrayList<EditText> addCompanyText;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant_dashboard);
        username = getIntent().getStringExtra("username");

        addCompanyText = new ArrayList<>();

        spinner = (Spinner) findViewById(R.id.companies_spinner);
        addCompanySwitch = (Switch) findViewById(R.id.add_company_switch);
        final LinearLayout ll = (LinearLayout) findViewById(R.id.add_company_layout);

        populateSpinner();

        addCompanySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    EditText e1 = new EditText(ConsultantDashboard.this);
                    EditText e2 = new EditText(ConsultantDashboard.this);

                    e1.setHint("Enter Company Name");
                    e2.setHint("Enter Company Location");

                    e1.setFilters(new InputFilter[] { new InputFilter.LengthFilter(200) });
                    e2.setFilters(new InputFilter[] { new InputFilter.LengthFilter(200) });

                    e1.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    e2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

                    addCompanyText.add(e1);
                    addCompanyText.add(e2);
                    ll.addView(e1);
                    ll.addView(e2);

                }else{
                    for (int i = 0; i < addCompanyText.size(); i ++){
                        ll.removeView(addCompanyText.get(i));
                    }
                    addCompanyText.clear();
                }
            }
        });


        Map<String, Object> map = new LinkedHashMap<>();
        map.put("query_type", "special");
        map.put("extra", "SELECT name FROM people WHERE username = '" + username + "'");
        QueryExecutable qe = new QueryExecutable(map);
        String welcomeString = "Welcome!";
        try {
            welcomeString = "Welcome " + qe.run().getJSONObject(0).getString("NAME") + "!";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ((TextView) findViewById(R.id.consultant_greeting)).setText(welcomeString);

        String[] subgreetings = {"Did you eat your fiber today?", "Do you like pie?", "What are your thoughts on golf?",
                "Is pasta better than pizza?", "Did you exercise today?", "Stop drinking so much bubble tea",
                "Bubble tea isn't a personality", "Bubble tea is not a replacement for water", "Damn you looking good!",
                "Guac is extra", "Guac shouldn't be extra, change my mind", "Did you know water is good for you?"};
        Random random = new Random();
        ((TextView) findViewById(R.id.consultant_greeting_extra)).setText(subgreetings[random.nextInt(subgreetings.length)]);
    }

    public void deleteAccount(View view) {
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("query_type", "special_change");
        map.put("extra", "DELETE FROM People WHERE username = '"+ username + "'");
        QueryExecutable qe = new QueryExecutable(map);
        JSONArray res = qe.run();

        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    public void navigate(View view){
        int buttonId = view.getId();
        Intent intent;

        if (buttonId == R.id.nav_button_clients) {
            intent = new Intent(this, ConsultantUserDashboard.class);
        } else if(buttonId == R.id.nav_button_plans){
            intent = new Intent(this, ConsultantPlansDashboard.class);
        } else {
            // this should never happen, but just in case...
            intent = new Intent(this, ConsultantDashboard.class);  ///FIX THISS
        }

        intent.putExtra("username", getIntent().getStringExtra("username"));
        startActivity(intent);
    }

    public void logOut(View view){
        SharedPreferences sp = getSharedPreferences(getString(R.string.user_login_data_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor spe = sp.edit();
        spe.putBoolean("loggedIn", false);
        spe.putString("type", "");
        spe.putString("username", "");
        spe.putString("password", "");
        spe.commit();

        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    private void populateSpinner (){
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        Map<String,Object> map = new LinkedHashMap<>();
        map.put("query_type", "special");
        map.put("extra", "SELECT companyID, name, location From Company WHERE companyID NOT IN (SELECT companyId from ConsultantWorksForCompany where consultantUsername = '" + username + "')" );
        QueryExecutable qe = new QueryExecutable(map);
        JSONArray ans = qe.run();

        try{
            for(int i = 0; i<ans.length(); i ++){
                JSONObject o = ans.getJSONObject(i);
                spinnerAdapter.add(o.getString("COMPANYID") + " - name: " + o.getString("NAME") + " - location :" + o.getString("LOCATION"));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        spinnerAdapter.notifyDataSetChanged();
    }

    public void addCompany(View view){
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("query_type", "special_change");
        QueryExecutable qe;


        int companyID = findMax();
        if (addCompanySwitch.isChecked()){

            map.put("extra", "INSERT INTO COMPANY VALUES(" + companyID + ", '"
                    + addCompanyText.get(0).getText().toString() + "', '" + addCompanyText.get(1).getText().toString() + "')");
            qe = new QueryExecutable(map);
            JSONArray ans = qe.run();

            map.clear();
            map.put("query_type", "special_change");




        }else {


            if(spinner.getAdapter().getCount() == 0){
                return;
            }

            String[] strs = spinner.getSelectedItem().toString().split(" ");

            companyID = Integer.parseInt(strs[0]);
        }

        map.put("extra", "INSERT INTO ConsultantWorksForCompany Values('" + username + "', "  + companyID + ")");
        qe = new QueryExecutable(map);
        JSONArray ans = qe.run();


        populateSpinner();

    }

    private int findMax(){
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("query_type", "special");
        map.put("extra", "Select Max(companyId) FROM Company");
        QueryExecutable qe = new QueryExecutable(map);
        JSONArray ans = qe.run();
        int max = 0;

        if (ans.length() > 0){
            try{
                max = 1 + Integer.parseInt(ans.getJSONObject(0).getString("MAX(COMPANYID)"));

            }catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return  max;
    }
}