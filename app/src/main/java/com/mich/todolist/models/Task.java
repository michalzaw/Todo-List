package com.mich.todolist.models;

import java.util.Calendar;

/**
 * Created by Michal on 01.11.2017.
 */

public class Task {
    private int id;
    private String title;
    private String description;
    private Calendar time;
    private int priority;
    private int category;

    public Task(int id, String title, String description, Calendar time, int priority, int category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.time = time;
        this.priority = priority;
        this.category = category;
    }

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

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
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
}
