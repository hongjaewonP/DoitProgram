package com.example.jolp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.time.LocalTime;

public class EventEditActivity extends AppCompatActivity
{
    private EditText eventNameET;
    private TextView eventDateTV;
    private EditText eventTime, eventTime2;
    private String starttime, endtime, aop;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        initWidgets();
        eventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(EventEditActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if(hourOfDay >= 12) {
                            aop = " PM";
                            hourOfDay -= 12;
                        }
                        else aop = " AM";
                        if(minutes < 10)
                            starttime = hourOfDay + ":" + "0" + minutes + aop;
                        else
                            starttime = hourOfDay + ":" + minutes + aop;
                        eventTime.setText(starttime);
                    }
                }, 0, 0, false);
                timePickerDialog.show();
            }
        });
        eventTime2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(EventEditActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if(hourOfDay >= 12) {
                            aop = " PM";
                            hourOfDay -= 12;
                        }
                        else aop = " AM";
                        if(minutes < 10)
                            endtime = hourOfDay + ":" + "0" + minutes + aop;
                        else
                            endtime = hourOfDay + ":" + minutes + aop;
                        eventTime2.setText(endtime);
                    }
                }, 0, 0, false);
                timePickerDialog.show();
            }
        });
        eventDateTV.setText("Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));
    }

    private void initWidgets()
    {
        eventNameET = findViewById(R.id.eventNameET);
        eventDateTV = findViewById(R.id.eventDateTV);
        eventTime = findViewById(R.id.eventTimeTV);
        eventTime2 = findViewById(R.id.eventTimeTV2);
    }

    public void saveEventAction(View view)
    {
        String eventName = eventNameET.getText().toString();
        Event newEvent = new Event(eventName, CalendarUtils.selectedDate, starttime, endtime);
        Event.eventsList.add(newEvent);
        finish();
    }
}
