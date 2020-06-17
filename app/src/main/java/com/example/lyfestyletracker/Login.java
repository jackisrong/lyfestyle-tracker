package com.example.lyfestyletracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lyfestyletracker.ui.login.CreateUserConsultant;
import com.example.lyfestyletracker.ui.login.CreateUserUser;
import com.example.lyfestyletracker.web.QueryExecutable;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.LinkedHashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private Button createAcc;
    private EditText eEdit;
    private EditText pEdit;
    private Switch consultant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        createAcc = findViewById(R.id.createAcc_button);
        eEdit = (EditText) findViewById(R.id.email_box);
        pEdit = (EditText) findViewById(R.id.password_box);
        consultant = (Switch) findViewById(R.id.consultant_switch);
        SharedPreferences sp = getSharedPreferences(getString(R.string.user_login_data_file_key), Context.MODE_PRIVATE);

        // check if already logged in
        if (sp.getBoolean("loggedIn", false)) {
            eEdit.setText(sp.getString("username", ""));
            pEdit.setText(sp.getString("password", ""));
            if (sp.getString("type", "").equals("user")) {
                consultant.setChecked(false);
            } else if (sp.getString("type", "").equals("consultant")) {
                consultant.setChecked(true);
            }
            authenticate(null);
        }
    }

    public void initializeSignup(View view) {
        consultant = (Switch) findViewById(R.id.consultant_switch);
        Intent intent;
        if (consultant.isChecked()) {
            intent = new Intent(Login.this, CreateUserConsultant.class);
        } else {
            intent = new Intent(Login.this, CreateUserUser.class);
        }
        startActivity(intent);
    }

    public void authenticate(View view) {
        EditText eEdit = (EditText) findViewById(R.id.email_box);
        EditText pEdit = (EditText) findViewById(R.id.password_box);
        consultant = (Switch) findViewById(R.id.consultant_switch);
        JSONArray res;
        Map<String, Object> map = new LinkedHashMap<>();
        Intent intent = null;

        if (consultant.isChecked()) {
            //If you are a consultant
            map.put("query_type", "authenticate_c");
            map.put("username", eEdit.getText());
            map.put("password", pEdit.getText());

            QueryExecutable qe = new QueryExecutable(map);
            res = qe.run();

            try {
                if (res.getJSONObject(0).getString("result").equals("sucess")) {
                    intent = new Intent(this, ConsultantDashboard.class);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            //If you are a user
            //queries are put into a map, you can use special as query_type and put whatever in
            map.put("query_type", "authenticate_u");
            map.put("username", eEdit.getText());
            map.put("password", pEdit.getText());

            QueryExecutable qe = new QueryExecutable(map);
            res = qe.run();

            try {
                if (res.getJSONObject(0).getString("result").equals("sucess")) {
                    intent = new Intent(this, UserDashboard.class);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (intent != null) {
            SharedPreferences sp = getSharedPreferences(getString(R.string.user_login_data_file_key), Context.MODE_PRIVATE);
            SharedPreferences.Editor spe = sp.edit();
            spe.putBoolean("loggedIn", true);
            if (consultant.isChecked()) {
                spe.putString("type", "consultant");
            } else {
                spe.putString("type", "user");
            }
            spe.putString("username", eEdit.getText().toString());
            spe.putString("password", pEdit.getText().toString());
            spe.commit();

            intent.putExtra("username", eEdit.getText().toString());
            startActivity(intent);
            finish();
        } else {
            if (view != null) {
                //makes a bar saying invalid comb
                Snackbar.make(view, "Invalid username and/or password", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        }
    }
}