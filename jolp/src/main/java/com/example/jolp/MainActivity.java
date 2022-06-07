package com.example.jolp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jolp.EventEditActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.jolp.UserData;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.util.JsonMapper;
import com.google.firebase.firestore.auth.User;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static com.example.jolp.CalendarUtils.daysInMonthArray;
import static com.example.jolp.CalendarUtils.monthYearFromDate;
import static com.example.jolp.Event.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, CalendarAdapter.OnItemListener {

    private TextView monthYearText;
    private TextView iv_name, iv_info;
    private ListView eventListView;
    private RecyclerView calendarRecyclerView;
    private FirebaseAuth mAuth;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private ArrayList<Event> eventlistt;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // 왼쪽 상단 버튼 만들기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //왼쪽 상단 버튼 아이콘 지정
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_24);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this); // 리스너 설정
        initWidgets();
        CalendarUtils.selectedDate = LocalDate.now();
        setMonthView();
        mAuth = FirebaseAuth.getInstance();
        View header = navigationView.getHeaderView(0);
        iv_name = (TextView) header.findViewById(R.id.tv_name);
        iv_info = (TextView) header.findViewById(R.id.tv_info);
        setUserProfile();
        //sharedpreference
    }


    @Override
    public void onBackPressed() {
        //뒤로가기 했을 때
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:{
                // 왼쪽 상단 버튼 눌렀을 때
            }
            case R.id.logout:{
                if(mAuth.getCurrentUser() != null){
                    mAuth.signOut();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }
    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        eventListView = findViewById(R.id.eventListView);
    }

    private void setMonthView()
    {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(CalendarUtils.selectedDate);
        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        onAdapter();
    }

    private void setEventAdapter()
    {
        String date = CalendarUtils.formattedDate(CalendarUtils.selectedDate);
        ArrayList<Event> dailyEvents = eventsForDate(CalendarUtils.selectedDate);
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), dailyEvents);
        eventListView.setAdapter(eventAdapter);
    }

    /* 기존에서 sharedpreferences를 사용했는데 변경
    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(eventsList);
        editor.putString("event list", json);
        editor.apply();
    } */

    private ArrayList<Event> loadData() {
        ArrayList<Event> eventArrayList = new ArrayList<Event>();
        /*
        SharedPreferences sp = getSharedPreferences("shared", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString("event list", null);
        Type type = new TypeToken<ArrayList<Event>>() {}.getType();
        eventArrayList = gson.fromJson(json, type);
        */
        File file = new File(this.getFilesDir(),"event list.json");
        FileReader fileReader = null;
        String files = "";
        try {
            files = EventEditActivity.getStringFromFile(file);
            JSONArray arra = new JSONArray(files);
            for (int i=0; i<arra.length(); i++) {
                JSONObject j = arra.getJSONObject(i);
                String name = j.getString("name");
                String date = j.getString("date");
                String place = j.getString("place");
                String start = j.getString("starttime");
                String end = j.getString("endtime");
                Event news = new Event(name, date, place, start, end);
                eventArrayList.add(news);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return eventArrayList;
    }

    public static ArrayList<Event> eventForDate(ArrayList<Event> eventlist, LocalDate date)
    {
        /*
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM월 dd일");
        LocalDate parsedate = LocalDate.parse(date, formatter); */
        ArrayList<Event> events = new ArrayList<>();

        for(Event event : eventlist)
        {
            String gevent = event.getDate();
            //event.getLocalDate(gevent).equals(date)
            if(event.getLocalDate(gevent).equals(date))
                events.add(event);
        }

        return events;
    }

    @Override
    protected void onStart() {
        super.onStart();
        eventlistt = loadData();
        String date = CalendarUtils.formattedDate(CalendarUtils.selectedDate);
        ArrayList<Event> events = eventForDate(eventlistt, CalendarUtils.selectedDate);
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), events);
        eventListView.setAdapter(eventAdapter);
    }

    protected void onAdapter() {
        eventlistt = loadData();
        if(eventlistt == null){
            setEventAdapter();
        }
        else{
            //String date = CalendarUtils.formattedDate(CalendarUtils.selectedDate);
            ArrayList<Event> events = eventForDate(eventlistt, CalendarUtils.selectedDate);
            EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), events);
            eventListView.setAdapter(eventAdapter);
        }
    }

    protected void setUserProfile() {
        String email = mAuth.getCurrentUser().getEmail();
        firebaseDatabase = FirebaseDatabase.getInstance("https://jolp-a5446-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference().child("UserData");
        databaseReference.child(mAuth.getCurrentUser().getUid()).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            UserData user = snapshot.getValue(UserData.class);
                            iv_name.setText(user.getUserName());
                            iv_info.setText(email);
                        }
                    }

                    //db 에러 확인
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        throw error.toException();
                    }
                });
    }

    public void previousMonthAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, LocalDate date)
    {
        if(date != null)
        {
            CalendarUtils.selectedDate = date;
            setMonthView();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav1) {
            startActivity(new Intent(this, WeekViewActivity.class));
            finish();
        } else if (id == R.id.nav2) {
            startActivity(new Intent(this, UploadActivity.class));
            finish();
        } else if (id == R.id.nav3) {
            String a = mAuth.getCurrentUser().getDisplayName();
            Toast.makeText(getApplicationContext(),a, Toast.LENGTH_SHORT).show();
        }
        else if(id == R.id.nav4) {
            startActivity(new Intent(this, FeedBack.class));
            finish();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}