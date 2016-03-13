package com.example.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.w3c.dom.Comment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anunay on 5/1/16.
 */
public class TaskDataSource {
    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_DAY, MySQLiteHelper.COLUMN_TASK, MySQLiteHelper.COLUMN_TASK_DESCRIPTION, MySQLiteHelper.COLUMN_TASK_TIME,
            MySQLiteHelper.COLUMN_LATITUDE, MySQLiteHelper.COLUMN_LONGITUDE, MySQLiteHelper.COLUMN_TASK_ADDRESS };

    public TaskDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();

    }

    public void close() {
        dbHelper.close();
    }

    public Task createTask(String day, String task, String taskDescription, String taskTime, double latitude, double longitude, String taskAddress) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_DAY, day);
        values.put(MySQLiteHelper.COLUMN_TASK, task);
        values.put(MySQLiteHelper.COLUMN_TASK_DESCRIPTION, taskDescription);
        values.put(MySQLiteHelper.COLUMN_TASK_TIME, taskTime);
        values.put(MySQLiteHelper.COLUMN_LATITUDE, latitude);
        values.put(MySQLiteHelper.COLUMN_LONGITUDE, longitude);
        values.put(MySQLiteHelper.COLUMN_TASK_ADDRESS, taskAddress);

        System.out.println("Inserting task:" + task + " taskDescription:" + taskDescription + " taskTime:" + taskTime);
        long insertId = database.insert(MySQLiteHelper.TABLE_TASK, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_TASK,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Task newTask = cursorToTask(cursor);
        System.out.println("Fetching cursor task:"+newTask.getTask()+" taskDescription:"+newTask.getTaskDescription()+" taskTime:"
                +newTask.getTaskTime()+" day:"+newTask.getDay()+" latitude:"+newTask.getLatitude()+" longitude:"+newTask.getLongitude());

        cursor.close();
        return newTask;
    }

    public void deleteTask(String task) {
        database.delete(MySQLiteHelper.TABLE_TASK, MySQLiteHelper.COLUMN_TASK
                + " = '" + task+"'", null);
    }

    public List<Task> getAllTask(String day) {
        List<Task> tasks = new ArrayList<Task>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_TASK,
                allColumns, MySQLiteHelper.COLUMN_DAY + " = '" + day +"'", null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Task task = cursorToTask(cursor);
            tasks.add(task);
            cursor.moveToNext();
        }
        System.out.println(tasks);
        // make sure to close the cursor
        cursor.close();
        return tasks;
    }

    public List<Task> getTask(String day, String taskTitle) {
        List<Task> tasks = new ArrayList<Task>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_TASK,
                allColumns, MySQLiteHelper.COLUMN_DAY + " = '" + day +"' and " + MySQLiteHelper.COLUMN_TASK + " = '" + taskTitle +"'", null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Task task = cursorToTask(cursor);
            tasks.add(task);
            cursor.moveToNext();
        }
        System.out.println(tasks);
        // make sure to close the cursor
        cursor.close();
        return tasks;
    }

    private Task cursorToTask(Cursor cursor) {
        Task task = new Task();
        task.setId(cursor.getLong(0));
        task.setDay(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_DAY)));
        task.setTask(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_TASK)));
        task.setTaskDescription(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_TASK_DESCRIPTION)));
        task.setTaskTime(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_TASK_TIME)));
        task.setLatitude(cursor.getDouble(cursor.getColumnIndex(MySQLiteHelper.COLUMN_LATITUDE)));
        task.setLongitude(cursor.getDouble(cursor.getColumnIndex(MySQLiteHelper.COLUMN_LONGITUDE)));
        task.setTaskAddress(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_TASK_ADDRESS)));


        return task;
    }
}
