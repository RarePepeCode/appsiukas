package com.example.appsiukas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public void onStartClick(View view) {
        Intent intent = new Intent(this, GameParametersActivity.class);
        startActivity(intent);
    }
}
