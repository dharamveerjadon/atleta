package com.atleta.models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class UserModel implements Parcelable {

    private String userId;
    private String displayName;
    private String firstName;
    private String middleName;
    private String lastName;
    private String mobileNumber;
    private String location;
    private ArrayList<String> preference;
    private String profile_image_url;
    /**
     * Get the profile image url
     *
     * @return profile image url
     */
    public String getProfileImageUrl() {
        return profile_image_url;
    }

    public ArrayList<String> getPreference() {
        return preference;
    }

    public void setPreference(ArrayList<String> preference) {
        this.preference = preference;
    }

    public void setProfileImageUrl(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }


    public  UserModel() {}

    public UserModel(String displayName, String firstName, String middleName, String lastName, String mobileNumber, String location, ArrayList<String> preference,  String profileImage) {
        this.displayName = displayName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
        this.location = location;
        this.preference = preference;
        this.profile_image_url = profileImage;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    protected UserModel(Parcel in) {
        userId = in.readString();
        displayName = in.readString();
        firstName = in.readString();
        middleName = in.readString();
        lastName = in.readString();
        mobileNumber = in.readString();
        location = in.readString();
        profile_image_url = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLocation() {
        return location;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(displayName);
        dest.writeString(firstName);
        dest.writeString(middleName);
        dest.writeString(lastName);
        dest.writeString(mobileNumber);
        dest.writeString(location);
        dest.writeString(profile_image_url);

    }
}
