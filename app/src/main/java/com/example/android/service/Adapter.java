package com.example.android.service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.timetable.R;

/**
 * Created by Anunay on 15/2/16.
 */
public class Adapter extends ArrayAdapter<String> {

    Context context;
    String[] taskTitles;
    String[] taskDescriptions;

    public Adapter(Context context, String[] taskTitles, String[] taskDescriptions) {
        super(context, R.layout.task_row, R.id.taskTitle, taskTitles);
        this.context=context;
        this.taskTitles=taskTitles;
        this.taskDescriptions=taskDescriptions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row=inflater.inflate(R.layout.task_row, parent, false);

        TextView taskTitle= (TextView)row.findViewById(R.id.taskTit);
        TextView taskDescription=(TextView)row.findViewById(R.id.taskDesc);

        taskTitle.setText(taskTitles[position]);
        taskDescription.setText(taskDescriptions[position]);

        return row;
    }

    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }
}
