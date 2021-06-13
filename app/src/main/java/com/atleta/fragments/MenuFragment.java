package com.atleta.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.atleta.R;
import com.atleta.adapters.MenuAdapter;
import com.atleta.interfaces.MenuItemInteraction;
import com.atleta.models.MenuItem;
import com.atleta.models.Session;
import com.atleta.models.UserModel;
import com.atleta.utils.AppPreferences;
import com.atleta.utils.Keys;
import com.atleta.utils.AtletaApplication;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created on 7/03/17.
 */
public class MenuFragment extends BaseFragment {

    //activity fragment communication
    private MenuItemInteraction mListener;
    private ProgressBar mProgressBar;
    private ImageView mImvMember;


    @SuppressWarnings("CanBeFinal")
    private BroadcastReceiver mProfileImageBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setImageOnView();
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mProfileImageBroadcast,
                new IntentFilter(Keys.BROADCAST_PROFILE_IMAGE));

    }

    @Override
    public void onResume() {
        super.onResume();
        setImageOnView();
    }

    /**
     * setting image on circle Image View
     */
    private void setImageOnView() {
        Session session = AppPreferences.getSession();

        if (session != null  ) {

           /* Glide.with(AtletaApplication.sharedInstance())
                    .load(session.getUserModel().getProfile_image_url())
                    .placeholder(R.drawable.ic_avatar) // can also be a drawable
                    .error(R.drawable.ic_avatar) // will be displayed if the image cannot be loaded
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(mImvMember);*/

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_menu, container, false);

        final ListView listView = (ListView) view.findViewById(R.id.listView);

        final List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem(R.string.string_home, R.drawable.ic_home_black));
        items.add(new MenuItem(R.string.string_message, R.drawable.ic_job_grey));
        items.add(new MenuItem(R.string.string_logout, R.drawable.ic_logout));

        final MenuAdapter menuAdapter = new MenuAdapter(getActivity(), items);

        listView.setOnItemClickListener((parent, v, position, id) -> {
            if (mListener != null) {
                //minus one for header
                mListener.onMenuClick((MenuItem) menuAdapter.getItem(position - 1));
            }
        });

        listView.setAdapter(menuAdapter);

        final ViewGroup headerView = (ViewGroup) inflater.inflate(R.layout.nav_header_menu, listView, false);
        mProgressBar = (ProgressBar) headerView.findViewById(R.id.progress_bar);
        listView.addHeaderView(headerView, null, false);
        final TextView textView = (TextView) headerView.findViewById(R.id.textView);
        mImvMember = (CircleImageView) headerView.findViewById(R.id.imv_member);

        Session session = AppPreferences.getSession();
        textView.setText(session.getEmailId());

        setImageOnView();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MenuItemInteraction) {
            mListener = (MenuItemInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mProfileImageBroadcast);
    }
}
