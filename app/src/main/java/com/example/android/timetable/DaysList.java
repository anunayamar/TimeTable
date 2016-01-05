package com.example.android.timetable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Anunay on 8/11/15.
 */
public class DaysList extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, TaskActivity.class);

        TextView textView = (TextView) view;
        textView.getText();

        intent.putExtra("DAY_NAME", textView.getText());

        startActivity(intent);
    }

}


