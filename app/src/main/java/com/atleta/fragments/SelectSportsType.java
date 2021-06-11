package com.atleta.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.UiThread;

import com.atleta.R;
import com.atleta.activities.FirstUserEntryActivity;
import com.atleta.customview.SpinnerView;
import com.atleta.models.ItemViewStateModel;
import com.atleta.models.Session;
import com.atleta.utils.AtletaApplication;
import com.atleta.utils.Utils;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class SelectSportsType extends BaseFragment implements View.OnClickListener {
    private SpinnerView spinnerView;
    private Button mBtnSubmit;
    private LinearLayout lnBasketball, lnFootball, lnCricket, lnFitness, lnHockey, lnOther;
    private ImageView imgBasketball, imgFootball, imgCricket, imgFitness, imgHockey, imgOther;
    private TextView txtBasketBall, txtFootball, txtCricket, txtFitness, txtHockey, txtOther;
    private boolean isBasketball, isFootball, isCricket, isFitness, isHockey, isOther;
    private ArrayList<ItemViewStateModel> sportsType = new ArrayList<>();

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
                if(sportsType.size() > 0) {
                    spinnerView.setVisibility(View.VISIBLE);
                    // Fetching all notes
                    AtletaApplication.sharedAtletaApiServicesInstance().register("name", "name@gmail.com", "123", "123456789", "male", "10/05/1995", "1,2,3")
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableSingleObserver<Session>() {
                                @Override
                                public void onSuccess(@NonNull Session session) {
                                    new Handler().postDelayed(() -> {
                                        spinnerView.setVisibility(View.GONE);
                                        startActivity(new Intent(getActivity().getApplicationContext(), FirstUserEntryActivity.class));
                                        getActivity().finish();
                                    }, 3000);
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    spinnerView.setVisibility(View.GONE);
                                }
                            });
                }else {
                    Utils.showToast(getActivity(), getActivity().findViewById(R.id.fragment_container),"Please select your favourite game");
                }

                break;
            case R.id.fl_basketball: changeBasketBallColorState(isBasketball);break;
            case R.id.fl_football: changeFootballColorState(isFootball);break;
            case R.id.fl_cricket: changeCricketColorState(isCricket);break;
            case R.id.fl_fitness: changeFitnessColorState(isFitness);break;
            case R.id.fl_hockey: changeHockeyColorState(isHockey);break;
            case R.id.fl_other: changeOtherColorState(isOther);break;
        }
    }

    private void changeBasketBallColorState(boolean value) {
        ItemViewStateModel item = new ItemViewStateModel("1","basketball", value);
        if(value) {
            isBasketball = false;
            lnBasketball.setBackground(getResources().getDrawable(R.drawable.bg_white_border_gray));
            txtBasketBall.setTextColor(getResources().getColor(R.color.colorBlack));
            for(int i = 0; i< sportsType.size(); i++) {
               if(sportsType.get(i).getId().equals("1")) {
                   sportsType.remove(i);
               }
            }
        }else {
            isBasketball = true;
            lnBasketball.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            txtBasketBall.setTextColor(getResources().getColor(R.color.colorWhite));
            sportsType.add(item);
        }
    }

    private void changeFootballColorState(boolean value) {
        ItemViewStateModel item = new ItemViewStateModel("2","football", value);
        if(value) {
            isFootball = false;
            lnFootball.setBackground(getResources().getDrawable(R.drawable.bg_white_border_gray));
            txtFootball.setTextColor(getResources().getColor(R.color.colorBlack));
             for(int i = 0; i< sportsType.size(); i++) {
                 if (sportsType.get(i).getId().equals("2")) {
                     sportsType.remove(i);
                 }
             }
        }else {
            isFootball = true;
            lnFootball.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            txtFootball.setTextColor(getResources().getColor(R.color.colorWhite));
            sportsType.add(item);
        }

    }

    private void changeCricketColorState(boolean value) {
        ItemViewStateModel item = new ItemViewStateModel("3","cricket", value);
        if(value) {
            isCricket = false;
            lnCricket.setBackground(getResources().getDrawable(R.drawable.bg_white_border_gray));
            txtCricket.setTextColor(getResources().getColor(R.color.colorBlack));
            for(int i = 0; i< sportsType.size(); i++) {
                if (sportsType.get(i).getId().equals("3")) {
                    sportsType.remove(i);
                }
            }
        }else {
            isCricket = true;
            lnCricket.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            txtCricket.setTextColor(getResources().getColor(R.color.colorWhite));
            sportsType.add(item);
        }

    }

    private void changeFitnessColorState(boolean value) {
        ItemViewStateModel item = new ItemViewStateModel("4","fitness", value);
        if(value) {
            isFitness = false;
            lnFitness.setBackground(getResources().getDrawable(R.drawable.bg_white_border_gray));
            txtFitness.setTextColor(getResources().getColor(R.color.colorBlack));
            for(int i = 0; i< sportsType.size(); i++) {
                if (sportsType.get(i).getId().equals("4")) {
                    sportsType.remove(i);
                }
            }
        }else {
            isFitness = true;
            lnFitness.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            txtFitness.setTextColor(getResources().getColor(R.color.colorWhite));
            sportsType.add(item);
        }

    }

    private void changeHockeyColorState(boolean value) {
        ItemViewStateModel item = new ItemViewStateModel("5","hockey", value);
        if(value) {
            isHockey = false;
            lnHockey.setBackground(getResources().getDrawable(R.drawable.bg_white_border_gray));
            txtHockey.setTextColor(getResources().getColor(R.color.colorBlack));
            for(int i = 0; i< sportsType.size(); i++) {
                if (sportsType.get(i).getId().equals("5")) {
                    sportsType.remove(i);
                }
            }
        }else {
            isHockey = true;
            lnHockey.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            txtHockey.setTextColor(getResources().getColor(R.color.colorWhite));
            sportsType.add(item);
        }

    }

    private void changeOtherColorState(boolean value) {
        ItemViewStateModel item = new ItemViewStateModel("6","other", value);
        if(value) {
            isOther = false;
            lnOther.setBackground(getResources().getDrawable(R.drawable.bg_white_border_gray));
            txtOther.setTextColor(getResources().getColor(R.color.colorBlack));
            for(int i = 0; i< sportsType.size(); i++) {
                if (sportsType.get(i).getId().equals("6")) {
                    sportsType.remove(i);
                }
            }
        }else {
            isOther = true;
            lnOther.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            txtOther.setTextColor(getResources().getColor(R.color.colorWhite));
            sportsType.add(item);
        }
    }

}