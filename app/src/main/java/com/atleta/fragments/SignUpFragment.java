package com.atleta.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.Nullable;
import com.atleta.activities.SignUpActivity;
import com.atleta.controllers.SocialLoginController;
import com.atleta.R;
import com.atleta.customview.SpinnerView;
import com.atleta.models.Session;
import com.atleta.utils.Utils;

public class SignUpFragment extends BaseFragment implements View.OnClickListener {

    private EditText mEdtDisplayName, mEdtEmailId, mEdtCreatePassword;
    private Button mBtnRegister, mBtnMale, mBtnFemale;
    private SpinnerView spinnerView;
    private DatePicker datePicker;
    private SignUpActivity activity;
    public SocialLoginController socialLoginController;
    private boolean isMaleCheck = true;
    public static SignUpFragment newInstance(@SuppressWarnings("SameParameterValue") String title) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (SignUpActivity)context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);


        findViewById(rootView);
        registerListenerId();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        socialLoginController = new SocialLoginController(activity,view,
                spinnerView, SignUpFragment.this, SignUpFragment.class.getSimpleName().toString());
    }

    private void findViewById(View view) {
        mEdtEmailId = view.findViewById(R.id.edt_email_id);
        mEdtCreatePassword = view.findViewById(R.id.edt_create_password);
        mEdtDisplayName = view.findViewById(R.id.edt_display_name);
        mBtnMale = view.findViewById(R.id.btn_male);
        mBtnFemale = view.findViewById(R.id.btn_female);
        datePicker = view.findViewById(R.id.datePicker1);
        mBtnRegister = view.findViewById(R.id.btn_register);
        spinnerView = view.findViewById(R.id.spinnerView);
    }

    private void registerListenerId() {

        mBtnMale.setOnClickListener(this);
        mBtnFemale.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_register:
                if (isValidation()) {
                    spinnerView.setVisibility(View.VISIBLE);
                    String displayName = mEdtDisplayName.getText().toString().trim();
                    String emailId = mEdtEmailId.getText().toString().trim();
                    String password = mEdtCreatePassword.getText().toString().trim();
                    String gender = isMaleCheck ? "male" : "female";
                    String dob = datePicker.getDayOfMonth()+"/"+ (datePicker.getMonth() + 1)+"/"+datePicker.getYear();

                    Session session = new Session(displayName, emailId, password, gender, dob);

                    new Handler().postDelayed(() -> {
                        spinnerView.setVisibility(View.GONE);
                        pushFragment(SelectSportsType.newInstance("", session), true);
                    }, 3000);
                }
                break;
            case R.id.btn_male:changeGenderState(R.id.btn_male); break;
            case R.id.btn_female:changeGenderState(R.id.btn_female); break;
        }
    }


    private boolean isValidation() {

        if (TextUtils.isEmpty(mEdtDisplayName.getText().toString().trim())) {
            mEdtDisplayName.setError("Enter name here..");
            mEdtDisplayName.requestFocus();
            return false;
        } else {
            mEdtDisplayName.setError(null);
        }

        if (TextUtils.isEmpty(mEdtEmailId.getText().toString().trim())) {
            mEdtEmailId.setError("Enter email address here..");
            mEdtEmailId.requestFocus();
            return false;
        } else {
            mEdtEmailId.setError(null);
        }

        if (!Utils.isValidEmail(mEdtEmailId.getText().toString().trim())) {
            mEdtEmailId.setError("Enter valid email address ..");
            mEdtEmailId.requestFocus();
            return false;
        } else {
            mEdtEmailId.setError(null);
        }
        if (TextUtils.isEmpty(mEdtCreatePassword.getText().toString().trim())) {
            mEdtCreatePassword.setError("Enter password here..");
            mEdtCreatePassword.requestFocus();
            return false;
        } else {
            mEdtCreatePassword.setError(null);
        }
        return true;
    }

    private void changeGenderState(int id) {
        switch(id) {
            case R.id.btn_male:
                isMaleCheck = true;
                mBtnMale.setBackground(getResources().getDrawable(R.drawable.border_button_orange));
                mBtnFemale.setBackground(getResources().getDrawable(R.drawable.border_button_black));
                break;
            case R.id.btn_female:
                isMaleCheck = false;
                mBtnFemale.setBackground(getResources().getDrawable(R.drawable.border_button_orange));
                mBtnMale.setBackground(getResources().getDrawable(R.drawable.border_button_black));
                break;
        }
    }
}
