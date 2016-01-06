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
            MySQLiteHelper.COLUMN_DAY, MySQLiteHelper.COLUMN_TASK };

    public TaskDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Task createTask(String day, String task) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_DAY, day);
        values.put(MySQLiteHelper.COLUMN_TASK, task);

        long insertId = database.insert(MySQLiteHelper.TABLE_TASK, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_TASK,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Task newTask = cursorToTask(cursor);
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
        // make sure to close the cursor
        cursor.close();
        return tasks;
    }

    private Task cursorToTask(Cursor cursor) {
        Task task = new Task();
        task.setId(cursor.getLong(0));
        task.setDay(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_DAY)));
        task.setTask(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_TASK)));

        return task;
    }
}
