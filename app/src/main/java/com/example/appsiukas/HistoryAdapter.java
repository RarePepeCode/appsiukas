package com.example.appsiukas;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by super on 10/27/2016.
 */
public class HistoryAdapter extends BaseAdapter {

    ArrayList<HistoryEntry> dataList;
    Activity activity;

    HistoryAdapter(ArrayList<HistoryEntry> entries, Activity activit) {
        dataList = entries;
        activity = activit;
    }

    public int getCount() {
        if (dataList != null)
        {
            return dataList.size();
        }
        return 0;
    }

    public long getItemId(int position) { return position; }

    public Object getItem(int position) {
        if(dataList != null) {
            return dataList.get(position);
        }
        return null;
    }

    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if(view == null)
        {
            LayoutInflater li = LayoutInflater.from(activity);
            view = li.inflate(R.layout.activity_history_adapter, null);
        }

        TextView wordText = (TextView)view.findViewById(R.id.textViewWord);
        TextView dateText = (TextView)view.findViewById(R.id.textViewDate);
        TextView lifeText = (TextView)view.findViewById(R.id.textViewLifes);

        HistoryEntry le = dataList.get(position);

        dateText.setText(le.getWord());
        wordText.setText(le.getGameDate());
        lifeText.setText(le.getLifesRemaining());

        if (le.getGameDate().charAt(0) == '0') {
            view.setBackgroundColor(Color.parseColor("#FFCCCC"));
        } else {
            view.setBackgroundColor(Color.parseColor("#CCFFCC"));
        }


        return view;
    }
}