package com.atleta.models;

import android.os.Parcel;
import android.os.Parcelable;

public class AddCommunityModel implements Parcelable {

    private String id;
    private String communityName;
    private String topic;
    private String communityDescription;
    private String type;
    private String scope;
    private String profileImage;
    private String coverImage;
    private String userId;

    public AddCommunityModel() {}

    public AddCommunityModel(String id, String communityName, String topic, String communityDescription, String type, String scope, String profileImage, String coverImage, String userId) {
        this.id = id;
        this.communityName = communityName;
        this.topic = topic;
        this.communityDescription = communityDescription;
        this.type = type;
        this.scope = scope;
        this.profileImage = profileImage;
        this.coverImage = coverImage;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getCommunityDescription() {
        return communityDescription;
    }

    public void setCommunityDescription(String communityDescription) {
        this.communityDescription = communityDescription;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    protected AddCommunityModel(Parcel in) {
        id = in.readString();
        communityName = in.readString();
        topic = in.readString();
        communityDescription = in.readString();
        type = in.readString();
        scope = in.readString();
        profileImage = in.readString();
        coverImage = in.readString();
        userId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(communityName);
        dest.writeString(topic);
        dest.writeString(communityDescription);
        dest.writeString(type);
        dest.writeString(scope);
        dest.writeString(profileImage);
        dest.writeString(coverImage);
        dest.writeString(userId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AddCommunityModel> CREATOR = new Creator<AddCommunityModel>() {
        @Override
        public AddCommunityModel createFromParcel(Parcel in) {
            return new AddCommunityModel(in);
        }

        @Override
        public AddCommunityModel[] newArray(int size) {
            return new AddCommunityModel[size];
        }
    };
}
