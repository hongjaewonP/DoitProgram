package com.example.jolp;

import com.google.gson.annotations.SerializedName;

public class Planner {
    @SerializedName("classnum")
    private String classnum;

    @SerializedName("num")
    private String num;

    public Planner(String classnum, String num) {
        this.classnum=classnum;
        this.num=num;
    }
}
