package com.aidanogrady.contextualtriggers.triggers;

import android.content.Intent;
import android.net.Uri;

import com.aidanogrady.contextualtriggers.context.ContextAPI;
import com.aidanogrady.contextualtriggers.context.data.CalendarEvent;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * The upcoming event trigger is a trigger for when the user has an event upcoming, indicating a
 * possibility for fitness activity by walking to the location this event is held.
 *
 * @author Aidan O'Grady
 */
public class UpcomingEventTrigger extends SimpleTrigger {
    /**
     * The minimum threshold for when to send this trigger.
     */
    private static final int MIN_MINUTES_THRESHOLD = 30;

    /**
     * The maximum threshold for when to send this trigger..
     */
    private static final int MAX_MINUTES_THRESHOLD = 90;

    /**
     * The title of the notification.
     */
    private static final String NOTIFICATION_TITLE = "Upcoming Event";

    /**
     * The text of this notification.
     */
    private static final String NOTIFICATION_TEXT =
            "You have an event today at %s, why not walk to %s? Tap for a suggested route.";

    /**
     * The data source holder
     */
    private ContextAPI mContextHolder;

    /**
     * The next event in the user's calendar.
     */
    private CalendarEvent mNextEvent;

    /**
     * Constructs a new SimpleTrigger with the given name.
     *
     * @param holder  the data source holder for accessing data
     */
    UpcomingEventTrigger(ContextAPI holder) {
        super(holder);
        this.mContextHolder = holder;
    }

    @Override
    public String getNotificationTitle() {
        return NOTIFICATION_TITLE;
    }

    @Override
    public String getNotificationMessage() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(mNextEvent.getStartTime());
        String time = cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);
        String location = mNextEvent.getLocation();

        return String.format(NOTIFICATION_TEXT, time, location);
    }

    @Override
    public Intent getNotificationIntent() {
        String location = mNextEvent.getLocation();
        String locUri = Uri.encode(location);
        String baseUri = "google.navigation:q=%s&mode=w";
        Uri gmmIntentUri = Uri.parse(String.format(baseUri, locUri));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        return mapIntent;
    }

    @Override
    public Boolean isTriggered() {
        List<CalendarEvent> today = mContextHolder.getTodaysEvents();

        // Basic checking
        if (today == null || today.isEmpty()) {
            return false;
        }

        mNextEvent = today.get(0);

        long now = System.currentTimeMillis();
        long start = mNextEvent.getStartTime();

        // Get the difference between times in minutes, shouldn't be negative but making sure.
        long diff = TimeUnit.MILLISECONDS.toMinutes(Math.abs(start - now));
        return diff >= MIN_MINUTES_THRESHOLD && diff <= MAX_MINUTES_THRESHOLD;
    }
}
