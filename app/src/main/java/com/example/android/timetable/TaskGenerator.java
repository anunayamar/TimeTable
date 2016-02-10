package com.example.android.timetable;


import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.example.android.db.Task;
import com.example.android.db.TaskDataSource;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TaskGenerator extends FragmentActivity {

    private TaskDataSource dataSource = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_generator);
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            System.out.println("Anunay1");
            TaskGenerator taskGenerator=(TaskGenerator)getActivity();
            taskGenerator.updateTimerLabel(hourOfDay, minute, view.is24HourView());

            System.out.println("Anunay2");
            //dismiss();
        }
    }

    private void updateTimerLabel(int hourOfDay, int minute, boolean is24Hour){

        TextView textView=(TextView)findViewById(R.id.timerlabel);
        textView.setTextSize(45);
        if(hourOfDay<=12) {
            textView.setText(hourOfDay + ":" + minute+" AM");
        }else{
            textView.setText(hourOfDay + ":" + minute+" PM");
        }

    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");

    }

    public void saveTask(View view){
        EditText taskTitle = (EditText) findViewById(R.id.taskTitle);
        EditText taskDescription = (EditText) findViewById(R.id.taskDescription);
        TextView taskTimer=(TextView) findViewById(R.id.timerlabel);
        int []dayId= {R.id.sundayToggle, R.id.mondayToggle, R.id.tuesdayToggle, R.id.wednesdayToggle, R.id.thursdayToggle, R.id.fridayToggle, R.id.saturdayToggle};

        Map<String, String> dayMap = new HashMap<>();
        dayMap.put("MON","Monday");
        dayMap.put("TUE","Tuesday");
        dayMap.put("WED","Wednesday");
        dayMap.put("THURS","Thursday");
        dayMap.put("FRI","Friday");
        dayMap.put("SAT","Saturday");
        dayMap.put("SUN","Sunday");

        dataSource = new TaskDataSource(this);
        try{
            dataSource.open();
        }catch(SQLException e){
            e.printStackTrace();
        }

        for(int i=0;i<7;i++){
            ToggleButton dayToggle = (ToggleButton)findViewById(dayId[i]);

            System.out.println(taskTitle.getText()+" "+taskDescription.getText()+" "+taskTimer.getText()+" "+dayToggle.isChecked()+" "+dayToggle.getTextOn());

            if(dayToggle.isChecked()){
                Task task = dataSource.createTask(dayMap.get(dayToggle.getTextOn().toString()), taskTitle.getText().toString(),
                        taskDescription.getText().toString(), taskTimer.getText().toString());

                System.out.println(task);
            }
        }
        dataSource.close();

    }
}
