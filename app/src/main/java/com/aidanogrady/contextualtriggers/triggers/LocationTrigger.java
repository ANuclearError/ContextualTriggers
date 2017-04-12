package com.aidanogrady.contextualtriggers.triggers;

import android.content.Intent;
import android.location.Location;
import android.support.annotation.Nullable;

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

    /**
     * Constructs a new LocationTrigger with the given name.
     *
     * @param name  the name of the service
     */
    public LocationTrigger(String name) {
        super(name);
    }

    @Override
    public void notifyUser() {
        Intent intent = new Intent("com.aidanogrady.contextualtriggers.TRIGGER");
        intent.putExtra("result", "You are inside the radius");
        sendBroadcast(intent);
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
                notifyUser();
            }
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
