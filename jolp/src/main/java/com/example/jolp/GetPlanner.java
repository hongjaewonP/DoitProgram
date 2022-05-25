package com.example.jolp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetPlanner extends Activity {
    private EditText classnum, num;
    private String classnumber, number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_getplanner);

        classnum = findViewById(R.id.classnum);
        num = findViewById(R.id.num);

    }

    //확인 버튼 클릭
    public void DownloadPlan(View v){
        //데이터 전달하기
        //여기에 retrofit 통신 넣기
        //String a = classnum.getText().toString()+'-'+num.getText().toString();
        /*Retrofit retrofit = new Retrofit.Builder().baseUrl("ec2-3-145-163-1.us-east-2.compute.amazonaws.com")
                .addConverterFactory(GsonConverterFactory.create()).build(); */

        if((classnum.length() == 0) ||(num.length() == 0)){
            Toast.makeText(getApplicationContext(), "학수번호와 번호를 정확히 입력해주세요",Toast.LENGTH_SHORT).show();
        } else {
            classnumber = classnum.getText().toString();
            number = num.getText().toString();
            startDownload(new Planner(classnumber, number));
        }
        //액티비티(팝업) 닫기
        finish();
    }

    private void startDownload(Planner planner) {
        ServiceApi serviceApi = new ServiceApi() {
            @Override
            public Call<PlannerResponse> userPlan(Planner plan) {
                return null;
            }
        };
        serviceApi.userPlan(planner).enqueue(new Callback<PlannerResponse>() {
            @Override
            public void onResponse(Call<PlannerResponse> call, Response<PlannerResponse> response) {
                PlannerResponse result = response.body();
                //서버로부터의 응답을 위에서 정의한 JoinResponse객체에 담는다.
                Toast.makeText(GetPlanner.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                // getMessage를 통해 성공시 서버로부터 회원가입 성공이라는 메시지를 받음
                if(result.getStatus() == 200) {
                    finish();  //getStatus로 받아온 코드가 200(OK)면 회원가입 프래그먼트 종료
                }
            }

            @Override
            public void onFailure(Call<PlannerResponse> call, Throwable t) {
                Toast.makeText(GetPlanner.this, "강의계획서 다운로드 Error", Toast.LENGTH_SHORT).show();
                Log.e("강의계획서 다운로드 Error", t.getMessage());
                t.printStackTrace();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 클릭 시 창 닫기
        finish();
    }

}
