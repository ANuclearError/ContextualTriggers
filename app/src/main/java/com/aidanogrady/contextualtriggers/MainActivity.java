package com.aidanogrady.contextualtriggers;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aidanogrady.contextualtriggers.context.data.LocationDataSource;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, LocationDataSource.class);
        startService(intent);
        finish();
    }
}
