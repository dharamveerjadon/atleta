package com.atleta.utils;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


public class AtletaApplication extends Application {

    /**
     * The default socket timeout in milliseconds
     */
    private static final int DEFAULT_TIMEOUT_MS = 10000;

    private final String TAG = AtletaApplication.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private static AtletaApplication mSharedInstance;

    public static AtletaApplication sharedInstance() {
        return mSharedInstance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mSharedInstance = this;
        AppPreferences.init(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    /**
     * Request Queue is provided to add new request
     *
     * @return Request Queue
     */
    public RequestQueue getRequestQueue() {

        if (mRequestQueue == null) {

            synchronized (this) {

                if (mRequestQueue == null) {

                    mRequestQueue = Volley.newRequestQueue(AtletaApplication.sharedInstance());
                }
            }
        }

        return mRequestQueue;
    }


    /**
     * Add your request to request queue
     *
     * @param req Request
     * @param tag String
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        req.setRetryPolicy(new DefaultRetryPolicy(
                DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(req);
    }


    /**
     * Add your request to request queue
     *
     * @param req Request
     */
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        req.setRetryPolicy(new DefaultRetryPolicy(
                DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(req);
    }


    /**
     * Cancel your request from request queue
     *
     * @param tag String
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


}
