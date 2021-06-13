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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atleta.R;
import com.atleta.activities.HomeActivity;
import com.atleta.adapters.CommunityAdapter;
import com.atleta.customview.SpinnerView;
import com.atleta.models.AddCommunityModel;
import com.atleta.models.PostModel;
import com.atleta.utils.AtletaApplication;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommunityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommunityFragment extends BaseFragment implements CommunityAdapter.OnItemClickListener {

    private CommunityAdapter mAdapter;
    private RecyclerView listRequirement;
    private ImageView noRecordFound;
    private HomeActivity activity;
    private SpinnerView spinnerView;


    public static CommunityFragment newInstance(String title) {
        CommunityFragment fragment = new CommunityFragment();
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
        View view = inflater.inflate(R.layout.fragment_community, container, false);

        viewById(view);

        if (mAdapter == null) {
            mAdapter = new CommunityAdapter(this.getContext(), getActivity(), this);
        }

        listRequirement.setAdapter(mAdapter);

        getdata();

        return view;
    }

    private void viewById(View view) {
        listRequirement = view.findViewById(R.id.listView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        listRequirement.setLayoutManager(gridLayoutManager);
        spinnerView = view.findViewById(R.id.spinnerView);
        noRecordFound = view.findViewById(R.id.no_record_found);


    }


    private void getdata() {
        spinnerView.setVisibility(View.VISIBLE);


        }



    @Override
    public void onItemClick(AddCommunityModel item) {

    }
}