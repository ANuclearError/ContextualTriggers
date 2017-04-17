package com.aidanogrady.contextualtriggers;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.aidanogrady.contextualtriggers.context.ContextHolder;
import com.aidanogrady.contextualtriggers.context.data.FoursquareDataSource;
import com.aidanogrady.contextualtriggers.context.data.LocationDataSource;
import com.aidanogrady.contextualtriggers.context.data.StepCounter;
import com.aidanogrady.contextualtriggers.context.data.WeatherDataSource;
import com.aidanogrady.contextualtriggers.triggers.TriggerManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kristine on 14/04/2017.
 */

public class ContextUpdateManager extends Service {

    private static final String TAG = "ContextUpdateManager";

    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;

    private TriggerManager triggerManager;

    private ConnectivityManager connectivityManager;

    private List<String> invokedServices;

    private ContextHolder contextHolder;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "reached service");

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        wakeLock.acquire();


        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        Intent stepCounter = new Intent(this, StepCounter.class);
        startService(stepCounter);
        Intent locationIntent = new Intent(this, LocationDataSource.class);
        startService(locationIntent);
        Intent weatherIntent = new Intent(this, WeatherDataSource.class);
        startService(weatherIntent);
        Intent foursquareIntent = new Intent(this, FoursquareDataSource.class);
        startService(foursquareIntent);

        invokedServices = new ArrayList<>();
        contextHolder = new ContextHolder();
        triggerManager = new TriggerManager(this, contextHolder);

        setupAlarm();

    }

    private void setupAlarm() {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent serviceInvoker = new Intent(this, ServiceInvoker.class);
        alarmIntent = PendingIntent.getBroadcast(this, 0, serviceInvoker, PendingIntent.FLAG_UPDATE_CURRENT);

        // set alarm
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
               AlarmManager.INTERVAL_FIFTEEN_MINUTES, alarmIntent);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent,flags,startId);
        Bundle bundle;

        if(intent != null) {
             bundle = intent.getExtras();
        }else{
            bundle = null;
        }
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
                            contextHolder.setSteps(steps);
                            Toast.makeText(getApplicationContext(),
                                            ("Steps: " + steps),
                                            Toast.LENGTH_LONG).show();
                        }
                        break;
                    case "Location":
                        double latitude = intent.getDoubleExtra("Latitude", 0.0);
                        double longitude = intent.getDoubleExtra("Longitude", 0.0);

                        contextHolder.setLocation(latitude, longitude);
                        Toast.makeText(getApplicationContext(),
                                ("Received: Lat " + latitude + "Long "+ longitude),
                                Toast.LENGTH_LONG).show();

                        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                        if (latitude != 0.0 && longitude != 0.0 && activeNetwork.isConnected()) {


                            Intent weatherIntent = new Intent(this, WeatherDataSource.class);
                            weatherIntent.putExtra("Latitude", latitude);
                            weatherIntent.putExtra("Longitude", longitude);
                            startService(weatherIntent);

                            Intent foursquareIntent = new Intent(this, FoursquareDataSource.class);
                            foursquareIntent.putExtra("Latitude", latitude);
                            foursquareIntent.putExtra("Longitude", longitude);
                            startService(foursquareIntent);
                            // should get new location and then call other services from here
                            // invokedServices.add(tag);
                        }
                        break;
                    case "Weather":
                        String id = intent.getStringExtra("id");
                        contextHolder.setWeatherId(id);
                        break;
                    case "Foursquare":
                        String nearby = intent.getStringExtra("nearby");
                        contextHolder.setNearbyFoursquareData(nearby);
                    // add other data sources here
                    // for any dataservice - update context api and invokedService.remove(tag)
                    // if set empty - notify trigger manager
                }
                if(triggerManager != null) {
                    triggerManager.update();
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
