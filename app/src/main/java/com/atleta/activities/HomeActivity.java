package com.atleta.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.places.Places;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.atleta.R;
import com.atleta.customview.TabBar;
import com.atleta.fragments.MessageFragment;
import com.atleta.fragments.MyProfileFragment;
import com.atleta.fragments.HomeFragment;
import com.atleta.interfaces.MenuItemInteraction;
import com.atleta.models.MenuItem;
import com.atleta.models.Session;
import com.atleta.utils.AppPreferences;
import com.atleta.utils.AtletaApplication;
import com.atleta.utils.Keys;
import com.atleta.utils.Utils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.atleta.utils.AppPreferences.SELECTED_HOME_SCREEN;

public class HomeActivity extends BaseActivity implements MenuItemInteraction ,  GoogleApiClient.OnConnectionFailedListener,  GoogleApiClient.ConnectionCallbacks, LocationListener {
    //toolbar
    private Toolbar mToolbar;
    private TextView toolbarTitle;
    private final String TAG = this.getClass().getSimpleName();
    private ActionBar actionBar;
    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static final int REQUEST_CHECK_SETTINGS = 555;
    private LinearLayout lnrEditIconProfile;
    //Reference to drawer layout to open or hide the menu
    private DrawerLayout mDrawerLayout;
    public static GoogleApiClient mGoogleApiClient;
    // This is drawer listener to close and open the drawer
    private ActionBarDrawerToggle mDrawerToggle;
    //bottom tab bar
    private TabBar mTabBar;
    public double currentLatitude;
    public double currentLongitude;
    private LocationRequest mLocationRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set Tool bar to the screen
        mToolbar = findViewById(R.id.toolbar);
        toolbarTitle = mToolbar.findViewById(R.id.toolbar_title);
        lnrEditIconProfile = mToolbar.findViewById(R.id.lnr_edit);
        toolbarTitle.setText(mToolbar.getTitle());
        setSupportActionBar(mToolbar);

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        Session session = AppPreferences.getSession();

        mDrawerLayout = findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(
                /* host Activity */
                this,
                /* DrawerLayout object */
                mDrawerLayout,
                0, 0) {
            public void onDrawerClosed(View view) {
                syncActionBarArrowState();
                // creates call to onPrepareOptionsMenu()
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                // creates call to onPrepareOptionsMenu()
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();


        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1000); // 1 second, in milliseconds


        mTabBar = findViewById(R.id.tab_bar);
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem(R.string.string_home, R.drawable.ic_home_black));
        items.add(new MenuItem(R.string.string_message, R.drawable.ic_message_black));
        items.add(new MenuItem(R.string.string_profile, R.drawable.ic_profile_grey));
        mTabBar.setMenuItems(items);


        mTabBar.setOnMenuClickListener(new MenuItemInteraction() {
            @Override
            public void onMenuClick(MenuItem menuItem) {
                switch (menuItem.textResId) {
                    case R.string.string_home:
                        addHomeFragment();
                        break;

                    case R.string.string_message:
                        addMessageFragment();
                        break;


                    case R.string.string_profile:
                        addMyProfileFragment();
                        break;
                    default:
                        mTabBar.setSelectedIndex(0, false);
                        addHomeFragment();
                }
            }

            @Override
            public void onPopClick() {
                HomeActivity.this.onPopClick();
            }
        });

        if (savedInstanceState == null) {
                mTabBar.setSelectedIndex(AppPreferences.getSelectedHomeScreen(), false);
        }

        new Handler().postDelayed(() -> onHandleNotification(getIntent()), 1000);

