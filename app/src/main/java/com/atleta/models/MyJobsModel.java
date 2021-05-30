package com.atleta.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyJobsModel implements Parcelable , Comparable<MyJobsModel> {

    private String title;
    private String description;
    private String date;
    private String jobType;
    private String yearOfExperience;
    private String skills;
    private String budgets;
    private String status;
    private String key;
    private ApplyJob applyJob;

    public String getKey() {
        return key;
    }

    public MyJobsModel() {}

    public String getDate() {
        return date;
    }

    public String getJobType() {
        return jobType;
    }

    public String getYearOfExperience() {
        return yearOfExperience;
    }

    public String getSkills() {
        return skills;
    }

    public String getBudgets() {
        return budgets;
    }

    public String getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public ApplyJob getApplyJob() {
        return applyJob;
    }

    public MyJobsModel(String title, String description, String date, String jobType, String yearOfExperience, String skills, String budgets, String status) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.jobType = jobType;
        this.yearOfExperience = yearOfExperience;
        this.skills = skills;
        this.budgets = budgets;
        this.status = status;
    }

    public MyJobsModel(String title, String description, String date, String jobType, String yearOfExperience, String skills, String budgets, String status, String key) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.jobType = jobType;
        this.yearOfExperience = yearOfExperience;
        this.skills = skills;
        this.budgets = budgets;
        this.status = status;
        this.key = key;
    }

    public MyJobsModel(String title, String description, String date, String jobType, String yearOfExperience, String skills, String budgets, String status, String key, ApplyJob applyJob) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.jobType = jobType;
        this.yearOfExperience = yearOfExperience;
        this.skills = skills;
        this.budgets = budgets;
        this.status = status;
        this.key = key;
        this.applyJob = applyJob;
    }

    public MyJobsModel(String title, String description, String date, String jobType, String yearOfExperience, String skills, String budgets) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.jobType = jobType;
        this.yearOfExperience = yearOfExperience;
        this.skills = skills;
        this.budgets = budgets;
    }

    protected MyJobsModel(Parcel in) {
        title = in.readString();
        description = in.readString();
        date = in.readString();
        jobType = in.readString();
        yearOfExperience = in.readString();
        skills = in.readString();
        budgets = in.readString();
        status = in.readString();
        key = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(date);
        dest.writeString(jobType);
        dest.writeString(yearOfExperience);
        dest.writeString(skills);
        dest.writeString(budgets);
        dest.writeString(status);
        dest.writeString(key);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MyJobsModel> CREATOR = new Creator<MyJobsModel>() {
        @Override
        public MyJobsModel createFromParcel(Parcel in) {
            return new MyJobsModel(in);
        }

        @Override
        public MyJobsModel[] newArray(int size) {
            return new MyJobsModel[size];
        }
    };

    @Override
    public int compareTo(MyJobsModel o) {
        return o.getDate().compareTo(getDate());
    }
}
  /*  SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_EMMMMDDYYYY);
    Date date1 = null;
    Date date2 = null;
        try {
                date1 = sdf.parse(getDate());
                } catch (ParseException e) {
                e.printStackTrace();
                }
                try {
                date2 = sdf.parse(o.getDate());
                } catch (ParseException e) {
                e.printStackTrace();
                }

                long getDate = date1.getTime();
                long getCompareDate = date2.getTime();

                return String.valueOf(getCompareDate).compareTo(String.valueOf(getDate));*/