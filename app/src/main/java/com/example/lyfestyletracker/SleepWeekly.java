package com.example.lyfestyletracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.lyfestyletracker.web.QueryExecutable;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.joda.time.LocalDate;
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
 * Use the {@link SleepWeekly#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SleepWeekly extends Fragment {

    private String username;
    private View thisView;

    public SleepWeekly() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param username username.
     * @return A new instance of fragment.
     */
    public static SleepWeekly newInstance(String username) {
        SleepWeekly fragment = new SleepWeekly();
        Bundle args = new Bundle();
        args.putString("username", username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString("username");
        }
        Map<String, Object> map = new LinkedHashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        thisView = inflater.inflate(R.layout.fragment_sleep_weekly, container, false);
        updateWeeklyGraph(thisView);
        return thisView;
    }

    //updates the weekly graph with the queries
    protected void updateWeeklyGraph(View v) {
        Map<String, Object> map = new LinkedHashMap<>();

        BarChart barChart = v.findViewById(R.id.weekly_bar_chart);
        barChart.clear();

        map.put("query_type", "special");
        map.put("extra", "Select username, sleepTime, sleepDate from UserSleepEntry WHERE username = '" + username + "' AND sleepDate >= TRUNC(SYSDATE, 'DY') AND sleepDate < TRUNC(SYSDATE, 'DY') + 7 ORDER BY sleepdate");

        QueryExecutable qe = new QueryExecutable(map);
        JSONArray res = qe.run();
        System.out.println(res);

        HashMap<Integer, Integer> sleepTimes = new HashMap<>();

        //gets the week day from the date format
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E");
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        for (int i = 0; i < res.length(); i++) {
            try {
                ArrayList<Integer> dates = getDate(res.getJSONObject(i).getString("SLEEPDATE"));

                LocalDate ld = new LocalDate(dates.get(2), dates.get(1), dates.get(0));
                System.out.println(ld.getDayOfWeek());
                sleepTimes.put(ld.getDayOfWeek(), Integer.parseInt(res.getJSONObject(i).getString("SLEEPTIME")));
                System.out.println(ld.getDayOfWeek());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        int[] ints = {7,1,2,3,4,5,6};

        for (int i = 0; i < days.length; i++) {
            if (sleepTimes.containsKey(ints[i])) {
                barEntries.add(new BarEntry(i, sleepTimes.get(ints[i])));
            } else {
                barEntries.add(new BarEntry(i, 0));
            }
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Weekly Sleep");
        barDataSet.setColors(ColorTemplate.PASTEL_COLORS);

        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.9f);

        barChart.setData(data);
        barChart.getAxisLeft().setAxisMinimum(0);
        barChart.getAxisRight().setAxisMinimum(0);
        barChart.getDescription().setEnabled(false);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new XaxisValueFormatter(days));
    }

    public static class XaxisValueFormatter extends IndexAxisValueFormatter {
        private String[] myValues;

        public XaxisValueFormatter(String[] values) {
            myValues = values;
        }

        @Override
        public String getFormattedValue(float value) {
            return myValues[(int) value];
        }
    }


    //gets the integer values from the oracle date
    public ArrayList<Integer> getDate(String s) {
        String[] str = s.split("-");
        ArrayList<Integer> ret = new ArrayList<>();

        ret.add(Integer.parseInt(str[0]));

        switch (str[1]) {
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