package com.example.jolp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event>
{
    public EventAdapter(@NonNull Context context, List<Event> events)
    {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        Event event = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_cell, parent, false);

        TextView eventCelltime = convertView.findViewById(R.id.eventCelltime);
        TextView eventCellname = convertView.findViewById(R.id.eventCellname);
        String eventTime = event.getstartTime()+"\n"+event.getendTime();
        //start_time && end_time로 바꾸기
        String eventTitle = event.getName();
        eventCelltime.setText(eventTime);
        eventCellname.setText(eventTitle);
        return convertView;
    }
}
