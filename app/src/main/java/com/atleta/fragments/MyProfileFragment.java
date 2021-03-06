package com.atleta.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.tabs.TabLayout;
import com.atleta.R;
import com.atleta.activities.HomeActivity;
import com.atleta.customview.SpinnerView;
import com.atleta.models.Session;
import com.atleta.utils.AppPreferences;
import com.atleta.utils.Keys;
import com.atleta.utils.AtletaApplication;
import com.atleta.utils.Utils;

import org.jetbrains.annotations.NotNull;

import static android.app.Activity.RESULT_OK;


public class MyProfileFragment extends BaseFragment implements View.OnClickListener {
    private ImageView docResume;
    private String resumeUrl;
    private TextView logout;
    private ImageView userImageEditIcon, profileImageView;
    private Uri imageuri;
    private String profileImage;
    private HomeActivity activity;
    private ProgressBar mProgressBar;
    private SpinnerView spinnerView;
    private TextView  mTxtName;
    private ProgressDialog dialog;
    private ViewPager mViewPager;
    public static MyProfileFragment newInstance(String title) {
        MyProfileFragment fragment = new MyProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Set title
        setToolbarTitle(getTitle());
        View rootView = inflater.inflate(R.layout.fragment_my_profile, container, false);
        findViewById(rootView);
        registerListener();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getdata();
    }

    private void getdata() {
        spinnerView.setVisibility(View.VISIBLE);
        // calling add value event listener method
        // for getting the values from database.
        Session session = AppPreferences.getSession();
        if (session != null) {
        }

    }

    private void findViewById(View view) {
        mProgressBar = view.findViewById(R.id.progress_bar);
        spinnerView = view.findViewById(R.id.spinnerView);
        userImageEditIcon = view.findViewById(R.id.img_edit);
        profileImageView = view.findViewById(R.id.profile_image);
        mTxtName = view.findViewById(R.id.user_name);

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);

        mViewPager = view.findViewById(R.id.pager);
        mViewPager.setAdapter(new Pager(getChildFragmentManager()));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(mViewPager);

    }

    private void registerListener() {
        userImageEditIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_edit:
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        switch (requestCode) {
            case 1:

                if (resultCode == RESULT_OK) {
                    spinnerView.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.VISIBLE);
                    imageuri = data.getData();

                    final Session session = AppPreferences.getSession();
                    final String messagePushID = session.getUserId() + "-profile";

                    dialog = new ProgressDialog(activity);
                    dialog.setMessage("Uploading");
                    dialog.setCancelable(false);
                            activity.sendBroadcast(new Intent(Keys.BROADCAST_PROFILE_IMAGE));
                           /* Glide.with(AtletaApplication.sharedInstance())
                                    .load(uri.toString())
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            mProgressBar.setVisibility(View.INVISIBLE);
                                            spinnerView.setVisibility(View.GONE);
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                            mProgressBar.setVisibility(View.INVISIBLE);
                                            spinnerView.setVisibility(View.GONE);
                                            return false;
                                        }
                                    })
                                    .placeholder(R.drawable.ic_avatar)
                                    .dontAnimate()
                                    .into(profileImageView);*/
                        } else {
                            Utils.showToast(activity, activity.findViewById(R.id.fragment_container), "Upload Failed");
                }
                break;

        }
    }

    private void initData() {

        Session session = AppPreferences.getSession();
        mTxtName.setText(session.getName());

        spinnerView.setVisibility(View.GONE);
    }
    private class Pager extends FragmentStatePagerAdapter {

        private Pager(FragmentManager fm) {
            super(fm);
        }

        //Overriding method getItem
        @NotNull
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            if (position ==  0)
                fragment = FeedPostFragment.newInstance("Post");

            if(position == 1)
                fragment = FeedPostFragment.newInstance("Live Session");

            if(position == 2)
                fragment = FeedPostFragment.newInstance("Challenges");


            return fragment;
        }

        //Overridden method getCount to get the number of tabs
        @Override
        public int getCount() {

            return AppPreferences.getSession().isAdmin() ? 4 : 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = null;
            if (position == 0)
                title = "Post";

            if (position == 1)
                title = "Live Session";

            if (position == 2)
                title = "Challenges";

            if (position == 3)
                title = "Admin";

            return title;
        }
    }
}
