package com.aidanogrady.contextualtriggers.context.data;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.google.android.gms.location.GeofencingEvent;

/**
 * Created by Kristine on 25/04/2017.
 */

public class GeofenceTransitionsService extends IntentService {

    public static final String TAG = "GeofenceTransitions";

    public GeofenceTransitionsService() { super(TAG); }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
//            String errorMessage = GeofenceErrorMessages
        }
    }
}
