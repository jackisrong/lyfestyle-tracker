package com.example.lyfestyletracker;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.lyfestyletracker.web.QueryExecutable;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserConsultantsList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserConsultantsList extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View thisView;
    public UserConsultantsList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserConsultantsList.
     */
    // TODO: Rename and change types and number of parameters
    public static UserConsultantsList newInstance(String param1, String param2) {
        UserConsultantsList fragment = new UserConsultantsList();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        thisView = inflater.inflate(R.layout.fragment_user_consultants_list, container, false);
        populateTable();
        return thisView;
    }

    public void populateTable(){
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("query_type", "special");
        map.put("extra", "Select c.username, c.result, co.name FROM Consultant c, Company co, ConsultantWorksForCompany cw WHERE cw.consultantUsername = c.username AND co.companyID = cw.companyId AND c.username IN (SELECT consultantUsername FROM UserHiresConsultant WHERE userUsername = '" + mParam1 +"' AND consultantUsername = c.username) ORDER BY c.result");

        QueryExecutable qe = new QueryExecutable(map);
        JSONArray ans = qe.run();
        System.out.println(ans);

        TableLayout mainTable = thisView.findViewById(R.id.consultant_log_main_table);

        mainTable.removeAllViews();

        if (ans == null) {
            return;
        }

        for (int i = 0; i < ans.length(); i++) {
            try {
                JSONObject o = ans.getJSONObject(i);

                TableRow row = new TableRow(getContext());
                row.setWeightSum(1.0f);
                row.setPadding(0, 10, 0, 10);
                if (i % 2 == 0) {
                    row.setBackgroundColor(getContext().getColor(R.color.table_light1));
                } else {
                    row.setBackgroundColor(getContext().getColor(R.color.table_light2));
                }

                TableRow.LayoutParams paramsusername = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.28f);
                TableRow.LayoutParams paramsCompany = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.42f);
                TableRow.LayoutParams paramsRating = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.15f);


                TextView usernameText = new TextView(getContext());
                usernameText.setText(o.getString("USERNAME"));
                usernameText.setLayoutParams(paramsusername);
                usernameText.setGravity(Gravity.CENTER_HORIZONTAL);

                TextView companyText = new TextView(getContext());
                companyText.setText(o.getString("NAME"));
                companyText.setLayoutParams(paramsCompany);

                TextView ratingText = new TextView(getContext());
                ratingText.setText(o.getString("RESULT"));
                ratingText.setLayoutParams(paramsRating);
                ratingText.setGravity(Gravity.CENTER_HORIZONTAL);


                row.addView(usernameText);
                row.addView(companyText);
                row.addView(ratingText);
                mainTable.addView(row);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        populateTable();
    }
}