package com.atleta.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.atleta.R;
import com.atleta.models.Session;
import com.atleta.utils.AppPreferences;

import org.jetbrains.annotations.NotNull;

public class FeedsFragment extends BaseFragment {
    private int mSelectedSubIndex = 0;
    private ViewPager mViewPager;
    private Session session;


    public static FeedsFragment newInstance(String title) {
        FeedsFragment fragment = new FeedsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,

                             Bundle savedInstanceState) {
        // Set title
        setToolbarTitle(getTitle());
        View view =  inflater.inflate(R.layout.fragment_feeds, container, false);
        session = AppPreferences.getSession();
        viewById(view);

        return view;
    }


    private void viewById(View view) {
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);

        mViewPager = view.findViewById(R.id.pager);
        mViewPager.setAdapter(new Pager(getChildFragmentManager()));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(mViewPager);


    }

    /**
     * get selected item in view pager
     *
     * @return
     */
    public int getCurrentItem() {
        return mViewPager.getCurrentItem();
    }

    /**
     * Set current item in view pager
     *
     * @param item index
     */
    public void setCurrentItem(int item) {
        mViewPager.setCurrentItem(item);
    }

    private class Pager extends FragmentStatePagerAdapter {

        private Pager(FragmentManager fm) {
            super(fm);
        }

        //Overriding method getItem
        @NotNull
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            if (position ==  0)
                fragment = FeedPostFragment.newInstance("All Post");

            if(position == 1)
                fragment = FeedPostFragment.newInstance("Trending");

            if(position == 2)
                fragment = FeedPostFragment.newInstance("Challenges");


       return fragment;
        }

        //Overridden method getCount to get the number of tabs
        @Override
        public int getCount() {

            return AppPreferences.getSession().isAdmin() ? 4 : 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = null;
            if (position == 0)
                title = "All Post";

             if (position == 1)
                title = "Trending";

            if (position == 2)
                title = "Challenges";

            if (position == 3)
                title = "Admin";

            return title;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(AppPreferences.IsCongratulationDone()){
            AppPreferences.setIsCongratulationDone(AppPreferences.IsCONGRATULATIONACTIONDONE, false);
            mViewPager.postDelayed(() -> mViewPager.setCurrentItem(1), 100);
        }

    }
}
