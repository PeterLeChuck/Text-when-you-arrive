package com.example.peter.SendSMSFromLocationAlarm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Peter on 8.3.2015.
 */
public class MainActivityAdapter extends BaseAdapter{
    ArrayList<AlarmItem> alarmsList = new ArrayList<>();
    private final Context mContext;
    LayoutInflater mInflater;

    public MainActivityAdapter(Context context) {

        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void add(AlarmItem item) {

        alarmsList.add(item);
        notifyDataSetChanged();

    }
    public void clear(int position) {
        alarmsList.get(position).cancelIntent();
        alarmsList.remove(position);
        notifyDataSetChanged();

    }
    @Override
    public int getCount() {
        return alarmsList.size();
    }

    @Override
    public Object getItem(int position) {
        return alarmsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the current ToDoItem
        final AlarmItem alarmItem = (AlarmItem)getItem(position);

        // Inflate the View for this ToDoItem
        // from todo_item.xml
        LinearLayout itemLayout = (LinearLayout) mInflater.inflate(R.layout.alarm_item, parent, false);



        //  Fill in specific ToDoItem data
        // Remember that the data that goes in this View
        // corresponds to the user interface elements defined
        // in the layout file


        // Display Title in TextView
        final TextView titleView = (TextView)itemLayout.findViewById(R.id.name);
        titleView.setText(alarmItem.getmPhoneName());

        final TextView timeView = (TextView)itemLayout.findViewById(R.id.time);
        timeView.setText(alarmItem.setTimeString());

        final Button button = (Button)itemLayout.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmItem.cancelIntent();
                clear(position);
            }
        });

        return itemLayout;
    }
}
