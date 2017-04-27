package com.aidanogrady.contextualtriggers.triggers;

import android.content.Intent;
import android.net.Uri;

import com.aidanogrady.contextualtriggers.context.ContextAPI;
import com.aidanogrady.contextualtriggers.context.data.CalendarEvent;
import com.aidanogrady.contextualtriggers.context.data.WeatherResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * The UpcomingEventWeatherTrigger combines the upcoming event and weather triggers, to ensure that
 * the upcoming event trigger only occurs when the weather is good.
 *
 * @author Aidan O'Grady
 */
public class UpcomingEventWeatherTrigger extends CompositeTrigger {
    /**
     * The title of the notification.
     */
    private static final String NOTIFICATION_TITLE = "Upcoming Event";

    /**
     * The text of this notification.
     */
    private static final String NOTIFICATION_TEXT =
            "You have an event at %s and it's %s outside, why not walk to %s? Tap here for a route";
    /**
     * The triggers that this trigger comprises of.
     */
    private List<Trigger> mTriggers;

    /**
     * The data source containing information.
     */
    private ContextAPI mContextHolder;

    /**
     * The upcoming event on the calendar.
     */
    private CalendarEvent mNextEvent;

    /**
     * The current weather forecast.
     */
    private WeatherResult mForeCast;

    /**
     * Constructs a new CompositeTrigger.
     *
     * @param triggers  the triggers that comprise of this trigger
     * @param holder  the data holder
     */
    UpcomingEventWeatherTrigger(List<Trigger> triggers, ContextAPI holder) {
        super(triggers, holder);
        mTriggers = triggers;
        mContextHolder = holder;
    }

    @Override
    public String getNotificationTitle() {
        return NOTIFICATION_TITLE;
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
    public String getNotificationMessage() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(mNextEvent.getStartTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH.mm");
        String time = dateFormat.format(cal.getTime());
        String location = mNextEvent.getLocation();
        String weather = mForeCast.getForecast().toString();

        return String.format(NOTIFICATION_TEXT, time, weather, location);
    }

    @Override
    public Boolean isTriggered() {
        boolean shouldTrigger = true;
        for (Trigger trigger: mTriggers) {
            shouldTrigger = shouldTrigger && trigger.isTriggered();
        }

        // Basic checking
        if (shouldTrigger) {
            List<CalendarEvent> today = mContextHolder.getTodaysEvents();
            mNextEvent = today.get(0);
            mForeCast = mContextHolder.getWeatherForecast();
        }
        return shouldTrigger;
    }
}
