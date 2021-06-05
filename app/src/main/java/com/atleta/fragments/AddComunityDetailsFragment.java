package com.atleta.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.atleta.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddComunityDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddComunityDetailsFragment extends BaseFragment implements View.OnClickListener{

    private Button mBtnNext;
    private EditText mEdtCommunityName, mEdtAboutCommunity;
    private Spinner spinnerTopic, spinnerScope;
    private RadioGroup mRadioGroupVisibility;
    private RadioButton mRadioPublic, mRadioInvite;
    private TextView mTxtRadioVisibilityUpdate;

    public static AddComunityDetailsFragment newInstance(String title) {
        AddComunityDetailsFragment fragment = new AddComunityDetailsFragment();
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
        return inflater.inflate(R.layout.fragment_add_comunity_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewById(view);
        registerListener();

    }

    private void viewById(View view) {
        mEdtCommunityName = view.findViewById(R.id.edt_community_name);
        mEdtAboutCommunity = view.findViewById(R.id.edt_about_coummunity);
        spinnerTopic = view.findViewById(R.id.spinner_topic);
        spinnerScope = view.findViewById(R.id.spinner_scope);
        mRadioGroupVisibility = view.findViewById(R.id.radio_visibility);
        mRadioPublic = view.findViewById(R.id.radio_public);
        mRadioInvite = view.findViewById(R.id.radio_invite);
        mTxtRadioVisibilityUpdate = view.findViewById(R.id.radio_txt_update);
        mBtnNext = view.findViewById(R.id.btn_next);
    }

    private void registerListener() {
        mBtnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:break;
        }
    }
}