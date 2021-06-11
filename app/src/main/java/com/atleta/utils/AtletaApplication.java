package com.atleta.utils;

import android.app.Application;

import com.atleta.api.AtletaApiClient;
import com.atleta.api.AtletaApiServices;
import com.atleta.api.HttpsTrustManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AtletaApplication extends Application {

    /**
     * The default socket timeout in milliseconds
     */
    private static final int DEFAULT_TIMEOUT_MS = 10000;

    private final String TAG = AtletaApplication.class.getSimpleName();

    private static AtletaApplication mSharedInstance;
    private static DatabaseReference mDatabase;
    private static AtletaApiServices atletaApiServices;


    public static AtletaApplication sharedInstance() {
        return mSharedInstance;
    }
    public static DatabaseReference sharedDatabaseInstance() {
        return mDatabase;
    }

    public static AtletaApiServices sharedAtletaApiServicesInstance() {
        return atletaApiServices;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSharedInstance = this;
        AppPreferences.init(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        atletaApiServices = AtletaApiClient.getClient(this)
                .create(AtletaApiServices.class);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }
}
