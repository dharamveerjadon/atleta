package com.atleta.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atleta.R;
import com.atleta.fragments.AddComunityDetailsFragment;

public class CreateCommunityActivity extends BaseActivity implements View.OnClickListener{
    private Toolbar mToolbar;
    private TextView toolbarTitle;
    private ImageView toolbarIcon;
    private ActionBar actionBar;
    private LinearLayout lnrEditIconProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_community);

        mToolbar = findViewById(R.id.toolbar);
        toolbarTitle = mToolbar.findViewById(R.id.toolbar_title);
        lnrEditIconProfile = mToolbar.findViewById(R.id.lnr_edit);
        toolbarIcon = mToolbar.findViewById(R.id.img_icon);
        toolbarTitle.setText(getString(R.string.string_community));
        setSupportActionBar(mToolbar);

        if (savedInstanceState == null) {
            addCommunityFragment();
        }

        toolbarIcon.setOnClickListener(this);
    }

    @Override
    protected void syncActionBarArrowState() {

    }
    /**
     * Add the timeline fragment
     */
    private void addCommunityFragment() {
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        AddComunityDetailsFragment fragment = AddComunityDetailsFragment.newInstance(getString(R.string.string_community));
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_icon : finish(); break;
        }
    }
}