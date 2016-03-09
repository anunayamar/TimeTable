package com.example.android.timetable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.android.service.Adapter;

import java.sql.SQLException;
import java.util.List;

public class TaskActivity extends Activity {

    /*private String []data= {"Wake up", "Take a shit", "Bath", "Feed your belly"};*/
    private ListView listView;
    private TaskDataSource dataSource = null;
    private String dayName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        setContentView(R.layout.taskview);

        System.out.println("TaskActivity: in onCreate()");
        dayName=intent.getCharSequenceExtra("DAY_NAME").toString();

        /*if(dayName.equals("Monday")){
            showTask();
        }*/
        displayTask();

    }

    public void displayTask(){
        dataSource = new TaskDataSource(this);
        try{
            dataSource.open();
        }catch(SQLException e){
            e.printStackTrace();
        }

        /*Task task=dataSource.createTask("Monday", "Tasked Created via SQLite", "Task description custom", "10:00 AM");
        System.out.println("Task :"+task);*/

        List<Task> values = dataSource.getAllTask(dayName);

        String taskTitles[]= new String[values.size()];
        String taskDescriptions[]= new String[values.size()];

        int i=0;
        for(Task task: values){
            taskTitles[i]=task.getTask();
            taskDescriptions[i]=task.getTaskDescription();
            System.out.println("Task Time:" + task.getTaskTime());
            i++;
        }


        System.out.println("Task Description:" + taskDescriptions);

        dataSource.close();

        listView= (ListView)findViewById(R.id.listView);

        final Adapter adapter = new Adapter(this, taskTitles, taskDescriptions);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("adapter.getItem(position):" + adapter.getItem(position) + " position: " + position);

                Intent intent = new Intent(TaskActivity.this, TaskGenerator.class);

                intent.putExtra("DAY_NAME", dayName);
                intent.putExtra("TASK_TITLE", adapter.getItem(position));
                startActivity(intent);
                finish();
            }
        });
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

        dataSource = new TaskDataSource(this);
        try{
            dataSource.open();
        }catch(SQLException e){
            e.printStackTrace();
        }

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String taskDescription= ((TextView)info.targetView.findViewById(R.id.taskTit)).getText().toString();

        Toast.makeText(this,taskDescription,Toast.LENGTH_LONG).show();
        dataSource.deleteTask(taskDescription);

        if(item.getTitle().toString().equals("Delete")){
            Toast.makeText(this,"Deleted",Toast.LENGTH_LONG).show();
        }

        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra("DAY_NAME", dayName);
        startActivity(intent);

        finish();
        return true;
    }

    /*public void showTask(){

        dataSource = new TaskDataSource(this);
        try{
            dataSource.open();
        }catch(SQLException e){
            e.printStackTrace();
        }

        Task task=dataSource.createTask("Monday", "Tasked Created via SQLite", "Sample Task Description", "10:00 AM");
        Log.i("Anunay",task.toString());
        System.out.println(task.toString());
        Log.e("DaysList", "anunay1 " + (ListView) findViewById(R.id.listView));
        listView= (ListView)findViewById(R.id.listView);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.task_row, R.id.textView, data);
        listView.setAdapter(adapter);
    }*/

    public void temp(View view){
        Toast.makeText(this,"map clicked",Toast.LENGTH_LONG);
    }

    public void createTask(View view){
        Intent intent = new Intent(this, TaskGenerator.class);
        intent.putExtra("DAY_NAME", dayName);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("TaskActivity: in onStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("TaskActivity: in onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("TaskActivity: in onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("TaskActivity: in onStop()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("TaskActivity: in onResume()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("TaskActivity: in onDestroy()");
    }
}

