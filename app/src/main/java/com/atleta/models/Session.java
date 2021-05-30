package com.atleta.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Session implements Parcelable {

    private String userId;
    private String displayName;
    private String emailId;
    private String createPassword;
    private String repeatpassword;
    private UserModel userModel;
    private String userToken;
    private String actionType;
    private boolean isAdmin = false;

    /**
     * Provides Current user
     *
     * @return User
     */
    public UserModel getCurrentUser() {
        return userModel;
    }


public Session() {}
    public Session(String userId, String displayName, String emailId, String createPassword, String repeatpassword, UserModel userModel, String userToken, boolean isAdmin) {
        this.userId = userId;
        this.displayName = displayName;
        this.emailId = emailId;
        this.createPassword = createPassword;
        this.repeatpassword = repeatpassword;
        this.userModel = userModel;
        this.userToken = userToken;
        this.isAdmin = isAdmin;
    }

    public Session( String mobileNumber, UserModel userModel, String userToken) {
        this.userModel = userModel;
        this.userToken = userToken;
    }


    protected Session(Parcel in) {
        userId = in.readString();
        emailId = in.readString();
        createPassword = in.readString();
        repeatpassword = in.readString();
        userToken = in.readString();
        isAdmin = in.readByte() != 0;
        actionType = in.readString();
    }

    public static final Creator<Session> CREATOR = new Creator<Session>() {
        @Override
        public Session createFromParcel(Parcel in) {
            return new Session(in);
        }

        @Override
        public Session[] newArray(int size) {
            return new Session[size];
        }
    };

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setCreatePassword(String createPassword) {
        this.createPassword = createPassword;
    }

    public void setRepeatpassword(String repeatpassword) {
        this.repeatpassword = repeatpassword;
    }


    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getCreatePassword() {
        return createPassword;
    }

    public String getRepeatpassword() {
        return repeatpassword;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getActionType() {
        return actionType;
    }

    public String getUserToken() {
        return userToken;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(displayName);
        dest.writeString(emailId);
        dest.writeString(createPassword);
        dest.writeString(repeatpassword);
        dest.writeString(userToken);
        dest.writeString(actionType);
        dest.writeByte((byte) (isAdmin ? 1 : 0));
    }
}
