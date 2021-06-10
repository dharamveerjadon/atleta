package com.atleta.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemViewStateModel implements Parcelable {
    private String id;
    private String value;
    private boolean isCheck;

    public ItemViewStateModel() {
    }

    public ItemViewStateModel(String id, String value, boolean isCheck) {
        this.id = id;
        this.value = value;
        this.isCheck = isCheck;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    protected ItemViewStateModel(Parcel in) {
        id = in.readString();
        value = in.readString();
        isCheck = in.readByte() != 0;
    }

    public static final Creator<ItemViewStateModel> CREATOR = new Creator<ItemViewStateModel>() {
        @Override
        public ItemViewStateModel createFromParcel(Parcel in) {
            return new ItemViewStateModel(in);
        }

        @Override
        public ItemViewStateModel[] newArray(int size) {
            return new ItemViewStateModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(value);
        dest.writeByte((byte) (isCheck ? 1 : 0));
    }
}
