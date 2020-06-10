package com.example.lyfestyletracker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class QueryExecutable {
    private static ExecutorService exec = Executors.newCachedThreadPool();

    private ASyncQuery as;
    public QueryExecutable(String url){
        as = new ASyncQuery(url);
    }

    public JSONArray run(){
        Future<JSONArray> future = exec.submit(as);

        JSONArray ret = null;
        try{
            ret = future.get();
        }catch (ExecutionException | InterruptedException e){

        }

        return ret;
    }

    public void stop(){
        exec.shutdown();
    }
}
