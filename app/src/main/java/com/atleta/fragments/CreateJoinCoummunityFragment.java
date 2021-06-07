package com.atleta.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.atleta.R;
import com.atleta.customview.SpinnerView;

public class CreateJoinCoummunityFragment extends BaseFragment {

    private SpinnerView spinnerView;
    public static CreateJoinCoummunityFragment newInstance(@SuppressWarnings("SameParameterValue") String title) {
        CreateJoinCoummunityFragment fragment = new CreateJoinCoummunityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_create_join_community, container, false);


        findViewById(rootView);
        registerListenerId();
        return rootView;
    }

    private void findViewById(View view) {
        spinnerView = view.findViewById(R.id.spinnerView);
    }

    private void registerListenerId() {

    }
}
