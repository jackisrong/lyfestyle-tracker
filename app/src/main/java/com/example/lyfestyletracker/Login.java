package com.example.lyfestyletracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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

        Map<String,Object> map = new LinkedHashMap<>();

        map.put("query_type", "authenticate");
        map.put("username", eEdit.getText());
        map.put("password", pEdit.getText());


        QueryExecutable qe = new QueryExecutable(map);
        JSONArray res = qe.run();

        try {
            if (res.getJSONObject(0).getString("result").equals("sucess")){
                Intent intent = new Intent(this, UserDashboard.class);
                intent.putExtra("username", eEdit.getText());
                startActivity(intent);
                finish();
            }
        }catch (JSONException e){
            e.printStackTrace();
        }




    }
}