        googlePlusInitalize();
        lnrEditIconProfile.setOnClickListener(v -> {
            Session editSession = AppPreferences.getSession();
        });

    }

    //================== GooglePlusLogin ======================================
    public void googlePlusInitalize() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    @Override
    protected void syncActionBarArrowState() {
        mDrawerToggle.setDrawerIndicatorEnabled(getSupportFragmentManager().getBackStackEntryCount() == 0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        onHandleNotification(intent);
    }

    /**
     * Add the more fragment
     */
    private void openCongratulationScreen(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    /**
     * handle notifications
     *
     * @param intent intent
     */

    private void onHandleNotification(Intent intent) {
        if (intent.hasExtra(Keys.NOTIFICATION)) {
            if (intent.getSerializableExtra(Keys.NOTIFICATION) instanceof HashMap) {
                @SuppressWarnings("unchecked") HashMap<String, String> map = (HashMap<String, String>) intent
                        .getSerializableExtra(Keys.NOTIFICATION);
                if (map != null) {
                    String type = map.get(Keys.TYPE);
                    if (!TextUtils.isEmpty(type)) {
                        switch (type) {
                            case Keys.TYPE_ADD_JOB:
                                String jobname = map.get("job_name");
                                String description = map.get("job_description");
                                String date = map.get("job_date");
                                String budgets = map.get("job_budgets");
                                String yearofexperience = map.get("job_experience");
                                String category = map.get("job_category");
                                String skills = map.get("skills_required");
                                String key = map.get("key");

                                break;

                            case Keys.TYPE_PROFILE_SELECTED:
                                addMessageFragment();

                                break;

                        }
                    }
                }
            }
        }
    }

    /**
     * logout the user
     */
    private void logout() {
        //show the confirmation dialog
        Utils.showConfirmDialog(this, getString(R.string.string_logout), getString(R.string.logout_message), android.R
                .string.yes, android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull android.view.MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeDrawers();
        } else {
            onBackPressed();
        }
        return true;
    }

    /**
     * close drawer on screen layout
     */
    private void closeDrawers() {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawers();
        }
    }

    /**
     * handles the back button pressed functionality
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            closeDrawers();
            return;
        } else if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            if (mTabBar.getSelectedIndex() != 0) {
                AppPreferences.setSelectedHomeScreen(SELECTED_HOME_SCREEN, 0);
                mTabBar.setSelectedIndex(0, false);
                return;
            } else {
                Fragment fragment = getTopFragment();
                if (fragment != null && fragment instanceof MessageFragment) {
                    mTabBar.setSelectedIndex(1, false);
                } else if (fragment != null && fragment instanceof MyProfileFragment) {
                    mTabBar.setSelectedIndex(2, false);
                } else {
                    mTabBar.setSelectedIndex(0, false);
                }
            }
        }

        super.onBackPressed();
    }

    @Override
    public void onMenuClick(MenuItem item) {
//close the drawers
        closeDrawers();

        switch (item.textResId) {
            case R.string.string_home:
                mTabBar.setSelectedIndex(0, false);
                addHomeFragment();
                break;

            case R.string.string_message:
                mTabBar.setSelectedIndex(2, false);
                addMessageFragment();
                break;

            case R.string.string_logout:
                redirectToLoginActivity(this);
                break;
        }

    }

    private void redirectToLoginActivity(Context context) {
        AppPreferences.logout();
        context.startActivity(new Intent(context, SignUpActivity.class));
        ((Activity) context).finish();
    }

    /**
     * Add the timeline fragment
     */
    private void addHomeFragment() {
        lnrEditIconProfile.setVisibility(View.INVISIBLE);
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        HomeFragment fragment = HomeFragment.newInstance(getString(R.string.app_name));
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Add the job fragment
     */
    private void addMessageFragment() {
        lnrEditIconProfile.setVisibility(View.INVISIBLE);
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        MessageFragment fragment = MessageFragment.newInstance(getString(R.string.string_feed));
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    private void addCommunityFragment() {
    }


    /**
     * Add the profile fragment
     */
    private void addMyProfileFragment() {
        lnrEditIconProfile.setVisibility(View.VISIBLE);
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        MyProfileFragment fragment = MyProfileFragment.newInstance(getString(R.string.string_profile));
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onPopClick() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            FragmentManager fm = getSupportFragmentManager();
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        toolbarTitle.setText(title);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
        int index = AppPreferences.getSelectedHomeScreen();
        if (index >= 0 && index < 3) {
            MenuItem menuItem = mTabBar.getMenuItem(index);
            setCurrentScreen(menuItem.name, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.stopAutoManage(this);
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppPreferences.setSelectedHomeScreen(SELECTED_HOME_SCREEN, 0);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            handleNewLocation(location);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Log.i(TAG, "User agreed to make required location settings changes.");
                    mGoogleApiClient.reconnect();
                    break;
                case Activity.RESULT_CANCELED:
                    Log.i(TAG, "User chose not to make required location settings changes.");
                    break;
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void handleNewLocation(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        handleNewLocation(location);
    }
}
