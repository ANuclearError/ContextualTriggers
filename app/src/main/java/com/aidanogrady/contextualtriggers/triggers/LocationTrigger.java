package com.aidanogrady.contextualtriggers.triggers;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.aidanogrady.contextualtriggers.R;
import com.aidanogrady.contextualtriggers.context.ContextAPI;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * The LocationTrigger triggers in the event that the user's location is within a certain distance
 * of a building.
 *
 * @author Aidan O'Grady
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


    private ContextAPI mContextHolder;
    private String mTitle;
    private String mMessage;

    LocationTrigger(ContextAPI holder) {
        super(holder);
        mContextHolder = holder;
        mTitle = "Location Notification";
        mMessage = "You're in a place!";
    }

    @Override
    public String getNotificationTitle() {
        return mTitle;
    }

    @Override
    public String getNotificationMessage() {
        return mMessage;
    }

    @Override
    public Intent getNotificationIntent() {
        return null;
    }

//    @Override
//    public void notifyIfTriggered() {
//
////        if (o instanceof Location) {
////            double latitude = ((Location) o).getLatitude();
////            double longitude = ((Location) o).getLongitude();
////
////            float[] res = new float[1];
////            Location.distanceBetween(latitude, longitude, TARGET_LATITUDE, TARGET_LONGITUDE, res);
////            double dist = res[0];
////
////            if (dist < RADIUS_M) {
////                //            notifyUser(getApplicationContext());
////            }
////        }
//
//        notifyUser();
//
//    }

    @Override
    public Boolean isTriggered() {
        return true;
    }

}
