package com.example.android.timetable;


import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class TaskGenerator extends FragmentActivity {

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
        newFragment.show(getFragmentManager(),"timePicker");

    }
}
