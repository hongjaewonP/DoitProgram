package com.example.jolp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ServiceApi {
    @Headers({"Content-Type: application/json"})
    @GET("3.145.163.1:8000/class_info/")
    //Call<응답클래스이름> userJoin(@Body 보낼 클래스데이터 이름);
    Call<PlannerResponse> userPlan(@Body Planner plan);
}
