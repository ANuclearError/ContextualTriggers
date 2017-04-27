package com.aidanogrady.contextualtriggers.triggers;

import android.content.Intent;

import com.aidanogrady.contextualtriggers.context.ContextAPI;
import com.aidanogrady.contextualtriggers.context.data.FoursquareVenue;
import com.aidanogrady.contextualtriggers.context.data.WeatherResult;

import java.util.Calendar;
import java.util.List;

/**
 * StepWeatherFoursquareTrigger alerts the user for when they should be walking more. If the weather
 * is good, then it will find a recommendation from Foursquare for a place to walk to.
 *
 * @author Aidan O'Grady
 */
public class StepsWeatherFoursquareTrigger extends CompositeTrigger {
    /**
     * The title of the notification.
     */
    private static final String NOTIFICATION_TITLE = "Empty Calendar & Good Weather Trigger";

    /**
     * The text of this notification.
     */
    private static final String NOTIFICATION_TEXT =
            "It's %s outside and you need to walk more, why not go for a walk to %s?";

    /**
     * The triggers that this trigger comprises of.
     */
    private List<Trigger> mTriggers;

    /**
     * The data source containing information.
     */
    private ContextAPI mContextHolder;

    /**
     * The current weather forecast.
     */
    private WeatherResult mForecast;

    /**
     * Nearby venues suitable for walking.
     */
    private List<FoursquareVenue> mVenues;

    /**
     * Constructs a new CompositeTrigger.
     *
     * @param triggers  triggers
     * @param holder  holder
     */
    StepsWeatherFoursquareTrigger(List<Trigger> triggers, ContextAPI holder) {
        super(triggers, holder);
        this.mTriggers = triggers;
        this.mContextHolder = holder;
    }

    @Override
    public int getComplexity() {
        return 246010;
    }

    @Override
    public String getNotificationTitle() {
        return NOTIFICATION_TITLE;
    }

    @Override
    public String getNotificationMessage() {
        return String.format(NOTIFICATION_TEXT,
                mForecast.getForecast(),
                mVenues.get(0).getVenueName());
    }

    @Override
    public Intent getNotificationIntent() {
        return null;
    }

    @Override
    public Boolean isTriggered() {
        boolean shouldTrigger = true;
        for (Trigger trigger: mTriggers) {
            shouldTrigger = shouldTrigger && trigger.isTriggered();
        }
        // Basic checking
        if (shouldTrigger) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            mVenues = mContextHolder.getNearbyFoursquareData().getNearbyVenues();
            mForecast = mContextHolder.getWeatherForecast();
        }
        return shouldTrigger;
    }
}
