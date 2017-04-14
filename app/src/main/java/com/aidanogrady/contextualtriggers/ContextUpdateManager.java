package com.aidanogrady.contextualtriggers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.aidanogrady.contextualtriggers.context.data.LocationDataSource;
import com.aidanogrady.contextualtriggers.context.data.StepCounter;

import java.util.Calendar;

/**
 * Created by Kristine on 14/04/2017.
 */

public class ContextUpdateManager extends Service {

    private static final String TAG = "ContextUpdateManager";

    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "reached service");

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        wakeLock.acquire();

        Intent stepCounter = new Intent(this, StepCounter.class);
        startService(stepCounter);
        Intent locationIntent = new Intent(this, LocationDataSource.class);
        startService(locationIntent);

        setupAlarm();

    }

    private void setupAlarm() {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent serviceInvoker = new Intent(this, ServiceInvoker.class);
        alarmIntent = PendingIntent.getBroadcast(this, 0, serviceInvoker, PendingIntent.FLAG_UPDATE_CURRENT);

        // set alarm
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
               AlarmManager.INTERVAL_HALF_HOUR, alarmIntent);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();

        if (bundle != null) {

            if (intent.hasExtra("DataSource")) {
                // received info from a datasource, should read from intent and store properly
                String source = intent.getStringExtra("DataSource");
                switch (source) {
                    case "Steps":
                        int steps = intent.getIntExtra("Count",0);
                        if (steps == -1) {
                            // should we handle if no step counter available?
                            // is so, should we use gps and calculate average steps?
                        } else {
                            Toast.makeText(getApplicationContext(),
                                            ("Steps: " + steps),
                                            Toast.LENGTH_LONG).show();
                        }
                        break;
                    case "Location":
                        break;
                    // add other data sources here
                }
            }
        }

        return START_STICKY;
    }



    @Nullable @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
