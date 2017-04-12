package com.aidanogrady.contextualtriggers.context.data;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.aidanogrady.contextualtriggers.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.permissioneverywhere.PermissionEverywhere;
import com.permissioneverywhere.PermissionResponse;
import com.permissioneverywhere.PermissionResultCallback;

/**
 * The location data source provides triggers with the current location of the user's device,
 * notifying concerned triggers.
 *
 * @author Aidan O'Grady
 * @since 0.0.1
 */
public class LocationDataSource extends IntentService implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        PermissionResultCallback {
    private static final long UPDATE_INTERVAL = 60 * 1000;

    private static final long FASTEST_INTERVAL = 30 * 1000;

    private static final long MAX_WAIT_TIME = 5 * UPDATE_INTERVAL;

    private GoogleApiClient mGoogleApiClient;

    private LocationRequest mLocationRequest;

    private boolean mRequestInProgress;

    private boolean mIsServicesAvailable;

    /**
     * The last recorded location recorded.
     */
    private Location mLocation;

    public LocationDataSource() {
        super("LocationDataSource");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        System.out.println("Creating location data source service");
        mRequestInProgress = false;

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setMaxWaitTime(MAX_WAIT_TIME);

        mIsServicesAvailable = isServicesConnected();

        setUpLocationClientIfNeeded();
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if (!mIsServicesAvailable || mGoogleApiClient.isConnected() || mRequestInProgress) {
            return START_STICKY;
        }

        setUpLocationClientIfNeeded();
        if (!mGoogleApiClient.isConnected() || !mGoogleApiClient.isConnecting() && !mRequestInProgress) {
            Log.e("Tr","potato");
            mRequestInProgress = true;
            mGoogleApiClient.connect();
        }
        return START_STICKY;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
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
    }

    @Override
    public void onConnectionSuspended(int i) {
        mRequestInProgress = false;
        mGoogleApiClient = null;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mRequestInProgress = false;
        if (connectionResult.hasResolution()) {
            System.out.println("Figure this out later");
        }
    }


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

    /**
     * Returns the location of the device.
     *
     * @return location of the device
     */
    public Location getLocation() {
        return mLocation;
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
}
