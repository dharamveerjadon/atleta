package com.atleta.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.atleta.R;
import com.atleta.fragments.CreateJoinCoummunityFragment;

public class FirstUserEntryActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = FirstUserEntryActivity.class.toString();
    //toolbar
    private Toolbar mToolbar;
    private ImageView toolbarIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_user_entry);

        // Set Tool bar to the screen
        mToolbar = findViewById(R.id.toolbar);
        toolbarIcon = mToolbar.findViewById(R.id.img_icon);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        if (savedInstanceState == null) {
            // get fragment manager
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, CreateJoinCoummunityFragment.newInstance(null));
            ft.commit();
        }

        toolbarIcon.setOnClickListener(this);
    }

    @Override
    protected void syncActionBarArrowState() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            mToolbar.setNavigationIcon(null);
        } else {
            mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_icon:
                finish();
                break;
        }
    }
}