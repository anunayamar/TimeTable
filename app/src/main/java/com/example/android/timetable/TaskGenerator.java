package com.example.android.timetable;


import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import android.widget.ToggleButton;

import com.example.android.com.example.android.constant.Constants;
import com.example.android.db.Task;
import com.example.android.db.TaskDataSource;
import com.example.android.service.FetchAddressIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

import org.w3c.dom.Text;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskGenerator extends FragmentActivity implements ConnectionCallbacks, OnConnectionFailedListener {


    private TaskDataSource dataSource = null;
    private String dayName;
    private double latitude;
    private double longitude;

    private int PLACE_PICKER_REQUEST = 1;

    protected GoogleApiClient mGoogleApiClient;

    /**
     * Receiver registered with this activity to get the response from FetchAddressIntentService.
     */
    private AddressResultReceiver mResultReceiver;

    /**
     * The formatted location address.
     */
    protected String mAddressOutput;

    private String deleteTaskTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("TaskGenerator: in onCreate");

        Intent intent = getIntent();
        setContentView(R.layout.task_generator);

        CharSequence daySeq = intent.getCharSequenceExtra("DAY_NAME");

        if (daySeq != null) {
            dayName = daySeq.toString();
        }

        CharSequence taskSeq = intent.getCharSequenceExtra("TASK_TITLE");
        buildGoogleApiClient();
        mResultReceiver = new AddressResultReceiver(new Handler());

        if (taskSeq != null) {
            displayTask(taskSeq.toString());
        }

    }

    protected void startIntentService(double lat, double longit) {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(this, FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra("LATITUDE", lat);
        intent.putExtra("LONGITUDE", longit);

        startService(intent);
    }


    public void editTask(View view) {

        Button edit = (Button) findViewById(R.id.button3);
        edit.setVisibility(View.INVISIBLE);

        EditText eTTaskTitle = (EditText) findViewById(R.id.taskTitle);
        eTTaskTitle.setEnabled(true);

        EditText eTTaskDescription = (EditText) findViewById(R.id.taskDescription);
        eTTaskDescription.setEnabled(true);

        ToggleButton tglSunday = (ToggleButton) findViewById(R.id.sundayToggle);
        tglSunday.setEnabled(true);

        ToggleButton tglMonday = (ToggleButton) findViewById(R.id.mondayToggle);
        tglMonday.setEnabled(true);

        ToggleButton tglTuesday = (ToggleButton) findViewById(R.id.tuesdayToggle);
        tglTuesday.setEnabled(true);

        ToggleButton tglWednesday = (ToggleButton) findViewById(R.id.wednesdayToggle);
        tglWednesday.setEnabled(true);

        ToggleButton tglThursday = (ToggleButton) findViewById(R.id.thursdayToggle);
        tglThursday.setEnabled(true);

        ToggleButton tglFriday = (ToggleButton) findViewById(R.id.fridayToggle);
        tglFriday.setEnabled(true);

        ToggleButton tglSaturday = (ToggleButton) findViewById(R.id.saturdayToggle);
        tglSaturday.setEnabled(true);

        ImageView imgClickImage = (ImageView) findViewById(R.id.clickImage);
        imgClickImage.setEnabled(true);

        ImageView imgTaskLocation = (ImageView) findViewById(R.id.taskLocation);
        imgTaskLocation.setEnabled(true);

        Button btnSave = (Button) findViewById(R.id.button);
        btnSave.setVisibility(View.VISIBLE);

        Button btnCancel = (Button) findViewById(R.id.button2);
        btnCancel.setVisibility(View.VISIBLE);

        deleteTaskTitle = eTTaskTitle.getText().toString();


    }

    public void displayTask(String taskTitle) {

        EditText eTTaskTitle = (EditText) findViewById(R.id.taskTitle);
        eTTaskTitle.setEnabled(false);

        EditText eTTaskDescription = (EditText) findViewById(R.id.taskDescription);
        eTTaskDescription.setEnabled(false);

        ToggleButton tglSunday = (ToggleButton) findViewById(R.id.sundayToggle);
        tglSunday.setEnabled(false);

        ToggleButton tglMonday = (ToggleButton) findViewById(R.id.mondayToggle);
        tglMonday.setEnabled(false);

        ToggleButton tglTuesday = (ToggleButton) findViewById(R.id.tuesdayToggle);
        tglTuesday.setEnabled(false);

        ToggleButton tglWednesday = (ToggleButton) findViewById(R.id.wednesdayToggle);
        tglWednesday.setEnabled(false);

        ToggleButton tglThursday = (ToggleButton) findViewById(R.id.thursdayToggle);
        tglThursday.setEnabled(false);

        ToggleButton tglFriday = (ToggleButton) findViewById(R.id.fridayToggle);
        tglFriday.setEnabled(false);

        ToggleButton tglSaturday = (ToggleButton) findViewById(R.id.saturdayToggle);
        tglSaturday.setEnabled(false);

        ImageView imgClickImage = (ImageView) findViewById(R.id.clickImage);
        imgClickImage.setEnabled(false);

        ImageView imgTaskLocation = (ImageView) findViewById(R.id.taskLocation);
        imgTaskLocation.setEnabled(false);

        Button btnSave = (Button) findViewById(R.id.button);
        btnSave.setVisibility(View.INVISIBLE);

        Button edit = (Button) findViewById(R.id.button3);
        edit.setVisibility(View.VISIBLE);

        Button btnCancel = (Button) findViewById(R.id.button2);
        btnCancel.setVisibility(View.INVISIBLE);

        dataSource = new TaskDataSource(this);
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<Task> values = dataSource.getTask(dayName, taskTitle);


        for (Task task : values) {
            TextView timeLabel = (TextView) findViewById(R.id.timerlabel);
            timeLabel.setText(task.getTaskTime());

            EditText taskTitleEditText = (EditText) findViewById(R.id.taskTitle);
            taskTitleEditText.setText(taskTitle);

            EditText taskDescriptionEditText = (EditText) findViewById(R.id.taskDescription);
            taskDescriptionEditText.setText(task.getTaskDescription());


            TextView locLabel = (TextView) findViewById(R.id.locationLabel);
            locLabel.setText(task.getTaskAddress());
            System.out.println("DayName fetched :" + dayName);

            latitude = task.getLatitude();

            longitude = task.getLongitude();


            switch (dayName) {
                case "Monday":
                    tglMonday.setChecked(true);
                    break;
                case "Tuesday":
                    tglTuesday.setChecked(true);
                    break;
                case "Wednesday":
                    tglWednesday.setChecked(true);
                    break;
                case "Thursday":
                    tglThursday.setChecked(true);
                    break;
                case "Friday":
                    tglFriday.setChecked(true);
                    break;
                case "Saturday":
                    tglSaturday.setChecked(true);
                    break;
                case "Sunday":
                    tglSunday.setChecked(true);
                    break;
            }


        }


        dataSource.close();
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

            TaskGenerator taskGenerator = (TaskGenerator) getActivity();
            taskGenerator.updateTimerLabel(hourOfDay, minute, view.is24HourView());

        }
    }

    private void updateTimerLabel(int hourOfDay, int minute, boolean is24Hour) {

        TextView textView = (TextView) findViewById(R.id.timerlabel);

        textView.setTextSize(45);
        if (hourOfDay <= 12) {
            if(minute < 10){
                textView.setText(hourOfDay + ":0" + minute + " AM");
            }else{
                textView.setText(hourOfDay + ":" + minute + " AM");
            }

        } else {
            if(minute < 10){
                textView.setText(hourOfDay + ":0" + minute + " PM");
            }else{
                textView.setText(hourOfDay + ":" + minute + " PM");
            }

        }

    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");

    }

    public void saveTask(View view) {

        if (deleteTaskTitle != null) {

            dataSource = new TaskDataSource(this);
            try {
                dataSource.open();
            } catch (SQLException e) {
                e.printStackTrace();
            }


            dataSource.deleteTask(deleteTaskTitle, dayName);
            System.out.println("deleted for edition");

            dataSource.close();
        }

        EditText taskTitle = (EditText) findViewById(R.id.taskTitle);
        EditText taskDescription = (EditText) findViewById(R.id.taskDescription);
        TextView taskTimer = (TextView) findViewById(R.id.timerlabel);

        int[] dayId = {R.id.sundayToggle, R.id.mondayToggle, R.id.tuesdayToggle, R.id.wednesdayToggle, R.id.thursdayToggle, R.id.fridayToggle, R.id.saturdayToggle};

        Map<String, String> dayMap = new HashMap<>();
        dayMap.put("MON", "Monday");
        dayMap.put("TUE", "Tuesday");
        dayMap.put("WED", "Wednesday");
        dayMap.put("THURS", "Thursday");
        dayMap.put("FRI", "Friday");
        dayMap.put("SAT", "Saturday");
        dayMap.put("SUN", "Sunday");

        dataSource = new TaskDataSource(this);
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 7; i++) {

            System.out.println("Before save");
            ToggleButton dayToggle = (ToggleButton) findViewById(dayId[i]);

            System.out.println(taskTitle.getText() + " " + taskDescription.getText() + " " + taskTimer.getText() + " " + dayToggle.isChecked() + " " + dayToggle.getTextOn());

            if (dayToggle.isChecked() && mAddressOutput != null) {
                Task task = dataSource.createTask(dayMap.get(dayToggle.getTextOn().toString()), taskTitle.getText().toString(),
                        taskDescription.getText().toString(), taskTimer.getText().toString(), latitude, longitude, mAddressOutput);

                System.out.println("Saving task:" + task);
            } else if (dayToggle.isChecked()) {
                TextView textView = (TextView) findViewById(R.id.locationLabel);


                Task task = dataSource.createTask(dayMap.get(dayToggle.getTextOn().toString()), taskTitle.getText().toString(),
                        taskDescription.getText().toString(), taskTimer.getText().toString(), latitude, longitude, textView.getText().toString());

                System.out.println("Saving task:" + task);
            }
        }
        dataSource.close();

        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra("DAY_NAME", dayName);
        startActivity(intent);

        finish();

    }

    public void showMap(View view) {
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

                LatLng latLng = place.getLatLng();

                this.latitude = latLng.latitude;
                this.longitude = latLng.longitude;


                startIntentService(this.latitude, this.longitude);

            }
        }
    }

    public void updateLocationLabel() {

        TextView textView = (TextView) findViewById(R.id.locationLabel);

        textView.setTextSize(18);
        textView.setText(mAddressOutput);

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

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i("TaskGenerator", "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i("TaskGenerator", "Connection suspended");
        mGoogleApiClient.connect();
    }


    /**
     * Receiver for data sent from FetchAddressIntentService.
     */
    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         * Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.
            mAddressOutput = resultData.getString("RESULT_DATA_KEY");

            System.out.println("Address found:" + mAddressOutput);
            updateLocationLabel();

            //Fetch pin, the address which is coming now and the getAdminArea

            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                System.out.println("Address success");
            }

        }
    }

    public void cancelTask(View view) {
        finish();
    }
}
