package com.atleta.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import com.atleta.R;
import com.atleta.customview.SpinnerView;
import com.atleta.utils.Constants;
import com.atleta.utils.Utils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.Scope;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.CancellationException;

import javax.net.ssl.HttpsURLConnection;

import static com.atleta.activities.HomeActivity.mGoogleApiClient;

/*
import static com.truckland.android.activities.ApplicationClass.twitterClient;
*/

/**
 * Created by dharamveer on 11/06/2021.
 */

public class SocialLoginController implements View.OnClickListener {

    private static final String host = "api.linkedin.com";
    private static final String topCardUrl = "https://" + host + "/v1/people/~:(email-address,formatted-name,phone-numbers,public-profile-url,picture-url,picture-urls::(original))";
    private final String TAG = "Social Login";
    private final Context ctx;
    private final Activity activity;
    private final SpinnerView spinnerView;
    public CallbackManager callbackManager;
    public String socialType = "";
    private Fragment fragment;
    private String fragmentName;
    private RadioGroup radioGroup;
    private RadioButton rbtnTwitter;
    private RadioButton rbtnFb;
    private RadioButton rbtnGPLus;
    private RadioButton rbtnLinkedin;
    private LoginManager fbLoginManager;
    /*private TwitterSession session;*/
    private JsonObject responseJson = new JsonObject();
    private String linkedInUserFirstName;
    private String linkedInUserLastName;
    private String linkedInUserProfile;
    private String linkedInUserEmailAddress;

    public SocialLoginController(Activity activity, View view, SpinnerView spinnerView, Fragment fragment, String fragmentName) {
        this.ctx = activity.getApplicationContext();
        this.activity = activity;
        this.fragment = fragment;
        this.fragmentName = fragmentName;
        this.spinnerView = spinnerView;

        // To find views on page by id
        findViews(view);
        // To register the listener on views as required
        registerListener();

        initController();

        // Add code to print out the key hash
        try {
            PackageInfo info = activity.getPackageManager().getPackageInfo(
                    "com.atleta",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }

    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }

    /**
     * Find the view ids.
     */
    private void findViews(View view) {
        radioGroup = view.findViewById(R.id.rg_login);
        rbtnFb = view.findViewById(R.id.rbtn_fb);
        rbtnGPLus = view.findViewById(R.id.rbtn_gplus);
    }

    /**
     * Register the views.
     */
    private void registerListener() {
        rbtnFb.setOnClickListener(this);
        rbtnGPLus.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.rbtn_fb:
                if (!Utils.isNetworkAvailable(ctx)) {
                    Utils.showToast(ctx, activity.findViewById(R.id.fragment_container), ctx.getString(R.string.error_api_no_internet_connection));
                    rbtnFb.setChecked(false);
                    return;
                }
                loginByFacebook();
                break;
            case R.id.rbtn_gplus:
                if (!Utils.isNetworkAvailable(ctx)) {
                    Utils.showToast(ctx, activity.findViewById(R.id.fragment_container), ctx.getString(R.string.error_api_no_internet_connection));
                    rbtnGPLus.setChecked(false);
                    return;
                }
                loginByGooglePlus();
                break;


        }
    }

    private void initController() {

        // If already logined by facebook
        logoutToFacebook();

        // Initialization of Facebook
        fbInitalization();
    }

    public static void logoutToFacebook() {
        if (LoginManager.getInstance() != null)
            LoginManager.getInstance().logOut();

    }


    /**
     * Method is call if user click on g+ button.
     */
    private void loginByGooglePlus() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        activity.startActivityForResult(signInIntent, Constants.GOOGLE_CODE);
    }

    /**
     * Handle the google plus result
     *
     * @param result
     */
    public void handleGooglePlusResult(GoogleSignInResult result, Fragment fragment, String fragmentName) {
        this.fragment = fragment;
        this.fragmentName = fragmentName;

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
//            Log.e(TAG, "display name: " + acct.getDisplayName());
//
            String fullName = null;
            String givenName = null;
            String familyName = null;
            String imageUrl = null;
            String email = null;


            if (acct != null) {
                if (acct.getDisplayName() != null)
                    fullName = acct.getDisplayName();
                if (acct.getGivenName() != null)
                    givenName = acct.getGivenName();
                if (acct.getFamilyName() != null)
                    familyName = acct.getFamilyName();
                if (acct.getEmail() != null)
                    email = acct.getEmail();
                if (acct.getPhotoUrl() != null)
                    imageUrl = acct.getPhotoUrl().toString();

            }

            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("FullName", fullName);
            jsonObject.addProperty("GivenName", givenName);
            jsonObject.addProperty("FamilyName", familyName);
            jsonObject.addProperty("Email", email);
            jsonObject.addProperty("ImageUrl", imageUrl);


            responseJson = jsonObject;



        } else {

        }
    }

    //=========================== FacebookLogin =========================================
    private void loginByFacebook() {
        fbLoginManager.logInWithReadPermissions(activity, Arrays.asList("email", "public_profile", "user_birthday"));
    }

    /**
     * Initalization the fb login manager
     */
    private void fbInitalization() {

        fbLoginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();

        fbLoginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Test", "onSuccess: ");
                if (AccessToken.getCurrentAccessToken() != null) {
                    requestFbData();
                }
            }

            @Override
            public void onCancel() {
                Utils.showToast(ctx, activity.findViewById(R.id.fragment_container), ctx.getString(R.string.string_social_error));
            }

            @Override
            public void onError(FacebookException e) {
//                Log.d("Test", "onError ");
                Utils.showToast(ctx, activity.findViewById(R.id.fragment_container), ctx.getString(R.string.string_social_error));
            }
        });
    }

    /**
     * Gettinh the fb data
     */
    private void requestFbData() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), (object, response) -> {

            JSONObject facebookResponse = response.getJSONObject();
            try {
                if (facebookResponse != null) {
                    if (facebookResponse.getString("email").isEmpty()) {
                       /* Utils.openDialog(ctx, ctx.getString(R.string.app_name), ctx.getString(R.string.fb_email_alert),
                                ctx.getString(R.string.string_ok), "", "1", new DialogResponse());*/
                    } else {
                        // hit the facebook login Api
                        Log.d("Test", "facebook   " + facebookResponse.getString("name"));
//                            MtProgressDialog.getInstance(ctx).progressDialog.show();

                        String firstName = "", lastName = "", email = "", id = "";
                        String[] name = null;

                        try {
                            name = facebookResponse.getString("name").split(" ");
                            email = facebookResponse.getString("email");
                            id = facebookResponse.getString("id");

                            if (name.length > 1) {
                                firstName = name[0];
                                lastName = name[1];
                            } else if (name.length == 1) {
                                firstName = name[0];
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JsonObject jsonObject = new JsonObject();

                        jsonObject.addProperty("First_name", firstName);
                        jsonObject.addProperty("last_name", lastName);
                        jsonObject.addProperty("email", email);
                        jsonObject.addProperty("id", id);
                        jsonObject.addProperty("pictureUrl", "http://graph.facebook.com/" + id + "/picture?type=large");

                        responseJson = jsonObject;

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }


    /**
     * After login by the socail registration then call the api.
     *
     * @param url
     */
    private void performSocialRegistration(String url) {

    }


}
