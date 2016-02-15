package com.example.android.db;

/**
 * Created by Anunay on 5/1/16.
 */
public class Task {
    private long id;
    private String day;
    private String task;
    private String taskDescription;
    private String taskTime;

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    // Will be used by the ArrayAdapter in the ListView
    public String toString(){
        return task;
    }
}
