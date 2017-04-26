package com.aidanogrady.contextualtriggers;

import android.app.AlarmManager;
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
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.aidanogrady.contextualtriggers.context.ContextHolder;
import com.aidanogrady.contextualtriggers.context.DBHelper;
import com.aidanogrady.contextualtriggers.context.Geofence;
import com.aidanogrady.contextualtriggers.context.data.ActivityRecognitionDataSource;
import com.aidanogrady.contextualtriggers.context.data.CalendarDataSource;
import com.aidanogrady.contextualtriggers.context.data.CalendarEvent;
import com.aidanogrady.contextualtriggers.context.data.FoursquareDataSource;
import com.aidanogrady.contextualtriggers.context.data.FoursquareResult;
import com.aidanogrady.contextualtriggers.context.data.LocationDataSource;
import com.aidanogrady.contextualtriggers.context.data.StepCounter;
import com.aidanogrady.contextualtriggers.context.data.OpenWeatherDataSource;
import com.aidanogrady.contextualtriggers.context.data.WeatherForecast;
import com.aidanogrady.contextualtriggers.context.data.WeatherResult;
import com.aidanogrady.contextualtriggers.triggers.TriggerManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * The ContextUpdateManager is an Android service that is used to manage the data sources
 * and handle any updates that they may send.
 */
public class ContextUpdateManager extends Service {

    private static final String TAG = "ContextUpdateManager";

    private TriggerManager triggerManager;

    private ConnectivityManager connectivityManager;

    private List<String> invokedServices;

    private ContextHolder contextHolder;

    private boolean isHomeGeofenceSet = false;
    private boolean isWorkGeofenceSet = false;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "reached service");

        DBHelper.init(getApplicationContext());

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        wakeLock.acquire();


        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        Intent stepCounter = new Intent(this, StepCounter.class);
        startService(stepCounter);
        Intent locationIntent = new Intent(this, LocationDataSource.class);
        startService(locationIntent);
        Intent weatherIntent = new Intent(this, OpenWeatherDataSource.class);
        startService(weatherIntent);
        Intent foursquareIntent = new Intent(this, FoursquareDataSource.class);
        startService(foursquareIntent);

        Intent activityIntent = new Intent(this, ActivityRecognitionDataSource.class);
        startService(activityIntent);

        Intent calenderIntent = new Intent(this, CalendarDataSource.class);
        startService(calenderIntent);


        invokedServices = new ArrayList<>();
        contextHolder = new ContextHolder(this);
        triggerManager = new TriggerManager(this, contextHolder);

        // check if home & work location are known
        Geofence homeGeofence = DBHelper.getGeofence("Home");
        Geofence workGeofence = DBHelper.getGeofence("Work");
        if (homeGeofence != null) {
            Log.e(TAG, "This should not be printed1");
            isHomeGeofenceSet = true;
            addGeofence(homeGeofence);
        }
        if (workGeofence != null) {
            Log.e(TAG, "This should not be printed2");
            isWorkGeofenceSet = true;
            addGeofence(workGeofence);
        }
        Log.e(TAG, "Checked that database is empty");

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
                        int steps = intent.getIntExtra("Count", 0);
                        if (steps == -1) {
                            // should we handle if no step counter available?
                            // is so, should we use gps and calculate average steps?
                        } else {
                            contextHolder.addSteps(steps);
                            System.out.println("Updated steps");
                        }
                        break;
                    case "Location":
//                        if (invokedServices.isEmpty()) {
                            double latitude = intent.getDoubleExtra("Latitude", Double.MAX_VALUE);
                            double longitude = intent.getDoubleExtra("Longitude", Double.MAX_VALUE);

                            contextHolder.setLocation(new Pair<>(latitude, longitude));
                            Toast.makeText(getApplicationContext(),
                                    ("Received: Lat " + latitude + "Long "+ longitude),
                                    Toast.LENGTH_LONG).show();

                            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                            if (latitude != Double.MAX_VALUE && longitude != Double.MAX_VALUE
                                    && activeNetwork.isConnected()) {

                                System.out.println("Getting weather data");
                                Intent weatherIntent = new Intent(this, OpenWeatherDataSource.class);
                                weatherIntent.putExtra("Latitude", latitude);
                                weatherIntent.putExtra("Longitude", longitude);
                                startService(weatherIntent);

                                Intent foursquareIntent = new Intent(this, FoursquareDataSource.class);
                                foursquareIntent.putExtra("Latitude", latitude);
                                foursquareIntent.putExtra("Longitude", longitude);
                                startService(foursquareIntent);
                                // add tags of any invoked services here
                                invokedServices.add(OpenWeatherDataSource.TAG);
                                invokedServices.add(FoursquareDataSource.TAG);

                                // check if can set any of geofences
                                if (!isHomeGeofenceSet && isInTimeRange(2,4)) {
                                    Geofence homeGeofence = new Geofence("Home", latitude, longitude);
                                    DBHelper.addGeofence(homeGeofence);
                                    addGeofence(homeGeofence);
                                    isHomeGeofenceSet = true;
                                }
                                if (!isWorkGeofenceSet && isInTimeRange(10,12)) {
                                    Geofence workGeofence = new Geofence("Work", latitude, longitude);
                                    DBHelper.addGeofence(workGeofence);
                                    addGeofence(workGeofence);
                                    isWorkGeofenceSet = true;
                                }
                            }
//                        }
                        break;
                    case "Weather":
                        WeatherResult result = intent.getParcelableExtra(WeatherResult.TAG);
                        contextHolder.setWeatherForecast(result);
                        invokedServices.remove(OpenWeatherDataSource.TAG);
                        break;
                    case "Foursquare":
                        FoursquareResult nearby = intent.getParcelableExtra("nearby");
                        contextHolder.setNearbyFoursquareData(nearby);
                        invokedServices.remove(FoursquareDataSource.TAG);
                        break;
                    case "Calendar":
                        ArrayList<CalendarEvent> events =
                                intent.getParcelableArrayListExtra(CalendarEvent.TAG);
                        if (events != null) {
                            System.out.printf("%d events found\n", events.size());
                        }
                        contextHolder.setTodaysEvents(events);
                        invokedServices.remove(CalendarDataSource.TAG);
                        break;
                    // add other data sources here
                    // for any dataservice - update context api and add invokedService.remove(tag)
                    // if set empty - notify trigger manager
                }
                if(triggerManager != null && invokedServices.isEmpty()) {
                    triggerManager.update();
                }
            }
        }

        return START_STICKY;
    }

    private boolean isInTimeRange(int from, int to) {
        Log.e(TAG, "Checking time range " + from + " " + to);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Log.e(TAG, "Current value " + calendar.get(Calendar.HOUR_OF_DAY)
                + ":" + calendar.get(Calendar.MINUTE)
                +":"+calendar.get(Calendar.SECOND));

        return calendar.get(Calendar.HOUR_OF_DAY) >= from
                && calendar.get(Calendar.HOUR_OF_DAY) <= to;
    }

    private void addGeofence(Geofence geofence) {
        Intent locationIntent = new Intent(this, LocationDataSource.class);
        locationIntent.putExtra("Geofence", geofence);
        startService(locationIntent);
    }

    @Nullable @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
