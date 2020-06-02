package com.example.appsiukas;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class ResultsActivity extends Activity {

    ArrayList<HistoryEntry> mContents;
    HistoryAdapter mAdapter;
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        HistoryDatabaseHandler dbhandler = new HistoryDatabaseHandler(this);
        mContents = dbhandler.getAllEntries();

        mAdapter = new HistoryAdapter(mContents, this);
        mListView = findViewById(R.id.resultListView);
        mListView.setAdapter(mAdapter);

    }

}