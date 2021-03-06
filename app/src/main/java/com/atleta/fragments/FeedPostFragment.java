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
import com.atleta.models.PostModel;
import com.atleta.utils.AtletaApplication;

import java.util.ArrayList;
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
        View view = inflater.inflate(R.layout.fragment_feed_post, container, false);

        viewById(view);

        if (mAdapter == null) {
            mAdapter = new FeedPostAdapter(this.getContext(), getActivity(), this);
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

        }



    @Override
    public void onItemClick(PostModel item) {

    }
}