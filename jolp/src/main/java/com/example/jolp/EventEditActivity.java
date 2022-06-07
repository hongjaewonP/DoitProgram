package com.example.jolp;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.PackageManagerCompat;

import org.json.*;
import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import com.google.gson.*;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.jolp.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Tag;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;

public class EventEditActivity extends AppCompatActivity
{
    private EditText eventNameET, selectEventDate, placetv;
    private TextView eventDateTV;
    private EditText eventTime, eventTime2, eventPlace;
    private String starttime, endtime, aop, eventDate;
    private LocalDate eventdate;
    private FirebaseDatabase firebasedatabase;
    private DatabaseReference reference;
    private String event;
    private FirebaseAuth mAuth;
    private Button savebtn, deletebtn;
    private String id;
    public JSONObject jj;
    private String name, dat, pl, start, end;
    public static int EDIT_MODE_OK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        initWidgets();
        mAuth = FirebaseAuth.getInstance();
        firebasedatabase = FirebaseDatabase.getInstance("https://jolp-a5446-default-rtdb.asia-southeast1.firebasedatabase.app/");
        reference = firebasedatabase.getReference("UserData");
        event = eventNameET.getText().toString();
        eventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(EventEditActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay > 12) {
                            aop = " PM";
                            hourOfDay -= 12;
                        }
                        else if(hourOfDay == 12)
                            aop = " PM";
                        else aop = " AM";
                        if (minutes < 10)
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
                        if (hourOfDay > 12) {
                            aop = " PM";
                            hourOfDay -= 12;
                        }
                        else if(hourOfDay == 12)
                            aop = " PM";
                        else aop = " AM";
                        if (minutes < 10)
                            endtime = hourOfDay + ":" + "0" + minutes + aop;
                        else
                            endtime = hourOfDay + ":" + minutes + aop;
                        eventTime2.setText(endtime);
                    }
                }, 0, 0, false);
                timePickerDialog.show();
            }
        });
        Gson gson = new GsonBuilder().create();
        eventDate = CalendarUtils.formattedDate(CalendarUtils.selectedDate);
        selectEventDate.setText(eventDate);
        id = mAuth.getCurrentUser().getUid();
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = savebtn.getText().toString();
                if(text.equals("저장")) {
                    try {
                        saveEventAction(view);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else if(text.equals("수정")){
                    try {
                        editEventAction(view, name, dat, pl);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //saveUserEvent(reference, id, event, eventDate, starttime, endtime);
            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    cancelEventAction(view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //EventAdapter class에서 불러올 경우
        if(new Intent(this.getIntent()).getFlags() == FLAG_ACTIVITY_NEW_TASK) {
            Intent i = new Intent(this.getIntent());

            name = i.getStringExtra("eventname");
            dat = i.getStringExtra("date");
            pl = i.getStringExtra("place");
            start = i.getStringExtra("starttime");
            end = i.getStringExtra("endtime");
            initWidgets();
            eventNameET.setText(name);
            selectEventDate.setText(dat);
            placetv.setText(pl);
            savebtn.setText("수정");
            eventTime.setText(start);
            eventTime2.setText(end);
        }
    }

    private void initWidgets()
    {
        eventNameET = findViewById(R.id.eventNameET);
        eventDateTV = findViewById(R.id.eventDateTV);
        eventTime = findViewById(R.id.eventTimeTV);
        eventTime2 = findViewById(R.id.eventTimeTV2);
        savebtn = findViewById(R.id.savebtn);
        deletebtn = findViewById(R.id.deletebtn);
        selectEventDate = findViewById(R.id.selectEventDate);
        placetv = findViewById(R.id.placetv);
    }

    /*
    public void saveUserEvent(@NonNull DatabaseReference reference, String id, String eventnm
            /*     String description , String date,
                              String start, String end) {
        Event event = new Event(eventnm, date, start, end);
        reference.child(id).child("EventList").child(eventnm).setValue(event);
         /*       .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //DB에 저장 성공시
                if(task.isSuccessful()) {
                    Toast.makeText(EventEditActivity.this, "DB 저장 완료", Toast.LENGTH_SHORT).show();
                    //             Log.d("dbTAG", "DB 저장 완료");
                }
                else {
                    Toast.makeText(EventEditActivity.this, "DB에 저장되지 않았습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    } */

    protected void saveEventAction(View view) throws Exception {
        ArrayList<Event> eventArrayList;
        String eventName = eventNameET.getText().toString();
        String eventday = selectEventDate.getText().toString();
        String place = placetv.getText().toString();
        String starts = eventTime.getText().toString();
        String ends = eventTime2.getText().toString();
        //eventDate = CalendarUtils.formattedDate(CalendarUtils.selectedDate);
        Event newEvent = new Event(eventName, eventday, place, starts, ends);
        Event.eventsList.add(newEvent);

        /*
        SharedPreferences sp = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Event>>() {}.getType();
        eventArrayList = gson.fromJson("event list", type);
        eventArrayList.add(newEvent);
        String json = gson.toJson(eventArrayList);
        editor.putString("event list", json);
        editor.apply(); */
        /*
        Gson gson = new GsonBuilder().create();
        // JSON 으로 변환
        String json = gson.toJson(newEvent, Event.class);
        */
        // Define the File Path and its Name
        File file = new File(this.getFilesDir(),"event list.json");
        FileWriter fileWriter = null;
        JSONArray jArray = new JSONArray();
        try {
            if (!file.exists()) {
                file.createNewFile();
                JSONObject sObject = new JSONObject();
                //배열 내에 들어갈 json
                sObject.put("name", newEvent.getName());
                sObject.put("date", newEvent.getDate());
                sObject.put("place", newEvent.getPlace());
                sObject.put("starttime", newEvent.getstartTime());
                sObject.put("endtime", newEvent.getendTime());
                jArray.put(sObject);
            }
            else{
                String filestr = getStringFromFile(file);
                JSONArray arr = new JSONArray(filestr);
                JSONObject sObject = new JSONObject();
                //배열 내에 들어갈 json
                sObject.put("name", newEvent.getName());
                sObject.put("date", newEvent.getDate());
                sObject.put("place", newEvent.getPlace());
                sObject.put("starttime", newEvent.getstartTime());
                sObject.put("endtime", newEvent.getendTime());
                arr.put(sObject);
                jArray = arr;
            }
            String json = jArray.toString();
            fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(json);
            bufferedWriter.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        /*
        SharedPreferences sp = getSharedPreferences("shared", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("event list", json); // JSON으로 변환한 객체를 저장한다.
        editor.commit(); //완료 */
        finish();
    }

    protected void cancelEventAction(View view){
        finish();
    }

    protected void editEventAction(View view, String nn1, String dd1,String pp1) throws Exception{
        File file = new File(this.getFilesDir(),"event list.json");
        FileWriter fileWriter = null;
        String eventName = eventNameET.getText().toString();
        String day = selectEventDate.getText().toString();
        String place = placetv.getText().toString();
        String st = eventTime.getText().toString();
        String ed = eventTime2.getText().toString();
        eventDate = CalendarUtils.formattedDate(CalendarUtils.selectedDate);
        Event newev = new Event(eventName, day, place, st, ed);
        try {
            if (!file.exists()) {
                Log.e("File not exist", "저장된 일정이 없습니다.");
            }
            else {
                String filestr = getStringFromFile(file);
                JSONArray arr = new JSONArray(filestr);
                for(int i=0;i<arr.length();i++){
                    JSONObject sObject = arr.getJSONObject(i);
                    String n1 = sObject.getString("name");
                    String d1 = sObject.getString("date");
                    String p1 = sObject.getString("place");
                    if((n1.equals(nn1)) && (d1.equals(dd1)) && (p1.equals(pp1))){
                        arr.remove(i);
                        JSONObject so = new JSONObject();
                        so.put("name", newev.getName());
                        so.put("date", newev.getDate());
                        so.put("place", newev.getPlace());
                        so.put("starttime", newev.getstartTime());
                        so.put("endtime", newev.getendTime());
                        arr.put(so);
                    }
                }
                String json = arr.toString();
                fileWriter = new FileWriter(file);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(json);
                bufferedWriter.close();
            }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        finish();
    }

    public static String getStringFromFile(File file) throws Exception{
        FileInputStream fin = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fin));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while((line = reader.readLine()) != null){
            sb.append(line).append("\n");
        }
        String filest = sb.toString();
        fin.close();
        return filest;
    }

}
