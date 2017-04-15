package com.aidanogrady.contextualtriggers.triggers;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.aidanogrady.contextualtriggers.R;

import java.util.Observable;

/**
 * The LocationTrigger triggers in the event that the user's location is within a certain distance
 * of a building.
 *
 * @author Aidan O'Grady
 * @since 0.0.1
 */
public class LocationTrigger extends SimpleTrigger {
    /**
     * The latitude of the target location.
     */
    private static final double TARGET_LATITUDE = 55.8610092;

    /**
     * The longitude of the target location.
     */
    private static final double TARGET_LONGITUDE = -4.2433644;

    /**
     * The radius around the target location.
     */
    private static final double RADIUS_M = 1000;


    /**
     * Constructs a new LocationTrigger.
     */
    public LocationTrigger() {
        super("LocationTrigger");
    }

    @Override
    public void notifyUser(Context context) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.basic_notification_icon)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");

        int mNotificationId = 001;


        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    @Override
    public void checkForContextChange(Context context) {

        //check for context
        notifyUser(context);

    }

    @Override
    public void update(Observable observable, Object o) {
        if (o instanceof Location) {
            double latitude = ((Location) o).getLatitude();
            double longitude = ((Location) o).getLongitude();

            float[] res = new float[1];
            Location.distanceBetween(latitude, longitude, TARGET_LATITUDE, TARGET_LONGITUDE, res);
            double dist = res[0];

            if (dist < RADIUS_M) {
    //            notifyUser(getApplicationContext());
            }
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
