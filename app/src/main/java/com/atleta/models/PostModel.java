package com.atleta.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class PostModel implements Parcelable {

    private String createdBy;
    private String CreatedDate;
    private String postDescription;
    private String createdByImage;
    private ArrayList<String> postImages = new ArrayList<>();
    private int LikesCount;
    private String postCommentCount;
    private boolean isBookmark = false;
    private boolean isFavourite = false;
    private ArrayList<CommentModel> commentList = new ArrayList<>();

    public PostModel(String createdBy, String createdDate, String postDescription, String createdByImage, ArrayList<String> postImages, int likesCount, String postCommentCount, boolean isBookmark, boolean isFavourite, ArrayList<CommentModel> commentList) {
        this.createdBy = createdBy;
        CreatedDate = createdDate;
        this.postDescription = postDescription;
        this.createdByImage = createdByImage;
        this.postImages = postImages;
        LikesCount = likesCount;
        this.postCommentCount = postCommentCount;
        this.isBookmark = isBookmark;
        this.isFavourite = isFavourite;
        this.commentList = commentList;
    }

    protected PostModel(Parcel in) {
        createdBy = in.readString();
        CreatedDate = in.readString();
        createdByImage = in.readString();
        postDescription = in.readString();
        postImages = in.createStringArrayList();
        LikesCount = in.readInt();
        postCommentCount = in.readString();
        isBookmark = in.readByte() != 0;
        isFavourite = in.readByte() != 0;
        commentList = in.createTypedArrayList(CommentModel.CREATOR);
    }

    public static final Creator<PostModel> CREATOR = new Creator<PostModel>() {
        @Override
        public PostModel createFromParcel(Parcel in) {
            return new PostModel(in);
        }

        @Override
        public PostModel[] newArray(int size) {
            return new PostModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(createdBy);
        dest.writeString(CreatedDate);
        dest.writeString(createdByImage);
        dest.writeString(postDescription);
        dest.writeStringList(postImages);
        dest.writeInt(LikesCount);
        dest.writeString(postCommentCount);
        dest.writeByte((byte) (isBookmark ? 1 : 0));
        dest.writeByte((byte) (isFavourite ? 1 : 0));
        dest.writeTypedList(commentList);
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getCreatedByImage() {
        return createdByImage;
    }

    public void setCreatedByImage(String createdByImage) {
        this.createdByImage = createdByImage;
    }

    public ArrayList<String> getPostImages() {
        return postImages;
    }

    public void setPostImages(ArrayList<String> postImages) {
        this.postImages = postImages;
    }

    public int getLikesCount() {
        return LikesCount;
    }

    public void setLikesCount(int likesCount) {
        LikesCount = likesCount;
    }

    public String getPostCommentCount() {
        return postCommentCount;
    }

    public void setPostCommentCount(String postComment) {
        this.postCommentCount = postComment;
    }

    public boolean isBookmark() {
        return isBookmark;
    }

    public void setBookmark(boolean bookmark) {
        isBookmark = bookmark;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public ArrayList<CommentModel> getCommentList() {
        return commentList;
    }

    public void setCommentList(ArrayList<CommentModel> commentList) {
        this.commentList = commentList;
    }
}
