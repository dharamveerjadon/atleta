package com.atleta.fragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.atleta.R;
import com.atleta.activities.MainActivity;
import com.atleta.adapters.WatingAdapter;
import com.atleta.customview.SpinnerView;
import com.atleta.models.ApplyJob;
import com.atleta.models.MyJobsModel;
import com.atleta.models.Session;
import com.atleta.utils.AppPreferences;
import com.atleta.utils.AtletaApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.view.View.GONE;

public class WaitingFragment extends BaseFragment implements WatingAdapter.OnItemClickListener {

    private WatingAdapter mAdapter;
    private ListView listRequirement;
    private ImageView noRecordFound;
    private MainActivity activity;
    private SpinnerView spinnerView;


    public static WaitingFragment newInstance(String title) {
        WaitingFragment fragment = new WaitingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity)context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_waiting, container, false);

        viewById(view);

        if (mAdapter == null) {
            mAdapter = new WatingAdapter(this.getContext(), this);
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
        final Session session = AppPreferences.getSession();

        if (session != null) {
            AtletaApplication.sharedDatabaseInstance().child("MyJobs").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<MyJobsModel> myJob = new ArrayList<>();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String title = ds.child("title").getValue(String.class);
                        String description = ds.child("description").getValue(String.class);
                        String date = ds.child("date").getValue(String.class);
                        String jobType = ds.child("jobType").getValue(String.class);
                        String skills = ds.child("skills").getValue(String.class);
                        String yearOfExperience = ds.child("yearOfExperience").getValue(String.class);
                        String budgets = ds.child("budgets").getValue(String.class);
                        String key = ds.child("key").getValue(String.class);
                        ApplyJob applyJob = ds.child("applyJob").child(session.getUserModel().getMobileNumber()).getValue(ApplyJob.class);
                        if(applyJob == null)
                            myJob.add(new MyJobsModel(title, description, date, jobType, yearOfExperience, skills, budgets, "InProcess",key, applyJob));

                    }

                    if (myJob.size() > 0) {
                        listRequirement.setVisibility(View.VISIBLE);
                        noRecordFound.setVisibility(GONE);
                        Collections.sort(myJob);

                        mAdapter.setItems(myJob, 10);
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
                    Toast.makeText(activity, "Fail to get data.", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public void onItemClick(MyJobsModel item) {
        pushFragment(CongratulationFragment.newInstance("Congratulation", item), true);
    }
}
