package com.aidanogrady.contextualtriggers.context.data;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by ASUS on 25/04/2017.
 */

public class GeofenceTransitionsService extends IntentService {

    public static final String TAG = "GeofenceTransitions";

    public GeofenceTransitionsService() { super(TAG); }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
