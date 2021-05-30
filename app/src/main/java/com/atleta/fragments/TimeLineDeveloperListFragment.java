package com.atleta.fragments;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.atleta.R;
import com.atleta.activities.MainActivity;
import com.atleta.adapters.TimeLineDeveloperListAdapter;
import com.atleta.customview.SpinnerView;
import com.atleta.models.ApplyJob;
import com.atleta.models.MyJobsModel;
import com.atleta.models.Session;
import com.atleta.utils.AppPreferences;
import com.atleta.utils.AtletaApplication;
import com.atleta.utils.Constants;
import com.atleta.utils.Keys;
import com.atleta.utils.MySingleton;
import com.atleta.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;

public class TimeLineDeveloperListFragment extends BaseFragment implements TimeLineDeveloperListAdapter
        .OnItemClickListener {
    private TimeLineDeveloperListAdapter mAdapter;
    private ListView listRequirement;
    private ImageView noRecordFound;
    private MyJobsModel item;
    private MainActivity activity;
    private SpinnerView spinnerView;

    public static TimeLineDeveloperListFragment newInstance(String title, MyJobsModel model) {
        TimeLineDeveloperListFragment fragment = new TimeLineDeveloperListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putParcelable("item", model);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);
        viewById(view);

        if (getArguments() != null) {
            item = getArguments().getParcelable("item");
        }

        if (mAdapter == null) {
            mAdapter = new TimeLineDeveloperListAdapter(this.getContext(), this);
        }

        listRequirement.setAdapter(mAdapter);

        getdata();
        return view;
    }

    private void viewById(View view) {
        listRequirement = view.findViewById(R.id.listView);
        spinnerView = view.findViewById(R.id.spinnerView);
        noRecordFound = view.findViewById(R.id.no_record_found);
    }

    private void getdata() {
        spinnerView.setVisibility(View.VISIBLE);
        // calling add value event listener method
        // for getting the values from database.
        Session session = AppPreferences.getSession();

        if (session != null) {
            AtletaApplication.sharedDatabaseInstance().child("MyJobs").child(item.getKey()).child("applyJob").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<ApplyJob> applyJobsList = new ArrayList<>();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String actionType = ds.child("actionType").getValue(String.class);
                        Session session = ds.child("session").getValue(Session.class);
                        int isShowHighlight = ds.child("isShowHighlight").getValue(Integer.class);
                        if (session != null)
                            applyJobsList.add(new ApplyJob(actionType, session, isShowHighlight));
                    }
                    if (applyJobsList.size() > 0) {
                        listRequirement.setVisibility(View.VISIBLE);
                        noRecordFound.setVisibility(GONE);
                        mAdapter.setItems(applyJobsList, 10);
                    } else {
                        listRequirement.setVisibility(GONE);
                        noRecordFound.setVisibility(View.VISIBLE);
                    }

                    spinnerView.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // calling on cancelled method when we receive
                    // any error or we are not able to get the data.
                    Toast.makeText(getContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onItemClick(Session item) {

        spinnerView.setVisibility(View.VISIBLE);
        String topic = item.getUserToken(); //topic must match with what the receiver subscribed to
        String title = "Congratulations " + item.getUserModel().getFirstName();
        String message = "Your profile just got matched to a client Requirement \n Time to get Work";

        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", title);
            notifcationBody.put("message", message);
            notifcationBody.put(Keys.TYPE, Keys.TYPE_DETAIL);

            notification.put("to", topic);
            notification.put("data", notifcationBody);
        } catch (JSONException e) {
            Log.e(Constants.TAG, "onCreate: " + e.getMessage());
        }
        sendNotification(notification);
    }

    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Constants.FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        spinnerView.setVisibility(View.GONE);
                        Utils.showToast(activity, activity.findViewById(R.id.fragment_container), "Notification sent");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(activity, "Request error", Toast.LENGTH_LONG).show();
                        spinnerView.setVisibility(View.GONE);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", Constants.serverKey);
                params.put("Content-Type", Constants.contentType);
                return params;
            }
        };
        MySingleton.getInstance(activity).addToRequestQueue(jsonObjectRequest);
    }
}
