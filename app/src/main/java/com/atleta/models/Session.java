package com.atleta.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Session implements Parcelable {

    private String userId;
    private String name;
    private String emailId;
    private String password;
    private String gender;
    private String number;
    private String dob;
    private boolean isAdmin = false;



public Session() {}

    public Session(String name, String emailId, String password, String gender, String dob) {
        this.name = name;
        this.emailId = emailId;
        this.password = password;
        this.gender = gender;
        this.dob = dob;
    }

    protected Session(Parcel in) {
        userId = in.readString();
        emailId = in.readString();
        number = in.readString();
        password = in.readString();
        gender = in.readString();
        dob = in.readString();
        isAdmin = in.readByte() != 0;
        name = in.readString();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(name);
        dest.writeString(emailId);
        dest.writeString(password);
        dest.writeString(gender);
        dest.writeString(number);
        dest.writeString(dob);
        dest.writeByte((byte) (isAdmin ? 1 : 0));
    }
}
