package com.example.jolp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event>
{
        public static int EDIT_REQUEST_MODE = 10;
    private static class ViewHolder {
        TextView eventCelltime;
        TextView eventCellname;
        ImageView eventCelledit;
    }

    public EventAdapter(@NonNull Context context, List<Event> events)
    {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        Event event = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_cell, parent, false);
            viewHolder.eventCellname = (TextView) convertView.findViewById(R.id.eventCellname);
            viewHolder.eventCelltime = (TextView) convertView.findViewById(R.id.eventCelltime);
            viewHolder.eventCelledit = (ImageView) convertView.findViewById(R.id.eventCelledit);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        /*
        TextView eventCelltime = convertView.findViewById(R.id.eventCelltime);
        TextView eventCellname = convertView.findViewById(R.id.eventCellname);
        ImageView eventEdit = (ImageView) convertView.findViewById(R.id.eventCelledit); */
        String eventTime = event.getstartTime()+"\n"+event.getPlace();
        //start_time && end_time로 바꾸기
        String eventTitle = event.getName();
        viewHolder.eventCellname.setText(eventTitle);
        viewHolder.eventCelltime.setText(eventTime);
        viewHolder.eventCelledit.setImageResource(R.drawable.edit);
        //이벤트 이름 클릭하면 일정 정보 보여줌
        viewHolder.eventCellname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //int position = (Integer) view.getTag();
                // Access the row position here to get the correct data item
                //Event event = getItem(position);
                Intent intent = new Intent(getContext(), ShowEvent.class);
                intent.putExtra("eventname", event.getName());
                intent.putExtra("date", event.getDate());
                intent.putExtra("place", event.getPlace());
                intent.putExtra("starttime", event.getstartTime());
                intent.putExtra("endtime", event.getendTime());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });
        viewHolder.eventCelledit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //int position = (Integer) view.getTag();
                // Access the row position here to get the correct data item
                //Event event = getItem(position);
                Intent intent = new Intent(getContext(), EventEditActivity.class);
                intent.putExtra("eventname", event.getName());
                intent.putExtra("date", event.getDate());
                intent.putExtra("place", event.getPlace());
                intent.putExtra("starttime", event.getstartTime());
                intent.putExtra("endtime", event.getendTime());
                //intent.putExtra("edit", "수정");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
                //((Activity)getContext()).startActivityForResult(intent, EDIT_REQUEST_MODE);
            }
        });
        /*eventCelltime.setText(eventTime);
        eventCellname.setText(eventTitle);
        eventEdit.setImageResource(R.drawable.edit);*/
        return convertView;
    }
}
