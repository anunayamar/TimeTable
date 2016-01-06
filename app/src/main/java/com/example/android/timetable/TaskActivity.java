package com.example.android.timetable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.db.Task;
import com.example.android.db.TaskDataSource;

import org.w3c.dom.Comment;

import java.sql.SQLException;
import java.util.List;

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

        /*if(dayName.equals("Monday")){
            showTask();
        }*/
        displayTask(dayName);

    }

    public void displayTask(String day){
        dataSource = new TaskDataSource(this);
        try{
            dataSource.open();
        }catch(SQLException e){
            e.printStackTrace();
        }

        //Task task=dataSource.createTask("Monday", "Tasked Created via SQLite");

        List<Task> values = dataSource.getAllTask(day);

        listView= (ListView)findViewById(R.id.listView);
        ArrayAdapter<Task> adapter=new ArrayAdapter<Task>(this,R.layout.task_row, R.id.textView, values);
        listView.setAdapter(adapter);

        registerForContextMenu(listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String taskDescription= ((TextView)info.targetView.findViewById(R.id.textView)).getText().toString();

        Toast.makeText(this,taskDescription,Toast.LENGTH_LONG).show();
        dataSource.deleteTask(taskDescription);

        if(item.getTitle().toString().equals("Delete")){
            Toast.makeText(this,"Deleted",Toast.LENGTH_LONG).show();
        }
        return true;
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

    public void createTask(View view){
        Intent intent = new Intent(this, TaskGenerator.class);
        startActivity(intent);
    }
}

