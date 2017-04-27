package com.aidanogrady.contextualtriggers;

import android.Manifest;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.aidanogrady.contextualtriggers.context.ContextHolder;
import com.aidanogrady.contextualtriggers.context.DBHelper;
import com.aidanogrady.contextualtriggers.context.Geofences;
import com.aidanogrady.contextualtriggers.context.data.ActivityRecognitionDataSource;
import com.aidanogrady.contextualtriggers.context.data.CalendarDataSource;
import com.aidanogrady.contextualtriggers.context.data.CalendarEvent;
import com.aidanogrady.contextualtriggers.context.data.FoursquareDataSource;
import com.aidanogrady.contextualtriggers.context.data.FoursquareResult;
import com.aidanogrady.contextualtriggers.context.data.LocationDataSource;
import com.aidanogrady.contextualtriggers.context.data.OpenWeatherDataSource;
import com.aidanogrady.contextualtriggers.context.data.StepCounter;
import com.aidanogrady.contextualtriggers.context.data.WeatherResult;
import com.aidanogrady.contextualtriggers.triggers.TriggerManager;
import com.google.android.gms.location.Geofence;
import com.permissioneverywhere.PermissionEverywhere;
import com.permissioneverywhere.PermissionResponse;
import com.permissioneverywhere.PermissionResultCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * The ContextUpdateManager is an Android service that is used to manage the data sources
 * and handle any updates that they may send.
 */
public class ContextUpdateManager extends Service implements PermissionResultCallback {
    private static final String TAG = "ContextUpdateManager";

    private static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_CALENDAR
    };

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

        requestPermissions();
        wakeLock.acquire();


        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        Intent stepCounter = new Intent(this, StepCounter.class);
        startService(stepCounter);
        Intent weatherIntent = new Intent(this, OpenWeatherDataSource.class);
        startService(weatherIntent);
        Intent foursquareIntent = new Intent(this, FoursquareDataSource.class);
        startService(foursquareIntent);
        Intent activityIntent = new Intent(this, ActivityRecognitionDataSource.class);
        startService(activityIntent);
        launchServicesWithPermissions();

        invokedServices = new ArrayList<>();
        contextHolder = new ContextHolder(this);
        triggerManager = new TriggerManager(this, contextHolder);

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
                                if (!isHomeGeofenceSet) {
                                    System.out.println("Home not set, adding geofence");
                                    Geofences homeGeofence = new Geofences("Home", latitude, longitude);
                                    DBHelper.addGeofence(homeGeofence);
                                    addGeofence(homeGeofence);
                                    isHomeGeofenceSet = true;
                                }
                                if (!isWorkGeofenceSet && isInTimeRange(0,2)) {
                                    Geofences workGeofence = new Geofences("Work", latitude, longitude);
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
                    case "Geofence":
                        Log.e(TAG, "transition happened: " + intent.getStringExtra("Transition"));
                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(this)
                                        .setSmallIcon(R.drawable.basic_notification_icon)
                                        .setContentTitle("GEOFENCE")
                                        .setContentText("IT WORKED " + intent.getIntExtra("Transition", Integer.MAX_VALUE));
                        NotificationManager mNotifyMgr =
                                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        mNotifyMgr.notify(1000, mBuilder.build());

                        ArrayList<String> triggeredGeofencesIds = intent.getStringArrayListExtra("Geofences");
                        switch (intent.getIntExtra("Transition", Integer.MAX_VALUE)) {
                            case Geofence.GEOFENCE_TRANSITION_ENTER:
                                if (triggeredGeofencesIds.contains("Work"))
                                    contextHolder.setAtWork(true);
                                break;
                            case Geofence.GEOFENCE_TRANSITION_DWELL:
                                if (triggeredGeofencesIds.contains("Work"))
                                    contextHolder.setAtWork(true);
                                break;
                            case Geofence.GEOFENCE_TRANSITION_EXIT:
                                if (triggeredGeofencesIds.contains("Work")) {
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(new Date());
                                    DBHelper.addWorkExit(calendar.getTimeInMillis());
                                    contextHolder.setAtWork(false);
                                }
                                break;
                        }

                        break;
                    case "Connected":
                        checkIfKnown();
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

        boolean res = calendar.get(Calendar.HOUR_OF_DAY) >= from
                && calendar.get(Calendar.HOUR_OF_DAY) <= to;
        System.out.println("In time range: " + res);
        return calendar.get(Calendar.HOUR_OF_DAY) >= from
                && calendar.get(Calendar.HOUR_OF_DAY) <= to;
    }

    private void addGeofence(Geofences geofence) {
        Intent locationIntent = new Intent(this, LocationDataSource.class);
        locationIntent.putExtra("Geofences", geofence);
        startService(locationIntent);
    }

    private void checkIfKnown() {
        // check if home & work location are known
        Geofences homeGeofence = DBHelper.getGeofence("Home");
        Geofences workGeofence = DBHelper.getGeofence("Work");
        Log.e(TAG, "Checking is geofences are set");
        if (homeGeofence != null) {
            Log.e(TAG, "Home location is set");
            isHomeGeofenceSet = true;
            addGeofence(homeGeofence);
        }
        if (workGeofence != null) {
            Log.e(TAG, "Work location is set");
            isWorkGeofenceSet = true;
            addGeofence(workGeofence);
        }
    }

    @Nullable @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void requestPermissions() {
        PermissionEverywhere.getPermission(getApplicationContext(),
                REQUIRED_PERMISSIONS,
                2,
                "Contextual Triggers",
                "This service needs multple write permissions",
                R.mipmap.ic_launcher)
                .enqueue(this);
    }

    @Override
    public void onComplete(PermissionResponse permissionResponse) {
        launchServicesWithPermissions();
    }

    private void launchServicesWithPermissions() {
        boolean coarse = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean fine = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (fine && coarse) {
            Intent locationIntent = new Intent(this, LocationDataSource.class);
            startService(locationIntent);
        }

        boolean read = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED;
        if (read) {
            Intent calendarIntent = new Intent(this, CalendarDataSource.class);
            startService(calendarIntent);
        }
    }
}
