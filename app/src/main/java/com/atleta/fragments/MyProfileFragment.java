package com.atleta.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.atleta.R;
import com.atleta.activities.MainActivity;
import com.atleta.activities.WebViewActivity;
import com.atleta.customview.SpinnerView;
import com.atleta.models.Session;
import com.atleta.models.Upload;
import com.atleta.models.UserModel;
import com.atleta.utils.AppPreferences;
import com.atleta.utils.Keys;
import com.atleta.utils.AtletaApplication;
import com.atleta.utils.Utils;

import static android.app.Activity.RESULT_OK;


public class MyProfileFragment extends BaseFragment implements View.OnClickListener {
    private ImageView docResume;
    private String resumeUrl;
    private TextView logout;
    private ImageView userImageEditIcon, profileImage;
    private Uri imageuri;
    private String PathHolder;
    private Context context;
    private Upload uploadProfile;
    private LinearLayout lnrSkills;
    private MainActivity activity;
    private ProgressBar mProgressBar;
    private SpinnerView spinnerView;
    private TextView mTxtLinkedIn, mTxtEmailId, mTxtName, mTxtContact, mTxtExpectedSalary, mTxtPricePerHour, mTxtLocation, mTxtSkypeId, mTxtYearOfExperience, mTxtSkills;
    private ProgressDialog dialog;

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
        activity = (MainActivity) context;
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
            AtletaApplication.sharedDatabaseInstance().child("Users").child(session.getUserModel().getMobileNumber()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // this method is call to get the realtime
                    // updates in the data.
                    // this method is called when the data is
                    // changed in our Firebase console.
                    // below line is for getting the data from
                    // snapshot of our database.
                    Session value = snapshot.getValue(Session.class);

                    // after getting the value we are setting
                    // our value to our text view in below line.
                    AppPreferences.setSession(value);

                    initData();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // calling on cancelled method when we receive
                    // any error or we are not able to get the data.
                    Toast.makeText(getContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void findViewById(View view) {
        mProgressBar = view.findViewById(R.id.progress_bar);
        spinnerView = view.findViewById(R.id.spinnerView);
        userImageEditIcon = view.findViewById(R.id.img_edit);
        profileImage = view.findViewById(R.id.profile_image);
        mTxtName = view.findViewById(R.id.user_name);
        mTxtEmailId = view.findViewById(R.id.txt_email);
        mTxtLinkedIn = view.findViewById(R.id.txt_linkedIn);
        mTxtContact = view.findViewById(R.id.txt_mobile_number);
        mTxtExpectedSalary = view.findViewById(R.id.txt_expected_salary);
        mTxtPricePerHour = view.findViewById(R.id.txt_price_per_hour);
        mTxtLocation = view.findViewById(R.id.txt_location);
        mTxtSkypeId = view.findViewById(R.id.txt_skype_id);
        mTxtYearOfExperience = view.findViewById(R.id.txt_year_of_experience);
        lnrSkills = view.findViewById(R.id.lnr_skill);
        docResume = view.findViewById(R.id.img_resume);
    }

    private void registerListener() {
        docResume.setOnClickListener(this);
        userImageEditIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_resume:
                Intent intentWeb = new Intent(getContext(), WebViewActivity.class);
                intentWeb.putExtra("resume_url", resumeUrl);
                startActivity(intentWeb);
                break;
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
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                    final Session session = AppPreferences.getSession();
                    final UserModel userModel = session.getUserModel();
                    final String messagePushID = session.getUserModel().getMobileNumber() + "-profile";

                    dialog = new ProgressDialog(activity);
                    dialog.setMessage("Uploading");
                    dialog.setCancelable(false);

                    // this will show message uploading
                    // while pdf is uploading
                    dialog.show();
                    // Here we are uploading the pdf in firebase storage with the name of current time
                    final StorageReference filepath = storageReference.child(messagePushID + "." + "pdf");
                    PathHolder = filepath.getName();
                    filepath.putFile(imageuri).continueWithTask((Continuation) task -> {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return filepath.getDownloadUrl();
                    }).addOnCompleteListener((OnCompleteListener<Uri>) task -> {
                        if (task.isSuccessful()) {
                            final Uri uri = task.getResult();

                            uploadProfile = new Upload(filepath.getName(), uri.toString());
                            UserModel userUpload = new UserModel(userModel.getDisplayName(),userModel.getFirstName(), userModel.getMiddleName(), userModel.getLastName(),userModel.getMobileNumber(), userModel.getLocation(), userModel.getPreference(), userModel.getProfile_image_url());
                            session.setUserModel(userUpload);

                            activity.sendBroadcast(new Intent(Keys.BROADCAST_PROFILE_IMAGE));
                            AtletaApplication.sharedDatabaseInstance().child("Users").child(session.getUserModel().getMobileNumber()).setValue(session)
                                    .addOnSuccessListener(aVoid -> {


                                        /*Glide.with(AtletaApplication.sharedInstance())
                                                .load(uri.toString())
                                                .listener(new RequestListener<String, GlideDrawable>() {
                                                    @Override
                                                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean
                                                            isFirstResource) {
                                                        mProgressBar.setVisibility(View.INVISIBLE);
                                                        spinnerView.setVisibility(View.GONE);
                                                        return false;
                                                    }

                                                    @Override
                                                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable>
                                                            target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                        mProgressBar.setVisibility(View.INVISIBLE);
                                                        spinnerView.setVisibility(View.GONE);
                                                        return false;
                                                    }
                                                })
                                                .placeholder(R.drawable.ic_avatar)
                                                .dontAnimate()
                                                .into(profileImage);*/
                                        AppPreferences.setSession(session);
                                        dialog.dismiss();
                                        Utils.showToast(activity, activity.findViewById(R.id.fragment_container), "Uploaded Successfully");
                                    })
                                    .addOnFailureListener(e -> Utils.showToast(activity, activity.findViewById(R.id.fragment_container), "Uploading Failure"));

                        } else {
                            Utils.showToast(activity, activity.findViewById(R.id.fragment_container), "Upload Failed");
                        }
                    });
                }
                break;

        }
    }

    private void initData() {

        Session session = AppPreferences.getSession();
        mTxtName.setText(session.getUserModel().getFirstName() + " " + session.getUserModel().getLastName());
        mTxtContact.setText(session.getUserModel().getMobileNumber());
        mTxtLocation.setText(session.getUserModel().getLocation());


        mTxtEmailId.setText(session.getEmailId());
        /*spinnerView.setVisibility(View.GONE);*/
        if (session.getUserModel().getProfile_image_url() != null) {


            /*Glide.with(AtletaApplication.sharedInstance())
                    .load(session.getUserModel().getProfileImage().url)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean
                                isFirstResource) {
                            spinnerView.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable>
                                target, boolean isFromMemoryCache, boolean isFirstResource) {
                            spinnerView.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .placeholder(R.drawable.ic_avatar)
                    .dontAnimate()
                    .into(profileImage);*/
        }
        spinnerView.setVisibility(View.GONE);
    }

    private void setTags(String skills) {
        lnrSkills.removeAllViews();
        String[] strSkills = skills.split(",");
        for (String value : strSkills) {
            addTextSkills(value);
        }
    }

    private void addTextSkills(String value) {

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.skills_item, null);
        TextView skills = view.findViewById(R.id.txt_name);
        skills.setText(value.trim());
        lnrSkills.addView(skills);
    }
}
