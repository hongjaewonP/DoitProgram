package com.example.jolp;

import com.google.gson.annotations.SerializedName;

//응답 클래스 - 강의계획서 json
public class PlannerResponse {
    @SerializedName("status")
    private int status;

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    public class Data{
        @SerializedName("userId")
        private int userId;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
