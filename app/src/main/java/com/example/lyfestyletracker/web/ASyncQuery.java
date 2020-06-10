package com.example.lyfestyletracker.web;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;

public class ASyncQuery implements Callable<JSONArray> {
    Map<String,Object> map;

    public ASyncQuery(Map<String,Object> map){
        this.map = map;
    }

    @Override
    public JSONArray call() throws Exception {
        BufferedReader br =null;
        StringBuilder sb = new StringBuilder();

        String url = "https://www.students.cs.ubc.ca/~luigi28/hello.php";


        try {
            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String,Object> param : map.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }

            byte[] postDataBytes = postData.toString().getBytes(StandardCharsets.UTF_8);

            URL phpURL = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) phpURL.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);
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
