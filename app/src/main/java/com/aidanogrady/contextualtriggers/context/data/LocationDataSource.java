package com.aidanogrady.contextualtriggers.context.data;

import android.Manifest;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.aidanogrady.contextualtriggers.ContextUpdateManager;
import com.aidanogrady.contextualtriggers.R;
import com.aidanogrady.contextualtriggers.context.Geofences;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.permissioneverywhere.PermissionEverywhere;
import com.permissioneverywhere.PermissionResponse;
import com.permissioneverywhere.PermissionResultCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * The location data source provides triggers with the current location of the user's device,
 * notifying concerned triggers.
 *
 * @author Aidan O'Grady
 */
public class LocationDataSource extends IntentService implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        PermissionResultCallback {

    private static final String TAG = "LocationDS";

    private static final long UPDATE_INTERVAL = 20 * 1000; // 35 * 60 * 1000

    private static final long FASTEST_INTERVAL = 15 * 1000; // 30 * 60 * 1000

    private static final long MAX_WAIT_TIME =  UPDATE_INTERVAL;

    private GoogleApiClient mGoogleApiClient;

    private LocationRequest mLocationRequest;

    private boolean mIsServicesAvailable;

    // Geofences

    private List<Geofence> mGeofenceList;

    private static final int GEOFENCE_RADIUS = 5;

    private PendingIntent mGeofencePendingIntent;

    public LocationDataSource() {
        super("LocationDataSource");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        System.out.println("Creating location data source service");

        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setMaxWaitTime(MAX_WAIT_TIME);

        mIsServicesAvailable = isServicesConnected();

        setUpLocationClientIfNeeded();
        mGoogleApiClient.connect();

        mGeofenceList = new ArrayList<>();
        mGeofencePendingIntent = null;
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Bundle bundle = null;
        if (intent != null) {
            bundle = intent.getExtras();
        }
        if (bundle != null) {
            Log.e(TAG, "in on handle intent");
            if (intent.hasExtra("Geofences")) {
                Geofences geofence
                        = intent.getParcelableExtra("Geofences");
                createGeofence(geofence);
                addGeofences();

            }
        }
        return START_STICKY;
    }

    @Override
    protected void onHandleIntent(Intent intent) {  }

    @Override
    public void onLocationChanged(Location location) {
        Log.e(TAG, "on Location Changed triggered: "
                + "lat " + location.getLatitude() + " long" + location.getLongitude());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(location.getTime());
        Log.e(TAG, "Current location time " + calendar.get(Calendar.HOUR_OF_DAY)
                + ":" + calendar.get(Calendar.MINUTE)
                +":"+calendar.get(Calendar.SECOND));

        Intent intent = new Intent(this, ContextUpdateManager.class);
        intent.putExtra("DataSource", "Location");
        intent.putExtra("Latitude", location.getLatitude());
        intent.putExtra("Longitude", location.getLongitude());
        startService(intent);

        Toast.makeText(getApplicationContext(),
                ("Lat " + location.getLatitude() + "Long "+ location.getLongitude()),
                Toast.LENGTH_LONG).show();
        System.out.printf("Lat %f Long %f", location.getLatitude(), location.getLongitude());

    }

    @Override
    public void onConnected(Bundle bundle) {
        PermissionEverywhere.getPermission(getApplicationContext(),
                new String[] {
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                },
                1,
                "Contextual Triggers",
                "This service needs location permissions",
                R.mipmap.ic_launcher)
                .enqueue(this);

        System.out.println("Notification worked");

        Intent intent = new Intent(this, ContextUpdateManager.class);
        intent.putExtra("DataSource", "Connected");
        startService(intent);
    }

    @Override
    public void onConnectionSuspended(int i) { }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { }


    private boolean isServicesConnected() {
        int res = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        return ConnectionResult.SUCCESS == res;
    }

    private void setUpLocationClientIfNeeded() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onComplete(PermissionResponse permissionResponse) {
        boolean coarse = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean fine = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (fine && coarse) {
            System.out.println("Permissions granted");
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest,
                    this);

        }
    }

    // Geofences methods below

    private void createGeofence(Geofences geofence) {
        mGeofenceList.add(new Geofence.Builder()
                            .setRequestId(geofence.getName())
                            .setCircularRegion(
                                    geofence.getLatitude(),
                                    geofence.getLongitude(),
                                    GEOFENCE_RADIUS
                            )
                            .setExpirationDuration(Geofence.NEVER_EXPIRE)
                            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                                                Geofence.GEOFENCE_TRANSITION_DWELL |
                                                Geofence.GEOFENCE_TRANSITION_EXIT)
                            .setLoiteringDelay(1000)
                            .build());
    }

    private GeofencingRequest getGeofencingRequest() {
        return new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER |
                        GeofencingRequest.INITIAL_TRIGGER_DWELL |
                        GeofencingRequest.INITIAL_TRIGGER_EXIT)
                .addGeofences(mGeofenceList)
                .build();
    }

    private PendingIntent getGeofencePendingIntent() {
        if (mGeofencePendingIntent != null)
            return mGeofencePendingIntent;

        Intent intent = new Intent(this, GeofenceTransitionsService.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void addGeofences() {
        boolean coarse = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean fine = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (fine && coarse) {
            LocationServices.GeofencingApi.addGeofences(mGoogleApiClient,
                                    getGeofencingRequest(),
                                    getGeofencePendingIntent());
        }
    }

    private void removeGeofences() {
        LocationServices.GeofencingApi.removeGeofences(
                mGoogleApiClient,
                getGeofencePendingIntent()
        );
    }
}
