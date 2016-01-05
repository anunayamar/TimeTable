package com.example.android.timetable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.android.db.Task;
import com.example.android.db.TaskDataSource;

import java.sql.SQLException;

public class TaskActivity extends Activity {

    private String []data= {"Wake up", "Take a shit", "Bath", "Feed your belly"};
    private ListView listView;
    private TaskDataSource dataSource = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        setContentView(R.layout.taskview);

        String dayName=intent.getCharSequenceExtra("DAY_NAME").toString();

        if(dayName.equals("Monday")){
            showTask();
        }

    }

    public void displayTask(){

    }

    public void showTask(){

        dataSource = new TaskDataSource(this);
        try{
            dataSource.open();
        }catch(SQLException e){
            e.printStackTrace();
        }

        Task task=dataSource.createTask("Monday", "Tasked Created via SQLite");
        Log.i("Anunay",task.toString());
        System.out.println(task.toString());
        Log.e("DaysList", "anunay1 " + (ListView) findViewById(R.id.listView));
        listView= (ListView)findViewById(R.id.listView);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.task_row, R.id.textView, data);
        listView.setAdapter(adapter);
    }
}

