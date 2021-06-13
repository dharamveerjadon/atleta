package com.atleta.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.atleta.R;
import com.atleta.activities.FirstUserEntryActivity;
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

public class SelectSportsType extends BaseFragment implements View.OnClickListener {
    private final ArrayList<ItemViewStateModel> sportsType = new ArrayList<>();
    private SpinnerView spinnerView;
    private Button mBtnSubmit;
    private LinearLayout lnBasketball, lnFootball, lnCricket, lnFitness, lnHockey, lnOther;
    private ImageView imgBasketball, imgFootball, imgCricket, imgFitness, imgHockey, imgOther;
    private TextView txtBasketBall, txtFootball, txtCricket, txtFitness, txtHockey, txtOther;
    private boolean isBasketball, isFootball, isCricket, isFitness, isHockey, isOther;
    private Session sessionItem;

    public static SelectSportsType newInstance(String title, Session session) {
        SelectSportsType fragment = new SelectSportsType();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putParcelable("item", session);
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
        if (getArguments() != null) {
            sessionItem = getArguments().getParcelable("item");
        }
        findViewById(view);
        registerListenerId();
    }

    private void findViewById(View view) {
        lnBasketball = view.findViewById(R.id.fl_basketball);
        lnFootball = view.findViewById(R.id.fl_football);
        lnCricket = view.findViewById(R.id.fl_cricket);
        lnFitness = view.findViewById(R.id.fl_fitness);
        lnHockey = view.findViewById(R.id.fl_hockey);
        lnOther = view.findViewById(R.id.fl_other);

        imgBasketball = view.findViewById(R.id.img_basketball);
        imgFootball = view.findViewById(R.id.img_football);
        imgCricket = view.findViewById(R.id.img_cricket);
        imgFitness = view.findViewById(R.id.img_fitness);
        imgHockey = view.findViewById(R.id.img_hockey);
        imgOther = view.findViewById(R.id.img_other);

        txtBasketBall = view.findViewById(R.id.txt_basketball);
        txtFootball = view.findViewById(R.id.txt_football);
        txtCricket = view.findViewById(R.id.txt_cricket);
        txtFitness = view.findViewById(R.id.txt_fitness);
        txtHockey = view.findViewById(R.id.txt_hockey);
        txtOther = view.findViewById(R.id.txt_other);

        spinnerView = view.findViewById(R.id.spinnerView);
        mBtnSubmit = view.findViewById(R.id.btn_register);
    }

