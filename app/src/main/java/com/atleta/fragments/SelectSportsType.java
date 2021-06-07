package com.atleta.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.atleta.R;
import com.atleta.activities.FirstUserEntryActivity;
import com.atleta.customview.SpinnerView;

public class SelectSportsType extends BaseFragment implements View.OnClickListener {
    private SpinnerView spinnerView;
    private Button mBtnSubmit;
    public static SelectSportsType newInstance(String title) {
        SelectSportsType fragment = new SelectSportsType();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_sports_type, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViewById(view);
        registerListenerId();
    }
    private void findViewById(View view) {
        spinnerView = view.findViewById(R.id.spinnerView);
        mBtnSubmit = view.findViewById(R.id.btn_register);
    }

    private void registerListenerId() {

        mBtnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_register:
                spinnerView.setVisibility(View.VISIBLE);
                new Handler().postDelayed(() -> {
                    spinnerView.setVisibility(View.GONE);
                    startActivity(new Intent(getActivity().getApplicationContext(), FirstUserEntryActivity.class));
                    getActivity().finish();
                }, 3000);
        }
    }
}