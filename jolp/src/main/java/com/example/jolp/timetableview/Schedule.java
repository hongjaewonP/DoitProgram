package com.example.jolp.timetableview;

import java.io.Serializable;

public class Schedule implements Serializable {
    static final int 월 = 0;
    static final int 화 = 1;
    static final int 수 = 2;
    static final int 목 = 3;
    static final int 금 = 4;
    static final int 토 = 5;
    static final int 일 = 6;

    String classTitle="";
    String classPlace="";
    String professorName="";
    private int day = 0;
    private Time startTime;
    private Time endTime;

    public Schedule() {
        this.startTime = new Time();
        this.endTime = new Time();
    }

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }

    public String getClassTitle() {
        return classTitle;
    }

    public void setClassTitle(String classTitle) {
        this.classTitle = classTitle;
    }

    public String getClassPlace() {
        return classPlace;
    }

    public void setClassPlace(String classPlace) {
        this.classPlace = classPlace;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }
}

