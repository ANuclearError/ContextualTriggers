package com.aidanogrady.contextualtriggers.context.data;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.aidanogrady.contextualtriggers.ContextUpdateManager;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

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
            Log.d(TAG, geofencingEvent.getErrorCode() + "");
            return;
        }

        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT
                || geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER
                || geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {

            Log.d(TAG, "Triggered transition" + getTransition(geofenceTransition));

            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            ArrayList<String> geofenceTransitionDetails = getGeofenceTransitionDetails(triggeringGeofences);

            // send details to main service
            Intent managerIntent = new Intent(this, ContextUpdateManager.class);
            managerIntent.putExtra("DataSource", "Geofence");
            managerIntent.putExtra("Transition", geofenceTransition);
            managerIntent.putStringArrayListExtra("Geofences", geofenceTransitionDetails);
            startService(managerIntent);

        } else
            Log.e(TAG, "Unknown transition " + geofenceTransition);
    }

    private ArrayList<String> getGeofenceTransitionDetails(List<Geofence> triggeringGeofences) {
        ArrayList<String> triggeredGeofencesIds = new ArrayList<>();
        for (Geofence geofence: triggeringGeofences) {
            triggeredGeofencesIds.add(geofence.getRequestId());
        }
        return triggeredGeofencesIds;
    }

    private String getTransition(int geofenceTransition) {

        switch (geofenceTransition) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return "Enter";
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return "Exit";
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                return "Dwell";
            default:
                return "Unknown";
        }
    }
}
