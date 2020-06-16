package com.example.lyfestyletracker;

import com.example.lyfestyletracker.web.QueryExecutable;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.LinkedHashMap;
import java.util.Map;

public class MaxPlan {

    public int getMaxPlan(){
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("query_type", "special");
        map.put("extra", "select max(planId) as maxPlan From Plan");
        QueryExecutable qe = new QueryExecutable(map);
        JSONArray ans = qe.run();

        if (ans.length() > 0){
            try{
                return Integer.parseInt(ans.getJSONObject(0).getString("MAXPLAN")) + 1;
            }catch (JSONException e){
                e.printStackTrace();
            }

        }

        return 0;
    }
}
