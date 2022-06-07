package com.example.jolp;

import static com.example.jolp.CalendarUtils.daysInWeekArray;
import static com.example.jolp.CalendarUtils.monthYearFromDate;
import static com.example.jolp.CalendarUtils.daysInMonthArray;


import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;


public class WeekViewActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener, NavigationView.OnNavigationItemSelectedListener {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;
    private Toolbar toolbar;
    private FloatingActionButton fab, fabSearch, fabEdit, fabChat;
    private boolean fabstatus = false;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private TextView iv_name, iv_info;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);
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
        View header = navigationView.getHeaderView(0);
        iv_name = (TextView) header.findViewById(R.id.tv_name);
        iv_info = (TextView) header.findViewById(R.id.tv_info);
        setUserProfile();
        initWidgets();
        setWeekView();
        fabSearch.setVisibility(View.INVISIBLE);
        fabEdit.setVisibility(View.INVISIBLE);
        fabChat.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFab();
            }
        });
        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(WeekViewActivity.this, GetPlanner.class);
                startActivity(i);
                //Toast.makeText(getApplicationContext(), "검색",Toast.LENGTH_SHORT).show();
                /* 여기에 SearchView 띄워서 retrofit으로 서버와 통신 */

            }
        });
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WeekViewActivity.this, EventEditActivity.class));
            }
        });
        fabChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //chatbot 화면 띄우기
                Toast.makeText(getApplicationContext(), "챗봇",Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        eventListView = findViewById(R.id.eventListView);
        fab = findViewById(R.id.fab);
        fabSearch = findViewById(R.id.fabSearch);
        fabEdit = findViewById(R.id.fabEdit);
        fabChat = findViewById(R.id.fabChat);

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
                    startActivity(new Intent(WeekViewActivity.this, LoginActivity.class));
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

    @Override
    protected void onStart() {
        super.onStart();
        ArrayList<Event> eventlistt = loadData();
        String date = CalendarUtils.formattedDate(CalendarUtils.selectedDate);
        ArrayList<Event> events = eventForDate(eventlistt, CalendarUtils.selectedDate);
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), events);
        eventListView.setAdapter(eventAdapter);
    }

    protected void setUserProfile() {
        mAuth = FirebaseAuth.getInstance();
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

    private void setWeekView()
    {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);
        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        onWeekAdapter();
    }

    private ArrayList<Event> loadData() {
        ArrayList<Event> eventArrayList = new ArrayList<Event>();
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

    public static ArrayList<Event> eventForDate(ArrayList<Event> eventlist, LocalDate date) {
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

    protected void onWeekAdapter() {
        ArrayList<Event> eventlistt = loadData();
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

    public void previousWeekAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    public void nextWeekAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav1) {
            startActivity(new Intent(this, MainActivity.class));
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

    @Override
    public void onItemClick(int position, LocalDate date)
    {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        onWeekAdapter();
    }

    private void setEventAdapter()
    {
        ArrayList<Event> dailyEvents = Event.eventsForDate(CalendarUtils.selectedDate);
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), dailyEvents);
        eventListView.setAdapter(eventAdapter);
    }

    public void newEventAction(View view)
    {
        startActivity(new Intent(this, EventEditActivity.class));
    }

    public void monthlyAction(View view)
    {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void toggleFab() {
        if(fabstatus) {
            // 플로팅 액션 버튼 닫기
            // 애니메이션 추가
            /*
            ObjectAnimator fs_animation = ObjectAnimator.ofFloat(fabSearch, "translationY", 0f);
            fs_animation.start();
            ObjectAnimator fe_animation = ObjectAnimator.ofFloat(fabEdit, "translationY", 0f);
            fe_animation.start();
            ObjectAnimator fc_animation = ObjectAnimator.ofFloat(fabChat, "translationY", 0f);
            fc_animation.start(); */
            fabSearch.setVisibility(View.INVISIBLE);
            fabEdit.setVisibility(View.INVISIBLE);
            fabChat.setVisibility(View.INVISIBLE);
            // 메인 플로팅 이미지 변경
            fab.setImageResource(R.drawable.week2);

        }else {
            // 플로팅 액션 버튼 열기
            /*
            ObjectAnimator fs_animation = ObjectAnimator.ofFloat(fabSearch, "translationY", -600f);
            fs_animation.start();
            ObjectAnimator fe_animation = ObjectAnimator.ofFloat(fabEdit, "translationY", -400f);
            fe_animation.start();
            ObjectAnimator fc_animation = ObjectAnimator.ofFloat(fabChat, "translationY", -200f);
            fc_animation.start(); */
            fabSearch.setVisibility(View.VISIBLE);
            fabEdit.setVisibility(View.VISIBLE);
            fabChat.setVisibility(View.VISIBLE);
            // 메인 플로팅 이미지 변경
            fab.setImageResource(R.drawable.ic_baseline_close_24);
        }
        // 플로팅 버튼 상태 변경
        fabstatus = !fabstatus;
    }

}
