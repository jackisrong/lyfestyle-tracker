package com.example.lyfestyletracker;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lyfestyletracker.web.QueryExecutable;

import org.json.JSONArray;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SleepTab1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SleepTab1 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SleepTab1() {
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
    public static SleepTab1 newInstance(String param1, String param2) {
        SleepTab1 fragment = new SleepTab1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Map<String,Object> map = new LinkedHashMap<>();

        map.put("query_type", "special");
        map.put("columns", "all");
        map.put("table", "userPerson");
        map.put("username", "bob123");
        map.put("password", "12345;)");
        map.put("extra", "Select up.username, us.sleepTime from userPerson up, UserSleepEntry us WHERE up.username = us.username");
        //map.put("extra", "UPDATE People SET name = 'Luis E' WHERE username = 'Luis'");

        //"https://www.students.cs.ubc.ca/~luigi28/hello.php?query_type=select&columns=all&table=userperson&username=bob123&password=12345;)"
        QueryExecutable qe = new QueryExecutable(map);
        JSONArray res = qe.run();
        System.out.println(res);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sleep_tab1, container, false);
    }
}