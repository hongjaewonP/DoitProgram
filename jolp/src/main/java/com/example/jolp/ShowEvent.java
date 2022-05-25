package com.example.jolp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ShowEvent extends Activity {
    private String name, dat, place, start, end;
    private TextView eventNameET, eventDateTV, placetv, eventTime, eventTime2;
    private TextView selectEventDate;
    private ImageView close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show);
        Intent i = getIntent();
        name = i.getStringExtra("eventname");
        dat = i.getStringExtra("date");
        place = i.getStringExtra("place");
        start = i.getStringExtra("starttime");
        end = i.getStringExtra("endtime");
        initWidgets();
        eventNameET.setText(name);
        selectEventDate.setText(dat);
        placetv.setText(place);
        eventTime.setText(start);
        eventTime2.setText(end);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initWidgets()
    {
        eventNameET = findViewById(R.id.eventNameET);
        eventDateTV = findViewById(R.id.eventDateTV);
        eventTime = findViewById(R.id.eventTimeTV);
        selectEventDate = findViewById(R.id.selectEventDate);
        eventTime2 = findViewById(R.id.eventTimeTV2);
        placetv = findViewById(R.id.placetv);
        close = findViewById(R.id.close);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 클릭 시 창 닫기
        finish();
    }

}