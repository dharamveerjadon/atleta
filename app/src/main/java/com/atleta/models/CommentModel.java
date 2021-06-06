package com.atleta.models;

import android.os.Parcel;
import android.os.Parcelable;

public class CommentModel implements Parcelable {

    private String comment;
    private String commentedBY;
    private String commentedDate;
    private String image;


    public CommentModel(String comment, String commentedBY, String commentedDate, String image) {
        this.comment = comment;
        this.commentedBY = commentedBY;
        this.commentedDate = commentedDate;
        this.image = image;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentedBY() {
        return commentedBY;
    }

    public void setCommentedBY(String commentedBY) {
        this.commentedBY = commentedBY;
    }

    public String getCommentedDate() {
        return commentedDate;
    }

    public void setCommentedDate(String commentedDate) {
        this.commentedDate = commentedDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    protected CommentModel(Parcel in) {
        comment = in.readString();
        commentedBY = in.readString();
        commentedDate = in.readString();
        image = in.readString();
    }

    public static final Creator<CommentModel> CREATOR = new Creator<CommentModel>() {
        @Override
        public CommentModel createFromParcel(Parcel in) {
            return new CommentModel(in);
        }

        @Override
        public CommentModel[] newArray(int size) {
            return new CommentModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(comment);
        dest.writeString(commentedBY);
        dest.writeString(commentedDate);
        dest.writeString(image);
    }
}
