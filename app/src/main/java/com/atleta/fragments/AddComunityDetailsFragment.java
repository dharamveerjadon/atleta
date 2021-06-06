package com.atleta.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.atleta.R;
import com.atleta.activities.HomeActivity;
import com.atleta.customview.SpinnerView;
import com.atleta.models.AddCommunityModel;
import com.atleta.models.Image;
import com.atleta.models.Session;
import com.atleta.models.UserModel;
import com.atleta.utils.AppPreferences;
import com.atleta.utils.AtletaApplication;
import com.atleta.utils.Constants;
import com.atleta.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddComunityDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddComunityDetailsFragment extends BaseFragment implements View.OnClickListener {

    private Button mBtnNext;
    private EditText mEdtCommunityName, mEdtAboutCommunity;
    private Spinner spinnerTopic, spinnerScope;
    private RadioGroup mRadioGroupVisibility;
    private RadioButton mRadioPublic, mRadioInvite;
    private TextView mTxtRadioVisibilityUpdate;
    private String mCommunityVisibility = "public";
    private String mSpinnerTopic;
    private String mSpinnerScope;
    private ImageView imgCommunityProfile, imgEditCommunityProfile;
    private ImageView imgCommunityCover, imgEditCommunityCover;
    private String coverImageUrl, profileImageUrl;
    private String imageType;
    private ProgressDialog dialog;
    private SpinnerView spinnerView;
    private ProgressBar mProgressProfile;

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
        imgCommunityProfile = view.findViewById(R.id.profile_image);
        imgEditCommunityProfile = view.findViewById(R.id.img_edit);
        imgCommunityCover = view.findViewById(R.id.image_cover);
        imgEditCommunityCover = view.findViewById(R.id.img_update_header);
        spinnerTopic = view.findViewById(R.id.spinner_topic);
        spinnerScope = view.findViewById(R.id.spinner_scope);
        mRadioGroupVisibility = view.findViewById(R.id.radio_visibility);
        mRadioPublic = view.findViewById(R.id.radio_public);
        mRadioInvite = view.findViewById(R.id.radio_invite);
        mTxtRadioVisibilityUpdate = view.findViewById(R.id.radio_txt_update);
        spinnerView = view.findViewById(R.id.spinnerView);
        mProgressProfile = view.findViewById(R.id.progress_bar);
        mBtnNext = view.findViewById(R.id.btn_next);
    }

    private void registerListener() {
        mBtnNext.setOnClickListener(this);
        imgEditCommunityProfile.setOnClickListener(this);
        imgEditCommunityCover.setOnClickListener(this);

        mRadioGroupVisibility.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == R.id.radio_public) {
                mTxtRadioVisibilityUpdate.setText("Public");
                mCommunityVisibility = "public";
            } else {
                mTxtRadioVisibilityUpdate.setText("Invite");
                mCommunityVisibility = "invite";
            }
        });

        String[] topicList = getResources().getStringArray(R.array.community_topic);
        ArrayAdapter<String> optionSpinner = new ArrayAdapter<String>(getActivity(), R.layout.item_spinner, topicList);
        optionSpinner.setDropDownViewResource(R.layout.item_spinner);
        spinnerTopic.setAdapter(optionSpinner);

        spinnerTopic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position > 0)
                mSpinnerTopic = topicList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        String[] scopeList = getResources().getStringArray(R.array.community_scope);
        ArrayAdapter<String> optionSpinnerScope = new ArrayAdapter<String>(getActivity(), R.layout.item_spinner, scopeList);
        optionSpinner.setDropDownViewResource(R.layout.item_spinner);
        spinnerScope.setAdapter(optionSpinnerScope);

        spinnerScope.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                mSpinnerScope = scopeList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if (isValidation()) {
                    sendDataToFirebase();
                }
                break;
            case R.id.img_edit:

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.GALLERY_REQUEST);
                } else {
                    pickImagesFromGallery("profile");
                }

                break;

            case R.id.img_update_header:
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.GALLERY_REQUEST);
                } else {
                    pickImagesFromGallery("cover");
                }

                break;
        }
    }


    /**
     * To pick image from gallery
     */
    public void pickImagesFromGallery(String type) {
        imageType = type;
        FilePickerBuilder.getInstance().setMaxCount(1)
                .setSelectedFiles(new ArrayList<String>())
                .setActivityTheme(R.style.LibAppTheme)
                .enableCameraSupport(false)
                .pickPhoto(this);
    }


    @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent data) {
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

        spinnerView.setVisibility(View.VISIBLE);
        mProgressProfile.setVisibility(View.VISIBLE);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        final Session session = AppPreferences.getSession();
        String messagePushID = null;
        if ("profile".equals(imageType)) {
            messagePushID = session.getUserId() + "-profile" + System.currentTimeMillis() / 1000;
        } else {
            messagePushID = session.getUserId() + "-cover" + System.currentTimeMillis() / 1000;
        }


        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Uploading");
        dialog.setCancelable(false);

        // this will show message uploading
        // while pdf is uploading
        /*dialog.show();*/
        // Here we are uploading the pdf in firebase storage with the name of current time
        final StorageReference filepath = storageReference.child(messagePushID + "." + "pdf");
        Uri uriPath = Uri.fromFile(new File(images.get(0).getPath()));
        filepath.putFile(uriPath).continueWithTask((Continuation) task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return filepath.getDownloadUrl();
        }).addOnCompleteListener((OnCompleteListener<Uri>) task -> {
            if (task.isSuccessful()) {

                final Uri uri = task.getResult();

                if ("profile".equals(imageType)) {
                    profileImageUrl = uri.toString();
                    Glide.with(AtletaApplication.sharedInstance())
                            .load(uri.toString())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    mProgressProfile.setVisibility(View.INVISIBLE);
                                    spinnerView.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    mProgressProfile.setVisibility(View.INVISIBLE);
                                    spinnerView.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .placeholder(R.drawable.ic_avatar)
                            .dontAnimate()
                            .into(imgCommunityProfile);
                } else {
                    coverImageUrl = uri.toString();
                    Glide.with(AtletaApplication.sharedInstance())
                            .load(uri.toString())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    mProgressProfile.setVisibility(View.INVISIBLE);
                                    spinnerView.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    mProgressProfile.setVisibility(View.INVISIBLE);
                                    spinnerView.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .placeholder(R.drawable.ic_avatar)
                            .dontAnimate()
                            .into(imgCommunityCover);
                }

            } else {
                mProgressProfile.setVisibility(View.INVISIBLE);
                spinnerView.setVisibility(View.GONE);
                Utils.showToast(getActivity(), getActivity().findViewById(R.id.fragment_container), "Upload Failed");
            }
        });

    }

    private boolean isValidation() {

        if (TextUtils.isEmpty(mEdtCommunityName.getText().toString().trim())) {
            mEdtCommunityName.setError("Enter community name here..");
            mEdtCommunityName.requestFocus();
            return false;
        } else {
            mEdtCommunityName.setError(null);
        }

        if (TextUtils.isEmpty(mSpinnerTopic) || "Select topic".equals(mSpinnerTopic)) {
            Utils.showToast(getActivity(), getActivity().findViewById(R.id.main_layout), "Please select category");
            return false;
        }

        if (TextUtils.isEmpty(mEdtAboutCommunity.getText().toString().trim())) {
            mEdtAboutCommunity.setError("Enter about community here..");
            mEdtAboutCommunity.requestFocus();
            return false;
        } else {
            mEdtAboutCommunity.setError(null);
        }

        return true;
    }

    private void sendDataToFirebase() {

        DatabaseReference addCommunity = AtletaApplication.sharedDatabaseInstance().child("Community");
        if(addCommunity != null) {
            final String key = addCommunity.push().getKey();
            final Session session = AppPreferences.getSession();

            AddCommunityModel addCommunityModel = new AddCommunityModel(key, mEdtCommunityName.getText().toString().trim(), mSpinnerTopic, mEdtAboutCommunity.getText().toString().trim(), mCommunityVisibility, mSpinnerScope, profileImageUrl, coverImageUrl, session.getUserId() );
            addCommunity.child(key).setValue(addCommunityModel)
                    .addOnSuccessListener(aVoid -> {
                        spinnerView.setVisibility(View.GONE);
                        pushFragment(InvitationFragment.newInstance("Invitation"), true);

                    })
                    .addOnFailureListener(e ->
                            spinnerView.setVisibility(View.GONE)
                    );
        }
    }

    public class HandleImagePicked extends AsyncTask<List<String>, Void, List<Image>> {

        ProgressDialog progressDialog;
        AddComunityDetailsFragment listener;

        HandleImagePicked(AddComunityDetailsFragment listener, Activity activity) {
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
}