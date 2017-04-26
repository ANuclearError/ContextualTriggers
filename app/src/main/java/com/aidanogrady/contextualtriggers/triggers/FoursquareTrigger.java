package com.aidanogrady.contextualtriggers.triggers;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.util.Pair;
import com.aidanogrady.contextualtriggers.context.ContextAPI;
import com.aidanogrady.contextualtriggers.context.data.FoursquareResult;
import com.aidanogrady.contextualtriggers.context.data.FoursquareVenue;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Calum on 15/04/2017.
 */

public class FoursquareTrigger extends SimpleTrigger {
    /**
     * Constructs a new SimpleTrigger with the given name.
     *
     * @param name    the name of the service for this trigger.
     * @param context
     */

    private ContextAPI mContextHolder;
    private String mNotificationTitle;
    private String mNotificationMessage;

    private List<Pair<String, String>> mRecentLocations;
    private double mRecentVisitThreshold = 90; // Time in minutes
    private int categoryVisitThreshold = 10; //Amount of visits before notification allowed
    private int mCheckinThreshold = 100; //Amount of checkins to allow for a notification

    public FoursquareTrigger(ContextAPI holder) {
        super(holder);
        mContextHolder = holder;
        mRecentLocations = new ArrayList<>();
    }

    @Override
    public String getNotificationTitle() {
        return mNotificationTitle;
    }

    @Override
    public String getNotificationMessage() {
        return mNotificationMessage;
    }

    @Override
    public Intent getNotificationIntent() {
        return null;
    }

    @Override
    public Boolean isTriggered() {

        FoursquareResult venues = mContextHolder.getNearbyFoursquareData();
        return venues != null && handleNearbyData(venues);

    }


    private boolean handleNearbyData(FoursquareResult venues) {

        SharedPreferences commonLocations = mContextHolder.getSharedPreferences("locationPrefs");
        List<FoursquareVenue> nearbyVenues = venues.getNearbyVenues();

        if (!nearbyVenues.isEmpty()) {
            for (FoursquareVenue venue : nearbyVenues) {
                if (venue.getCheckins() >= mCheckinThreshold) {

                    updateTally(venue.getVenueName(), venue.getVenueCategory());
                    int categoryTally = commonLocations.getInt(venue.getVenueCategory(), -1);

                    if (categoryTally >= categoryVisitThreshold) {
                        mNotificationTitle = "Great location nearby!";
                        mNotificationMessage = String.format("You are near %s! Perfect for a run!",
                                venue.getVenueName());
                        return true;
                    }
                }

            }
            return false;
        }
        return false;
    }

    private void updateTally(String venueName, String category) {

        SharedPreferences commonLocations = mContextHolder.getSharedPreferences("locationPrefs");
        int categoryTally = commonLocations.getInt(category, -1);

        SharedPreferences.Editor editor = commonLocations.edit();
        if (categoryTally >= 0) {
            if (!visitedRecently(venueName)) {
                System.out.println("CATEGORY VISITS: " + (categoryTally + 1));
                editor.putInt(category, categoryTally + 1);
            }
        } else {
            editor.putInt(category, 1);
        }

        editor.apply();
    }

    private boolean visitedRecently(String name) {

        for (Pair location : mRecentLocations) {

            if (location.first.equals(name)) {
                try {
                    String dateString = (String) location.second;
                    DateFormat format = DateFormat.getDateTimeInstance();
                    Date previous_time = format.parse(dateString);
                    System.out.println(previous_time);

                    Date current_time = new Date();
                    System.out.println(current_time);

                    long difference = current_time.getTime() - previous_time.getTime();
                    long minutesDifference = difference / (60 * 1000);
                    System.out.println("DIFFERENCE: " + minutesDifference);

                    if (minutesDifference > mRecentVisitThreshold) {
                        mRecentLocations.remove(location);
                        mRecentLocations.add(new Pair<>(name, format.format(current_time)));
                        return false;
                    } else {
                        return true;
                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        DateFormat format = DateFormat.getDateTimeInstance();
        Date now = new Date();
        String current_time = format.format(now);

        mRecentLocations.add(new Pair<>(name, current_time));
        return false;
    }
}
