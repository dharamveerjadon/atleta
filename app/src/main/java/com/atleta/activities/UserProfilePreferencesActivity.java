package com.atleta.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.atleta.R;
import com.atleta.customview.SpinnerView;
import com.atleta.models.Session;
import com.atleta.models.UserModel;
import com.atleta.utils.AppPreferences;
import com.atleta.utils.AtletaApplication;
import com.atleta.utils.Keys;
import com.atleta.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class UserProfilePreferencesActivity extends BaseActivity implements View.OnClickListener {
    private static final int DOCUMENT_CODE = 07;
    private static DatabaseReference mDatabase;
    private EditText mEdtDisplayName, mEdtFirstName, mEdtMiddleName, mEdtLastName, mEdtEmailId, mEdtContactNumber,
            mEdtLocation;
    private Button mBtnSubmit;
    private CardView mCardViewBasketBall, mCardViewFootball, mCardViewCricket, mCardViewFitness;
    private Uri imageuri;
    private ProgressDialog dialog;
    private SpinnerView spinnerView;
    private boolean isFirstTime;
    private String profileImage;
    private ProgressBar mProgressBar;
    private final ArrayList<String> preferences = new ArrayList<>();
    private ImageView userImageEditIcon, profileImageView;
    private boolean isBasketCheck, isFootballCheck, isCricketCheck, isFitnessCheck;
    private ImageView mImgBasketball, mImgCricket, mImgFootball, mImgFitness;
    private TextView mTxtBasketball, mTxtCricket, mTxtFootball, mTxtFitness;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile_preference);

        if (getIntent() != null) {
            isFirstTime = getIntent().getBooleanExtra("isFirst", false);
        }


        mDatabase = FirebaseDatabase.getInstance().getReference();
        findViewId();
        registerListener();
        getdata();
    }

    private void findViewId() {
        mProgressBar = findViewById(R.id.progress_bar);
        profileImageView = findViewById(R.id.profile_image);
        mEdtDisplayName = findViewById(R.id.edt_display_name);
        mEdtFirstName = findViewById(R.id.edt_first_name);
        mEdtMiddleName = findViewById(R.id.edt_middle_name);
        mEdtLastName = findViewById(R.id.edt_last_name);
        mEdtEmailId = findViewById(R.id.edt_email_id);
        mEdtContactNumber = findViewById(R.id.edt_contact_number);
        mEdtLocation = findViewById(R.id.edt_location);
        mBtnSubmit = findViewById(R.id.btn_submit);
        spinnerView = findViewById(R.id.spinnerView);
        userImageEditIcon = findViewById(R.id.img_edit);
        mCardViewBasketBall = findViewById(R.id.cardview_basketball);
        mCardViewFootball = findViewById(R.id.cardview_football);
        mCardViewCricket = findViewById(R.id.cardview_cricket);
        mCardViewFitness = findViewById(R.id.cardview_fitness);

        mImgBasketball = findViewById(R.id.img_basketball);
        mImgFootball = findViewById(R.id.img_football);
        mImgCricket = findViewById(R.id.img_cricket);
        mImgFitness = findViewById(R.id.img_fitness);

        mTxtBasketball = findViewById(R.id.txt_basketball);
        mTxtFootball = findViewById(R.id.txt_football);
        mTxtCricket = findViewById(R.id.txt_cricket);
        mTxtFitness = findViewById(R.id.txt_fitness);
    }

    private void registerListener() {
        mBtnSubmit.setOnClickListener(this);
        userImageEditIcon.setOnClickListener(this);
        mCardViewFitness.setOnClickListener(this);
        mCardViewBasketBall.setOnClickListener(this);
        mCardViewFootball.setOnClickListener(this);
        mCardViewCricket.setOnClickListener(this);
    }

    private void getdata() {
        spinnerView.setVisibility(View.VISIBLE);
        // calling add value event listener method
        // for getting the values frm database.

        Session session = AppPreferences.getSession();
        if (session != null) {
            mDatabase.child("Users").child(session.getUserId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Session value = snapshot.getValue(Session.class);
                    AppPreferences.setSession(value);

                    initData();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // calling on cancelled method when we receive
                    // any error or we are not able to get the data.
                    spinnerView.setVisibility(View.GONE);
                    Toast.makeText(UserProfilePreferencesActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void pressBasketPreference() {
        if (isBasketCheck) {
            isBasketCheck = false;
            mCardViewBasketBall.setCardBackgroundColor(getResources().getColor(R.color.colorWhite));
            mImgBasketball.setImageResource(R.drawable.ic_basketball_black);
            mTxtBasketball.setTextColor(getResources().getColor(R.color.colorBlack));

            preferences.remove("basketball");
        } else {
            isBasketCheck = true;
            mCardViewBasketBall.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
            mImgBasketball.setImageResource(R.drawable.ic_basketball_white);
            mTxtBasketball.setTextColor(getResources().getColor(R.color.colorWhite));
            preferences.add("basketball");
        }

    }

    private void pressFootballPreference() {
        if (isFootballCheck) {
            isFootballCheck = false;
            mCardViewFootball.setCardBackgroundColor(getResources().getColor(R.color.colorWhite));
            mImgFootball.setImageResource(R.drawable.ic_basketball_black);
            mTxtFootball.setTextColor(getResources().getColor(R.color.colorBlack));
            preferences.remove("football");
        } else {
            isFootballCheck = true;
            mCardViewFootball.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
            mImgFootball.setImageResource(R.drawable.ic_basketball_white);
            mTxtFootball.setTextColor(getResources().getColor(R.color.colorWhite));
            preferences.add("football");
        }

    }

    private void pressCricketPreference() {
        if (isCricketCheck) {
            isCricketCheck = false;
            mCardViewCricket.setCardBackgroundColor(getResources().getColor(R.color.colorWhite));
            mImgCricket.setImageResource(R.drawable.ic_basketball_black);
            mTxtCricket.setTextColor(getResources().getColor(R.color.colorBlack));
            preferences.remove("cricket");
        } else {
            isCricketCheck = true;
            mCardViewCricket.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
            mImgCricket.setImageResource(R.drawable.ic_basketball_white);
            mTxtCricket.setTextColor(getResources().getColor(R.color.colorWhite));
            preferences.add("cricket");
        }

    }

    private void pressFitnessPreference() {
        if (isFitnessCheck) {
            isFitnessCheck = false;
            mCardViewFitness.setCardBackgroundColor(getResources().getColor(R.color.colorWhite));
            mImgFitness.setImageResource(R.drawable.ic_basketball_black);
            mTxtFitness.setTextColor(getResources().getColor(R.color.colorBlack));
            preferences.remove("badminton");
        } else {
            isFitnessCheck = true;
            mCardViewFitness.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
            mImgFitness.setImageResource(R.drawable.ic_basketball_white);
            mTxtFitness.setTextColor(getResources().getColor(R.color.colorWhite));
            preferences.add("badminton");
        }

    }

    private boolean isValidation() {
        if (TextUtils.isEmpty(mEdtDisplayName.getText().toString().trim())) {
            mEdtDisplayName.setError("Enter display name here..");
            mEdtDisplayName.requestFocus();
            return false;
        } else {
            mEdtDisplayName.setError(null);
        }

        if (TextUtils.isEmpty(mEdtFirstName.getText().toString().trim())) {
            mEdtFirstName.setError("Enter first name here..");
            mEdtFirstName.requestFocus();
            return false;
        } else {
            mEdtFirstName.setError(null);
        }
        if (TextUtils.isEmpty(mEdtLastName.getText().toString().trim())) {
            mEdtLastName.setError("Enter last name here..");
            mEdtLastName.requestFocus();
            return false;
        } else {
            mEdtLastName.setError(null);
        }

        if (TextUtils.isEmpty(mEdtEmailId.getText().toString().trim())) {
            mEdtEmailId.setError("Enter email id here..");
            mEdtEmailId.requestFocus();
            return false;
        } else {
            mEdtEmailId.setError(null);
        }

        if (TextUtils.isEmpty(mEdtContactNumber.getText().toString().trim())) {
            mEdtContactNumber.setError("Enter contact number here..");
            mEdtContactNumber.requestFocus();
            return false;
        } else {
            mEdtContactNumber.setError(null);
        }

        if (TextUtils.isEmpty(mEdtLocation.getText().toString().trim())) {
            mEdtLocation.setError("Enter location here..");
            mEdtLocation.requestFocus();
            return false;
        } else {
            mEdtLocation.setError(null);
        }

        if (TextUtils.isEmpty(profileImage)) {
            Utils.showToast(UserProfilePreferencesActivity.this, findViewById(R.id.fragment_container), "Please upload resume..");
            return false;
        }

        return true;
    }

    private void initData() {
        spinnerView.setVisibility(View.GONE);
        Session session = AppPreferences.getSession();
        if (session != null) {
            mEdtDisplayName.setText(session.getDisplayName());
            mEdtEmailId.setText(session.getEmailId());
        }

        if (session != null && session.getUserModel() != null) {
            mEdtFirstName.setText(session.getUserModel().getFirstName());
            mEdtMiddleName.setText(session.getUserModel().getMiddleName());
            mEdtLastName.setText(session.getUserModel().getLastName());
            mEdtLocation.setText(session.getUserModel().getLocation());
            profileImage = session.getUserModel().getProfile_image_url();
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                if (isValidation())
                    saveDataToFirebase();
                break;

            case R.id.img_edit:
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
                break;
            case R.id.cardview_fitness:
                pressFitnessPreference();
                break;
            case R.id.cardview_basketball:
                pressBasketPreference();
                break;
            case R.id.cardview_football:
                pressFootballPreference();
                break;
            case R.id.cardview_cricket:
                pressCricketPreference();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    spinnerView.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.VISIBLE);
                    imageuri = data.getData();
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                    final String messagePushID = AppPreferences.getSession().getUserId() + "-profile";

                    dialog = new ProgressDialog(this);
                    dialog.setMessage("Uploading");
                    dialog.setCancelable(false);

                    // this will show message uploading
                    // while pdf is uploading
                    dialog.show();
                    // Here we are uploading the pdf in firebase storage with the name of current time
                    final StorageReference filepath = storageReference.child(messagePushID + "." + "pdf");
                    filepath.putFile(imageuri).continueWithTask((Continuation) task -> {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return filepath.getDownloadUrl();
                    }).addOnCompleteListener((OnCompleteListener<Uri>) task -> {
                        if (task.isSuccessful()) {
                            final Uri uri = task.getResult();

                            profileImage = uri.toString();

                            this.sendBroadcast(new Intent(Keys.BROADCAST_PROFILE_IMAGE));

                                        Glide.with(AtletaApplication.sharedInstance())
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
                                                .into(profileImageView);
                                        dialog.dismiss();
                                        Utils.showToast(this, findViewById(R.id.fragment_container), "Uploaded Successfully");

                        } else {
                            Utils.showToast(this, findViewById(R.id.fragment_container), "Upload Failed");
                        }
                    });
                }
                break;
        }
    }

    private void saveDataToFirebase() {

        spinnerView.setVisibility(View.VISIBLE);
        String displayname = mEdtDisplayName.getText().toString().trim();
        String firstName = mEdtFirstName.getText().toString().trim();
        String middleName = mEdtMiddleName.getText().toString().trim();
        String LastName = mEdtLastName.getText().toString().trim();
        String emailId = mEdtEmailId.getText().toString().trim();
        final String contactNumber = mEdtContactNumber.getText().toString().trim();
        String location = mEdtLocation.getText().toString().trim();
        final Session session = AppPreferences.getSession();
        UserModel userModel = new UserModel(displayname, firstName, middleName, LastName, contactNumber, location, preferences, profileImage);
        session.setUserModel(userModel);


        mDatabase.child("Users").child(session.getUserId()).setValue(session)
                .addOnSuccessListener(aVoid -> {
                    AppPreferences.setSession(session);
                    spinnerView.setVisibility(View.GONE);
                    startActivity(new Intent(UserProfilePreferencesActivity.this, MainActivity.class));
                    finish();
                })
                .addOnFailureListener(e ->
                        spinnerView.setVisibility(View.GONE));
    }

    @Override
    protected void syncActionBarArrowState() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}