package com.atleta.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.atleta.R;
import com.atleta.activities.HomeActivity;
import com.atleta.adapters.FeedPostAdapter;
import com.atleta.customview.SpinnerView;
import com.atleta.models.MyJobsModel;
import com.atleta.utils.AtletaApplication;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedPostFragment extends BaseFragment implements FeedPostAdapter.OnItemClickListener {

    private FeedPostAdapter mAdapter;
    private ListView listRequirement;
    private ImageView noRecordFound;
    private HomeActivity activity;
    private SpinnerView spinnerView;


    public static FeedPostFragment newInstance(String title) {
        FeedPostFragment fragment = new FeedPostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (HomeActivity)context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_waiting, container, false);

        viewById(view);

        if (mAdapter == null) {
            mAdapter = new FeedPostAdapter(this.getContext(), this);
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

            AtletaApplication.sharedDatabaseInstance().child("Feeds").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<MyJobsModel> myJob = new ArrayList<>();

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



    @Override
    public void onItemClick(MyJobsModel item) {

    }
}