package com.atleta.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.atleta.activities.FirstUserEntryActivity;
import com.atleta.controllers.SocialLoginController;
import com.atleta.R;
import com.atleta.activities.HomeActivity;
import com.atleta.activities.SignUpActivity;
import com.atleta.customview.SpinnerView;
import com.atleta.models.ItemViewStateModel;
import com.atleta.models.Session;
import com.atleta.models.SignUpModel;
import com.atleta.utils.AppPreferences;
import com.atleta.utils.AtletaApplication;
import com.atleta.utils.Constants;
import com.atleta.utils.GsonRequest;
import com.atleta.utils.Keys;
import com.atleta.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignInFragment extends BaseFragment implements View.OnClickListener {

    private Button mBtnSignUp, mBtnLogin;
    private EditText mEdtLoginId, mEdtPassword;
    private SpinnerView spinnerView;
    private Session session;
    private SignUpActivity activity;
    public SocialLoginController socialLoginController;

    public static SignInFragment newInstance(@SuppressWarnings("SameParameterValue") String title) {
        SignInFragment fragment = new SignInFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (SignUpActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);

        findViewById(rootView);
        registerListenerId();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        socialLoginController = new SocialLoginController(activity, view,
                spinnerView, SignInFragment.this, SignInFragment.class.getSimpleName().toString());
    }

    private void findViewById(View view) {
        mEdtLoginId = view.findViewById(R.id.edt_login_id);
        mEdtPassword = view.findViewById(R.id.edt_password);
        mBtnSignUp = view.findViewById(R.id.btn_sign_up);
        mBtnLogin = view.findViewById(R.id.btn_login);
        spinnerView = view.findViewById(R.id.progress_bar);
    }

    private void registerListenerId() {
        mBtnSignUp.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);

        mEdtLoginId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getUserInformation(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getUserInformation(String mobileNumber) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_up:
                pushFragment(SignUpFragment.newInstance(null), true);
                break;
            case R.id.btn_login:
                if (isValidation()) {
                    spinnerView.setVisibility(View.VISIBLE);
                    sendDataToserver();
                }
                break;
        }
    }

    private boolean isValidation() {

        if (TextUtils.isEmpty(mEdtLoginId.getText().toString().trim())) {
            mEdtLoginId.setError("Enter email id here..");
            mEdtLoginId.requestFocus();
            return false;
        } else {
            mEdtLoginId.setError(null);
        }
        if (TextUtils.isEmpty(mEdtPassword.getText().toString().trim())) {
            mEdtPassword.setError("Enter password here..");
            mEdtPassword.requestFocus();
            return false;
        } else {
            mEdtPassword.setError(null);
        }

        return true;
    }

    private void sendDataToserver() {
        JSONObject params = new JSONObject();
        try {
            params.put("email", mEdtLoginId.getText().toString().trim());
            params.put("password", mEdtPassword.getText().toString().trim());
            params.put("deviceID", Utils.getDeviceId());
            params.put("deviceType", Constants.device);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = Constants.LOGIN;
        // Call login the api
        GsonRequest gsonRequest = new GsonRequest<SignUpModel>(Request.Method.POST, url, params, SignUpModel.class, response -> {
            spinnerView.setVisibility(View.GONE);
            if (isAdded()) {
                if (response.getStatus().equals("1")) {
                    AppPreferences.setSession(response.getUser());
                    startActivity(new Intent(getActivity().getApplicationContext(), FirstUserEntryActivity.class));
                    getActivity().finish();
                } else {
                    Utils.showToast(getActivity(), getActivity().findViewById(R.id.fragment_container), response.getMessage());
                }
            }
        }, error -> {
            spinnerView.setVisibility(View.GONE);

            if (isAdded()) {
                if (error.networkResponse != null) {
                    NetworkResponse response = error.networkResponse;
                }
                Utils.showToast(getActivity(), getActivity().findViewById(R.id.fragment_container), getActivity().getString(R.string.error_api_try_again));
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put(Keys.AUTHORIZATION, "");
                return headers;
            }
        };

        AtletaApplication.sharedInstance().addToRequestQueue(gsonRequest);

    }
}
