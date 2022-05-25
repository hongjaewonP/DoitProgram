package com.example.jolp;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
//import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@IgnoreExtraProperties
public class Event
{
    @SerializedName("name")
    private String name;
    @SerializedName("date")
    private String date;
    @SerializedName("place")
    private String place;
    @SerializedName("starttime")
    private String starttime;
    @SerializedName("endtime")
    private String endtime;

    public static ArrayList<Event> eventsList = new ArrayList<>();

    public ArrayList<Event> getEventsList(){
        return eventsList;
    }

    public static ArrayList<Event> eventsForDate(LocalDate date)
    {
        /*
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM월 dd일");
        LocalDate parsedate = LocalDate.parse(date, formatter); */
        ArrayList<Event> events = new ArrayList<>();

        for(Event event : eventsList)
        {
            String gevent = event.getDate();
            if(event.getLocalDate(gevent).equals(date))
                events.add(event);
        }

        return events;
    }

    public Event(){

    }

    public Event(String name, String date, String place, String starttime, String endtime)
    {
        this.name = name;
        this.date = date;
        this.place = place;
        this.starttime = starttime;
        this.endtime = endtime;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPlace()
    {
        return place;
    }

    public void setPlace(String place)
    {
        this.place = place;
    }

    public String getDate(){
        return date;
    }

    public static LocalDate getLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM월 dd일");
        LocalDate parsedate = LocalDate.parse(date, formatter);
        return parsedate;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getstartTime()
    {
        return starttime;
    }

    public void setstartTime(String starttime) { this.starttime = starttime; }

    public void setendTime(String endtime) { this.endtime = endtime; }

    public String getendTime() { return endtime; }
}
