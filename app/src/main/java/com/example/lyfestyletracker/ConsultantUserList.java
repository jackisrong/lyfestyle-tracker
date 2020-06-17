package com.example.lyfestyletracker;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.lyfestyletracker.web.QueryExecutable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class ConsultantUserList extends Fragment {

    private String username;
    private View thisView;

    public ConsultantUserList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param username username.
     * @return A new instance of fragment FoodDiets.
     */
    public static ConsultantUserList newInstance(String username) {
        ConsultantUserList fragment = new ConsultantUserList();
        Bundle args = new Bundle();
        args.putString("username", username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(username);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        thisView = inflater.inflate(R.layout.fragment_consultant_user_division, container, false);
        populateTable();
        return thisView;
    }

    public void populateTable() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("query_type", "special");
        map.put("extra", "Select u.username, p.email, uhc.contractNumber From UserPerson u, UserHiresConsultant uhc, People p Where uhc.userUsername = u.username AND p.username = u.username AND uhc.consultantUsername = '" + username + "'");
        QueryExecutable qe = new QueryExecutable(map);
        JSONArray ans = qe.run();
        System.out.println(ans);

        TableLayout mainTable = thisView.findViewById(R.id.user_log_main_table);
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
                TableRow.LayoutParams paramsEmail = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.42f);
                TableRow.LayoutParams paramsContract = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.15f);

                TextView usernameText = new TextView(getContext());
                usernameText.setText(o.getString("USERNAME"));
                usernameText.setLayoutParams(paramsusername);
                usernameText.setGravity(Gravity.CENTER_HORIZONTAL);

                TextView emailText = new TextView(getContext());
                emailText.setText(o.getString("EMAIL"));
                emailText.setLayoutParams(paramsEmail);

                TextView contractText = new TextView(getContext());
                contractText.setText(o.getString("CONTRACTNUMBER"));
                contractText.setLayoutParams(paramsContract);
                contractText.setGravity(Gravity.CENTER_HORIZONTAL);

                row.addView(usernameText);
                row.addView(emailText);
                row.addView(contractText);
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
