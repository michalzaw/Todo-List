package com.mich.todolist.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

/**
 * Created by Michal on 01.11.2017.
 */

public class Task implements Parcelable {
    private int id;
    private String title;
    private String description;
    private String date;
    private int priority;
    private int category;

    public Task(int id, String title, String description, String date, int priority, int category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.priority = priority;
        this.category = category;
    }

    protected Task(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        date = in.readString();
        priority = in.readInt();
        category = in.readInt();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(date);
        parcel.writeInt(priority);
        parcel.writeInt(category);
    }
}
