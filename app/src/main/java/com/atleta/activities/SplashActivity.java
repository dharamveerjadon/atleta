package com.atleta.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.atleta.R;
import com.atleta.models.Session;
import com.atleta.utils.AppPreferences;
import com.atleta.utils.Keys;
import com.atleta.utils.Utils;

public class SplashActivity extends AppCompatActivity {

    private final int INTERVAL_TIME = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Intent intent = getIntent();
        if (Utils.isAppUrl(intent.getDataString()) || intent.hasExtra(Keys.NOTIFICATION)) {
            //if the app opens using link or notification then don't delay
            takeDecisionBasedOnSession(intent);
        } else {
            new Handler().postDelayed(() -> takeDecisionBasedOnSession(intent), INTERVAL_TIME);
        }
    }

    /**
     * /**
     * Take decision based on session to show the login screen or auto login to app
     *
     * @param intent intent
     */
    private void takeDecisionBasedOnSession(Intent intent) {
        Session session = AppPreferences.getSession();
        Intent newIntent = new Intent();
        newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (AppPreferences.isLoggedIn()) {
            // user is already loggedIn open the MainActivity
                newIntent.setClass(this, HomeActivity.class);
                newIntent.putExtra(Keys.NOTIFICATION, intent.getSerializableExtra(Keys.NOTIFICATION));
                if (Utils.isAppUrl(intent.getDataString())) {
                    //if app is open using reset password link from email and user is already login then show the message
                    Utils.showToast(this, null, getString(R.string.already_logged_in));
                }
        } else {
            //open the login activity
            newIntent.putExtras(intent);
            newIntent.setData(intent.getData());
            newIntent.setClass(this, SignUpActivity.class);
        }
        startActivity(newIntent);
        finish();
    }

}
