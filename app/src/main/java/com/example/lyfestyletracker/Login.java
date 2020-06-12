package com.example.lyfestyletracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import com.example.lyfestyletracker.web.QueryExecutable;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.LinkedHashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void authenticate(View view) {

        EditText eEdit = (EditText) findViewById(R.id.email_box);
        EditText pEdit = (EditText) findViewById(R.id.password_box);
        Switch consultant = (Switch) findViewById(R.id.consultant_switch);
        JSONArray res;
        Map<String,Object> map = new LinkedHashMap<>();
        Intent intent = null;

        if (consultant.isChecked()){
            //If you are a consultant
            map.put("query_type", "authenticate_c");
            map.put("username", eEdit.getText());
            map.put("password", pEdit.getText());


            QueryExecutable qe = new QueryExecutable(map);
            res = qe.run();

            try {
                if (res.getJSONObject(0).getString("result").equals("sucess")){
                    intent = new Intent(this, ConsultantDashboard.class);

                }
            }catch (JSONException e){
                e.printStackTrace();
            }


        }else{

            //If you are a user
            map.put("query_type", "authenticate_u");
            map.put("username", eEdit.getText());
            map.put("password", pEdit.getText());

            QueryExecutable qe = new QueryExecutable(map);
            res = qe.run();

            try {
                if (res.getJSONObject(0).getString("result").equals("sucess")){
                    intent = new Intent(this, UserDashboard.class);

                }
            }catch (JSONException e){
                e.printStackTrace();
            }

        }


        if (intent != null){
            intent.putExtra("username", eEdit.getText().toString());
            startActivity(intent);
            finish();
        }
    }
}