    private void registerListenerId() {

        lnBasketball.setOnClickListener(this);
        lnFootball.setOnClickListener(this);
        lnCricket.setOnClickListener(this);
        lnFitness.setOnClickListener(this);
        lnHockey.setOnClickListener(this);
        lnOther.setOnClickListener(this);

        mBtnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_register:
                if (sportsType.size() > 0) {
                    spinnerView.setVisibility(View.VISIBLE);
                    sendDataToserver();
                } else {
                    Utils.showToast(getActivity(), getActivity().findViewById(R.id.fragment_container), "Please select your favourite game");
                }
                break;
            case R.id.fl_basketball:
                changeBasketBallColorState(isBasketball);
                break;
            case R.id.fl_football:
                changeFootballColorState(isFootball);
                break;
            case R.id.fl_cricket:
                changeCricketColorState(isCricket);
                break;
            case R.id.fl_fitness:
                changeFitnessColorState(isFitness);
                break;
            case R.id.fl_hockey:
                changeHockeyColorState(isHockey);
                break;
            case R.id.fl_other:
                changeOtherColorState(isOther);
                break;
        }
    }

    private void sendDataToserver() {

        ArrayList<String> SelectedIdList = new ArrayList<>();
        for (ItemViewStateModel itemViewStateModel : sportsType) {
            SelectedIdList.add(itemViewStateModel.getId());
        }
        String selectedSports = TextUtils.join(",", SelectedIdList);
        JSONObject params = new JSONObject();
        try {
            params.put("name", sessionItem.getName());

            params.put("email", sessionItem.getEmailId());
            params.put("number", sessionItem.getNumber());
            params.put("password", sessionItem.getPassword());
            params.put("gender", sessionItem.getGender());
            params.put("dob", sessionItem.getDob());
            params.put("sports", selectedSports);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = Constants.SIGN_UP;
        // Call login the api
        GsonRequest gsonRequest = new GsonRequest<SignUpModel>(Request.Method.POST, url, params, SignUpModel.class, response -> {
            spinnerView.setVisibility(View.GONE);
            if (isAdded()) {
                if(response.getStatus().equals("1")) {
                    AppPreferences.setSession(response.getUser());
                    startActivity(new Intent(getActivity().getApplicationContext(), FirstUserEntryActivity.class));
                    getActivity().finish();
                }else {
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

    private void changeBasketBallColorState(boolean value) {
        ItemViewStateModel item = new ItemViewStateModel("1", "basketball", value);
        if (value) {
            isBasketball = false;
            lnBasketball.setBackground(getResources().getDrawable(R.drawable.bg_white_border_gray));
            txtBasketBall.setTextColor(getResources().getColor(R.color.colorBlack));
            for (int i = 0; i < sportsType.size(); i++) {
                if (sportsType.get(i).getId().equals("1")) {
                    sportsType.remove(i);
                }
            }
        } else {
            isBasketball = true;
            lnBasketball.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            txtBasketBall.setTextColor(getResources().getColor(R.color.colorWhite));
            sportsType.add(item);
        }
    }

    private void changeFootballColorState(boolean value) {
        ItemViewStateModel item = new ItemViewStateModel("2", "football", value);
        if (value) {
            isFootball = false;
            lnFootball.setBackground(getResources().getDrawable(R.drawable.bg_white_border_gray));
            txtFootball.setTextColor(getResources().getColor(R.color.colorBlack));
            for (int i = 0; i < sportsType.size(); i++) {
                if (sportsType.get(i).getId().equals("2")) {
                    sportsType.remove(i);
                }
            }
        } else {
            isFootball = true;
            lnFootball.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            txtFootball.setTextColor(getResources().getColor(R.color.colorWhite));
            sportsType.add(item);
        }

    }

    private void changeCricketColorState(boolean value) {
        ItemViewStateModel item = new ItemViewStateModel("3", "cricket", value);
        if (value) {
            isCricket = false;
            lnCricket.setBackground(getResources().getDrawable(R.drawable.bg_white_border_gray));
            txtCricket.setTextColor(getResources().getColor(R.color.colorBlack));
            for (int i = 0; i < sportsType.size(); i++) {
                if (sportsType.get(i).getId().equals("3")) {
                    sportsType.remove(i);
                }
            }
        } else {
            isCricket = true;
            lnCricket.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            txtCricket.setTextColor(getResources().getColor(R.color.colorWhite));
            sportsType.add(item);
        }

    }

    private void changeFitnessColorState(boolean value) {
        ItemViewStateModel item = new ItemViewStateModel("4", "fitness", value);
        if (value) {
            isFitness = false;
            lnFitness.setBackground(getResources().getDrawable(R.drawable.bg_white_border_gray));
            txtFitness.setTextColor(getResources().getColor(R.color.colorBlack));
            for (int i = 0; i < sportsType.size(); i++) {
                if (sportsType.get(i).getId().equals("4")) {
                    sportsType.remove(i);
                }
            }
        } else {
            isFitness = true;
            lnFitness.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            txtFitness.setTextColor(getResources().getColor(R.color.colorWhite));
            sportsType.add(item);
        }

    }

    private void changeHockeyColorState(boolean value) {
        ItemViewStateModel item = new ItemViewStateModel("5", "hockey", value);
        if (value) {
            isHockey = false;
            lnHockey.setBackground(getResources().getDrawable(R.drawable.bg_white_border_gray));
            txtHockey.setTextColor(getResources().getColor(R.color.colorBlack));
            for (int i = 0; i < sportsType.size(); i++) {
                if (sportsType.get(i).getId().equals("5")) {
                    sportsType.remove(i);
                }
            }
        } else {
            isHockey = true;
            lnHockey.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            txtHockey.setTextColor(getResources().getColor(R.color.colorWhite));
            sportsType.add(item);
        }

    }

    private void changeOtherColorState(boolean value) {
        ItemViewStateModel item = new ItemViewStateModel("6", "other", value);
        if (value) {
            isOther = false;
            lnOther.setBackground(getResources().getDrawable(R.drawable.bg_white_border_gray));
            txtOther.setTextColor(getResources().getColor(R.color.colorBlack));
            for (int i = 0; i < sportsType.size(); i++) {
                if (sportsType.get(i).getId().equals("6")) {
                    sportsType.remove(i);
                }
            }
        } else {
            isOther = true;
            lnOther.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            txtOther.setTextColor(getResources().getColor(R.color.colorWhite));
            sportsType.add(item);
        }
    }

}