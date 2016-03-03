package com.example.android.timetable;


import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.android.db.Task;
import com.example.android.db.TaskDataSource;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TaskGenerator extends FragmentActivity {

    private TaskDataSource dataSource = null;
    private String dayName;
    private double latitude;
    private double longitude;

    private  int PLACE_PICKER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("TaskGenerator: in onCreate");

        Intent intent=getIntent();
        setContentView(R.layout.task_generator);
        dayName=intent.getCharSequenceExtra("DAY_NAME").toString();
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
                        taskDescription.getText().toString(), taskTimer.getText().toString(), latitude, longitude);

                System.out.println(task);
            }
        }
        dataSource.close();

        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra("DAY_NAME",dayName);
        startActivity(intent);

        finish();

    }

    public void showMap(View view){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);

                String toastMsg = String.format("Place: %s", place.getName());
                LatLng latLng=place.getLatLng();

                Toast.makeText(this, latLng.toString(), Toast.LENGTH_LONG).show();
                updateLocationLabel(latLng.latitude,latLng.longitude);
            }
        }
    }

    public void updateLocationLabel(double latitude, double longitude){

        TextView textView=(TextView)findViewById(R.id.locationLabel);

        textView.setTextSize(25);
        textView.setText("LATITUDE: " + latitude + "\nLONGITUDE: " + longitude);
        this.latitude=latitude;
        this.longitude=longitude;
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("TaskGenerator: in onStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("TaskGenerator: in onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("TaskGenerator: in onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("TaskGenerator: in onStop()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("TaskGenerator: in onResume()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("TaskGenerator: in onDestroy()");
    }
}
