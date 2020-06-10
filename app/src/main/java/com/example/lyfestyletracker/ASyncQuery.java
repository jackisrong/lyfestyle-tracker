package com.example.lyfestyletracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

public class ASyncQuery implements Callable<JSONArray> {
    String url;

    public ASyncQuery(String url){
        this.url = url;
    }

    @Override
    public JSONArray call() throws Exception {
        BufferedReader br =null;
        StringBuilder sb = new StringBuilder();

        try {
            URL phpURL = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) phpURL.openConnection();
            conn.connect();
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        JSONArray obj = null;
        try{
            obj = new JSONArray(sb.toString());
        }catch (JSONException e){
            e.printStackTrace();
        }
        return obj;
    }
}
