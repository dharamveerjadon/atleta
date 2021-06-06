package com.atleta.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.atleta.R;
import com.atleta.models.AddCommunityModel;
import com.atleta.models.PostModel;
import com.atleta.utils.AtletaApplication;
import com.atleta.utils.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CommunityAdapter extends BaseAdapter {

    //corporate list view
    private static final int TYPE_ITEM = 0;

    //show footer view
    private static final int TYPE_EMPTY = 1;

    private final OnItemClickListener mOnItemClickListener;
    @SuppressWarnings("CanBeFinal")
    private final LayoutInflater mInflater;
    private final Context context;
    private List<AddCommunityModel> mItems;
     Activity activity;
    private long mItemCountOnServer;

    public CommunityAdapter(Context context, Activity activity, CommunityAdapter.OnItemClickListener onItemClickListener) {
        this.context = context;
        this.activity = activity;
        mOnItemClickListener = onItemClickListener;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public interface OnItemClickListener {
        void onItemClick(AddCommunityModel item);
    }

    /**
     * set the article items
     *
     * @param items             article items
     * @param itemCountOnServer total item count on the server
     */
    public void setItems(final List<AddCommunityModel> items, final long itemCountOnServer) {
        mItems = items;
        mItemCountOnServer = itemCountOnServer;
        notifyDataSetChanged();
    }

    /**
     * add items to list
     *
     * @param start             starting index
     * @param items             items list
     * @param itemCountOnServer item count on server
     */
    public void addItems(int start, final List<AddCommunityModel> items, final long itemCountOnServer) {
        //remove the expire result of this page due to caching
        mItems.subList(start, mItems.size()).clear();

        mItems.addAll(items);
        mItemCountOnServer = itemCountOnServer;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        if (mItems == null) {
            return 0;
        }
        //plus one for footer
        return mItems.size();
    }

    /**
     * get the article items
     *
     * @return article items
     */
    public List<AddCommunityModel> getItems() {
        return mItems;
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        //if the position is greater or equal to item size then show footer
        final int size = mItems.size();
        if (size == 0) {
            return TYPE_EMPTY;
        }
        return TYPE_ITEM;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = this.getItemViewType(position);
        View v = convertView;
        switch (viewType) {
            case TYPE_ITEM:
                final AddCommunityModel item = mItems.get(position);
                ItemViewHolder itemViewHolder;
                if (v == null) {
                    v = mInflater.inflate(R.layout.fragment_community_info_list_item, parent, false);
                    itemViewHolder = new ItemViewHolder(v, mOnItemClickListener);
                    v.setTag(itemViewHolder);
                } else {
                    itemViewHolder = (ItemViewHolder) v.getTag();
                }
                itemViewHolder.bind(context, activity, item);
                return v;

            default:
                return mInflater.inflate(R.layout.fragment_blank, parent, false);

        }
    }

    /**
     * Item View Holder
     */
    private static class ItemViewHolder implements View.OnClickListener {

        //view on click listener need to forward click events
        private final CommunityAdapter.OnItemClickListener mOnItemClickListener;
        private ImageView mProfileImage, mCoverImage;
        private final TextView mHeaderCommunityName, mHeaderCommunityDesc;
        private final TextView txtMember;
        private Button btnJoin;
        // current bind to view holder
        private AddCommunityModel mCurrentItem;
        SliderPagerAdapter sliderPagerAdapter;
        ItemViewHolder(@NonNull View view, final CommunityAdapter.OnItemClickListener listener) {
            mOnItemClickListener = listener;
            view.setOnClickListener(this);
            mProfileImage = view.findViewById(R.id.image_profile);
            mCoverImage = view.findViewById(R.id.image_cover);
            mHeaderCommunityName = view.findViewById(R.id.txt_comunity_name);
            mHeaderCommunityDesc = view.findViewById(R.id.txt_community_description);
            txtMember = view.findViewById(R.id.txt_joined_member);
            btnJoin = view.findViewById(R.id.btn_join);
        }

        @Override
        public void onClick(View view) {
            if (mCurrentItem != null && mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(mCurrentItem);
            }
        }

        /**
         * Bind the the values of the view holder
         *
         * @param item article item
         */
        void bind(Context context, Activity activity, final AddCommunityModel item) {
            mCurrentItem = item;

            if(!TextUtils.isEmpty(item.getCoverImage())) {
                Glide.with(AtletaApplication.sharedInstance())
                        .load(item.getCoverImage())
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
                        .placeholder(R.drawable.ic_avatar)
                        .dontAnimate()
                        .into(mCoverImage);
            }

            if(!TextUtils.isEmpty(item.getProfileImage())) {
                Glide.with(AtletaApplication.sharedInstance())
                        .load(item.getProfileImage())
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
                        .placeholder(R.drawable.ic_avatar)
                        .dontAnimate()
                        .into(mProfileImage);
            }

            mHeaderCommunityName.setText(item.getCommunityName());
            mHeaderCommunityDesc.setText(item.getCommunityDescription());

        }

        private String setDateFormat(String value) {
            DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_EDDMMMYYYY);
            Date date = new Date(value);
            return dateFormat.format(date);
        }
    }
}
