package com.atleta.fragments;


import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.atleta.activities.HomeActivity;
import com.google.firebase.database.DatabaseReference;
import com.atleta.R;
import com.atleta.customview.SpinnerView;
import com.atleta.models.Session;
import com.atleta.models.UserModel;
import com.atleta.utils.AppPreferences;
import com.atleta.utils.AtletaApplication;
import com.atleta.utils.Utils;

public class SignUpFragment extends BaseFragment implements View.OnClickListener {

    private EditText mEdtDisplayName, mEdtEmailId, mEdtCreatePassword, mEdtRepeatPassword;
    private Button mBtnRegister;
    private SpinnerView spinnerView;

    public static SignUpFragment newInstance(@SuppressWarnings("SameParameterValue") String title) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
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

    private void findViewById(View view) {
        mEdtEmailId = view.findViewById(R.id.edt_email_id);
        mEdtCreatePassword = view.findViewById(R.id.edt_create_password);
        mEdtRepeatPassword = view.findViewById(R.id.edt_repeat_password);
        mEdtDisplayName = view.findViewById(R.id.edt_display_name);
        mBtnRegister = view.findViewById(R.id.btn_register);
        spinnerView = view.findViewById(R.id.spinnerView);
    }

    private void registerListenerId() {

        mBtnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_register:
                if (!isValidation()) {
                    spinnerView.setVisibility(View.VISIBLE);
                    String userId = mEdtEmailId.getText().toString().trim();
                    String displayName = mEdtDisplayName.getText().toString().trim();
                    String emailId = mEdtEmailId.getText().toString().trim();
                    String password = mEdtCreatePassword.getText().toString().trim();
                    String repeatPassword = mEdtRepeatPassword.getText().toString().trim();
                    boolean isAdmin = false;
                    new Handler().postDelayed(() -> {
                        spinnerView.setVisibility(View.GONE);
                        pushFragment(SelectSportsType.newInstance(""), true);
                    }, 3000);

                }

                break;
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
        if (TextUtils.isEmpty(mEdtRepeatPassword.getText().toString().trim())) {
            mEdtRepeatPassword.setError("Enter repeat password here..");
            mEdtRepeatPassword.requestFocus();
            return false;
        } else {
            mEdtRepeatPassword.setError(null);
        }

        if(!mEdtRepeatPassword.getText().toString().trim().equals(mEdtCreatePassword.getText().toString().trim())) {
            mEdtRepeatPassword.setError("Enter correct repeat password here..");
            mEdtRepeatPassword.requestFocus();
            return false;
        }else {
            mEdtRepeatPassword.setError(null);
        }
        return true;
    }
}
