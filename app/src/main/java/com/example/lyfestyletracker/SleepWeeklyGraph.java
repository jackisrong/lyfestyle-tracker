package com.example.lyfestyletracker;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lyfestyletracker.web.QueryExecutable;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SleepWeeklyGraph#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SleepWeeklyGraph extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String username;
    private View thisView;

    public SleepWeeklyGraph() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SleepTab1.
     */
    // TODO: Rename and change types and number of parameters
    public static SleepWeeklyGraph newInstance(String param1, String param2, String username) {
        SleepWeeklyGraph fragment = new SleepWeeklyGraph();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString("username", username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            username = getArguments().getString("username");
        }
        Map<String,Object> map = new LinkedHashMap<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        thisView  = inflater.inflate(R.layout.fragment_sleep_tab1, container, false);
        updateWeeklyGraph(thisView);
        return thisView;

    }

    //updates the weekly graph with the queries
    protected void updateWeeklyGraph(View v){
        Map<String,Object> map = new LinkedHashMap<>();

        BarChart barChart = v.findViewById(R.id.BarChart1);
        barChart.clear();


        //
        map.put("query_type", "special");
        map.put("extra", "Select username, sleepTime, sleepDate from UserSleepEntry WHERE username = '"+ username + "' AND sleepDate >= TRUNC(SYSDATE, 'DY') AND sleepDate < TRUNC(SYSDATE, 'DY') + 7 ORDER BY sleepdate");
        //map.put("extra", "UPDATE People SET name = 'Luis E' WHERE username = 'Luis'");

        QueryExecutable qe = new QueryExecutable(map);
        JSONArray res = qe.run();
        System.out.println(res);


        HashMap<String, Integer> sleepTimes = new HashMap<>();


        //gets the week day from the date format
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E");
        ArrayList<BarEntry> barEntries = new ArrayList<>();


        for (int i =0; i < res.length(); i++){
            try {
                ArrayList<Integer> dates = getDate(res.getJSONObject(i).getString("SLEEPDATE"));


                java.util.Date date1 = new Date(dates.get(2) - 1900, dates.get(1) - 1, dates.get(0));
                sleepTimes.put(simpleDateFormat.format(date1),Integer.parseInt(res.getJSONObject(i).getString("SLEEPTIME")));
                System.out.println(simpleDateFormat.format(date1));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        String[] days = {"Mon.","Tue.","Wed.","Thu.","Fri.","Sat.","Sun."};

        for (int i = 0; i < days.length; i ++){
            if (sleepTimes.containsKey(days[i])){
                barEntries.add(new BarEntry(i, sleepTimes.get(days[i])));
            }else {
                barEntries.add(new BarEntry(i, 0));
            }
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Weekly Sleep");
        barDataSet.setColors(ColorTemplate.PASTEL_COLORS);

        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.9f);

        barChart.setData(data);
        barChart.getDescription().setEnabled(false);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new XaxisValueFormatter(days));


    }

    public static class XaxisValueFormatter extends IndexAxisValueFormatter {

        private String[] myValues;
        public  XaxisValueFormatter(String[] values){
            myValues = values;
        }

        @Override
        public String getFormattedValue(float value) {
            return myValues[(int) value];
        }
    }





    //gets the integer values from the oracle date
    public ArrayList<Integer> getDate(String s){
        String[] str = s.split("-");
        ArrayList<Integer> ret = new ArrayList<>();

        ret.add(Integer.parseInt(str[0]));

        switch (str[1]){
            case "JAN":
                ret.add(1);
                break;
            case "FEB":
                ret.add(2);
                break;
            case "MAR":
                ret.add(3);
                break;
            case "APR":
                ret.add(4);
                break;
            case "MAY":
                ret.add(5);
                break;
            case "JUN":
                ret.add(6);
                break;
            case "JUL":
                ret.add(7);
                break;
            case "AUG":
                ret.add(8);
                break;
            case "SEP":
                ret.add(9);
                break;
            case "OCT":
                ret.add(10);
                break;
            case "NOV":
                ret.add(11);
                break;
            default:
                ret.add(12);
                break;
        }

        ret.add(Integer.parseInt("20" + str[2]));

        return ret;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateWeeklyGraph(thisView);
    }
}