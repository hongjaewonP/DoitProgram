#navigation header의 유저 정보가 바뀌질 않아서 해결하면 코드 수정해서 올리겠습니다
#수정할 부분 - setUserProfile()
package com.example.jolp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.time.LocalDate;
import java.util.ArrayList;

import static com.example.jolp.CalendarUtils.daysInMonthArray;
import static com.example.jolp.CalendarUtils.monthYearFromDate;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, CalendarAdapter.OnItemListener {

    private TextView monthYearText;
    private TextView iv_name, iv_info;
    private ListView eventListView;
    private RecyclerView calendarRecyclerView;
    private FirebaseAuth mAuth;
    private DrawerLayout drawer;
    Toolbar toolbar;
    private NavigationView navigationView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

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
        setUserProfile();
        ImageButton imageButton = (ImageButton) findViewById(R.id.uploadbtn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UploadActivity.class);
                startActivity(intent);
            }
        });

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
        setEventAdapter();
    }

    private void setEventAdapter()
    {
        ArrayList<Event> dailyEvents = Event.eventsForDate(CalendarUtils.selectedDate);
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), dailyEvents);
        eventListView.setAdapter(eventAdapter);
    }

    private void setUserProfile() {
        String email = mAuth.getCurrentUser().getEmail();
        firebaseDatabase = FirebaseDatabase.getInstance("https://jolp-a5446-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference().child("UserData");
   //     Query query = databaseReference.orderByChild("userID").equalTo(email);
        databaseReference.orderByChild("userID").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            UserData user = snapshot.getValue(UserData.class);

                            View header = navigationView.getHeaderView(0);
                            iv_name = (TextView) header.findViewById(R.id.tv_name);
                            iv_info = (TextView) header.findViewById(R.id.tv_info);
                            String name = user.getUserName();
                        //    String email = mAuth.getCurrentUser().getEmail();
                            iv_name.setText(name);
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
            Toast.makeText(getApplicationContext(),"setting activity", Toast.LENGTH_SHORT).show();
        }
        else if(id == R.id.nav4) {
            Toast.makeText(getApplicationContext(),"Feedback activity", Toast.LENGTH_SHORT).show();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
