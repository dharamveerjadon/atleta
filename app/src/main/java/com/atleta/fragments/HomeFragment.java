package com.atleta.fragments;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.atleta.R;
import com.atleta.activities.CreateCommunityActivity;
import com.atleta.models.Image;
import com.atleta.models.Session;
import com.atleta.utils.AppPreferences;
import com.atleta.utils.Constants;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends BaseFragment implements View.OnClickListener {
    public String selectFileType = Constants.FILE_TYPE_IMAGE;
    private final int mSelectedSubIndex = 0;
    private ViewPager mViewPager;
    private Session session;
    private com.nambimobile.widgets.efab.FabOption mfeedOption, mChallengeOption, mCommunityOption;

    public static HomeFragment newInstance(String title) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,

                             Bundle savedInstanceState) {
        // Set title
        setToolbarTitle(getTitle());
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        session = AppPreferences.getSession();
        viewById(view);
        registerListener();

        return view;
    }

    private void viewById(View view) {
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);

        mViewPager = view.findViewById(R.id.pager);
        mfeedOption = view.findViewById(R.id.feedoption);
        mChallengeOption = view.findViewById(R.id.challenge_otion);
        mCommunityOption = view.findViewById(R.id.community_option);
        mViewPager.setAdapter(new Pager(getChildFragmentManager()));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void registerListener() {
        mfeedOption.setOnClickListener(this);
        mChallengeOption.setOnClickListener(this);
        mCommunityOption.setOnClickListener(this);
    }

    /**
     * To pick image from gallery
     */
    public void pickImagesFromGallery() {

        FilePickerBuilder.getInstance().setMaxCount(5)
                .setSelectedFiles(new ArrayList<String>())
                .setActivityTheme(R.style.LibAppTheme)
                .enableCameraSupport(false)
                .pickPhoto(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO) {
            List<String> photoPaths = new ArrayList<>();
            photoPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
            new HandleImagePicked(this, getActivity()).execute(photoPaths);
        }
    }


    private void getPickedImages(List<Image> images) {
        onPickedImages(images);
    }

    /**
     * To upload selected images on server
     *
     * @param images
     */
    private void onPickedImages(final List<Image> images) {
        if (images == null && images.size() == 0) return;

        //performToUploadImageOnServer(images);

    }

    /**
     * get selected item in view pager
     *
     * @return
     */
    public int getCurrentItem() {
        return mViewPager.getCurrentItem();
    }

    /**
     * Set current item in view pager
     *
     * @param item index
     */
    public void setCurrentItem(int item) {
        mViewPager.setCurrentItem(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.feedoption:
                final String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), permissions, Constants.GALLERY_REQUEST);
                } else {
                    pickImagesFromGallery();
                }

                break;
            case R.id.community_option:
                startActivity(new Intent(getActivity().getApplicationContext(), CreateCommunityActivity.class));
                    break;

            case R.id.challenge_otion:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AppPreferences.IsCongratulationDone()) {
            AppPreferences.setIsCongratulationDone(AppPreferences.IsCONGRATULATIONACTIONDONE, false);
            mViewPager.postDelayed(() -> mViewPager.setCurrentItem(1), 100);
        }

    }

    public class HandleImagePicked extends AsyncTask<List<String>, Void, List<Image>> {

        ProgressDialog progressDialog;
        HomeFragment listener;

        HandleImagePicked(HomeFragment listener, Activity activity) {
            this.listener = listener;
            progressDialog = new ProgressDialog(activity);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.string_progress_msg));
            progressDialog.show();
        }

        @SafeVarargs
        @Override
        protected final List<Image> doInBackground(List<String>... lists) {

            List<Image> images = new ArrayList<>();
            List<String> passedList = lists[0];

            for (int i = 0; i < passedList.size(); i++) {
                String imagePath = passedList.get(i);
                if (TextUtils.isEmpty(imagePath)) {
                    progressDialog.dismiss();
                    return new ArrayList<Image>();
                }
                if (imagePath != null && imagePath.length() > 0) {
                    Image image = new Image(0, "image_" + i, imagePath, false);
                    images.add(image);
                }
            }

            return images;
        }

        @Override
        protected void onPostExecute(List<Image> images) {
            super.onPostExecute(images);
            listener.getPickedImages(images);
            this.progressDialog.dismiss();
        }
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
            if (position == 0)
                fragment = FeedPostFragment.newInstance("Feeds");

            if (position == 1)
                fragment = CommunityFragment.newInstance("Community");

            if (position == 2)
                fragment = FeedPostFragment.newInstance("Challenges");

            if (position == 3)
                fragment = FeedPostFragment.newInstance("Trending");


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
                title = "Feeds";

            if (position == 1)
                title = "Community";

            if (position == 2)
                title = "Challenges";

            if (position == 3)
                title = "Trending";

            return title;
        }
    }
}
