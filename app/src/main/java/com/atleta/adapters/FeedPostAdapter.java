package com.atleta.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.atleta.R;
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

public class FeedPostAdapter extends BaseAdapter {

    //corporate list view
    private static final int TYPE_ITEM = 0;

    //show footer view
    private static final int TYPE_EMPTY = 1;

    private final OnItemClickListener mOnItemClickListener;
    @SuppressWarnings("CanBeFinal")
    private final LayoutInflater mInflater;
    private final Context context;
    private List<PostModel> mItems;
     Activity activity;
    private long mItemCountOnServer;

    public FeedPostAdapter(Context context, Activity activity,  FeedPostAdapter.OnItemClickListener onItemClickListener) {
        this.context = context;
        this.activity = activity;
        mOnItemClickListener = onItemClickListener;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public interface OnItemClickListener {
        void onItemClick(PostModel item);
    }

    /**
     * set the article items
     *
     * @param items             article items
     * @param itemCountOnServer total item count on the server
     */
    public void setItems(final List<PostModel> items, final long itemCountOnServer) {
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
    public void addItems(int start, final List<PostModel> items, final long itemCountOnServer) {
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
    public List<PostModel> getItems() {
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
                final PostModel item = mItems.get(position);
                ItemViewHolder itemViewHolder;
                if (v == null) {
                    v = mInflater.inflate(R.layout.item_feed_atleta, parent, false);
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
        private final FeedPostAdapter.OnItemClickListener mOnItemClickListener;
        private ImageView mProfileImage, mThreeDot;
        private final TextView mHeaderTitle, mHeaderSubTitle;
        private final ViewPager viewPager;
        private final DotsIndicator dotsIndicator;
        private final ImageView imgHeart, imgComment, imgForward, imgCollection;
        private final TextView txtLikes, txtCommentView;
        // current bind to view holder
        private PostModel mCurrentItem;
        SliderPagerAdapter sliderPagerAdapter;
        ItemViewHolder(@NonNull View view, final FeedPostAdapter.OnItemClickListener listener) {
            mOnItemClickListener = listener;
            view.setOnClickListener(this);
            mProfileImage = view.findViewById(R.id.img_profile);
            mThreeDot = view.findViewById(R.id.three_dot);
            mHeaderTitle = view.findViewById(R.id.header_title);
            mHeaderSubTitle = view.findViewById(R.id.header_sub_title);
            viewPager = view.findViewById(R.id.view_pager);
            dotsIndicator = view.findViewById(R.id.dots_indicator);
            imgHeart = view.findViewById(R.id.icon_heart);
            imgComment = view.findViewById(R.id.icon_comment);
            imgForward = view.findViewById(R.id.icon_post_Share);
            imgCollection = view.findViewById(R.id.icon_save_collection);
            txtLikes = view.findViewById(R.id.txt_likes);
            txtCommentView = view.findViewById(R.id.txt_view_comments);
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
        void bind(Context context, Activity activity, final PostModel item) {
            mCurrentItem = item;

            if(!TextUtils.isEmpty(item.getCreatedByImage())) {
                Glide.with(AtletaApplication.sharedInstance())
                        .load(item.getCreatedByImage())
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

            sliderPagerAdapter = new SliderPagerAdapter(activity, item.getPostImages());
            viewPager.setAdapter(sliderPagerAdapter);
            dotsIndicator.setViewPager(viewPager);

            mHeaderTitle.setText(item.getCreatedBy());
            mHeaderSubTitle.setText(setDateFormat(item.getCreatedDate()));
            txtLikes.setText(setDateFormat(item.getLikesCount()+" Likes"));
            txtCommentView.setText(item.getPostCommentCount() + " Comments");

        }

        private String setDateFormat(String value) {
            DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_EDDMMMYYYY);
            Date date = new Date(value);
            return dateFormat.format(date);
        }
    }
}
