package com.example.jolp;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Event
{
    public static ArrayList<Event> eventsList = new ArrayList<>();

    public static ArrayList<Event> eventsForDate(LocalDate date)
    {
        ArrayList<Event> events = new ArrayList<>();

        for(Event event : eventsList)
        {
            if(event.getDate().equals(date))
                events.add(event);
        }

        return events;
    }


    private String name;
    private LocalDate date;
    private String starttime;
    private String endtime;

    public Event(String name, LocalDate date, String starttime, String endtime)
    {
        this.name = name;
        this.date = date;
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

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
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
