package com.aidanogrady.contextualtriggers.triggers;

import com.aidanogrady.contextualtriggers.context.ContextAPI;
import com.aidanogrady.contextualtriggers.context.data.WeatherResult;

/**
 * The GoodWeatherTrigger is a trigger that enacts when it deems that the current weather is
 * suitable for activity.
 *
 * @author Calum Alexander-McGarry
 * @author Aidan O'Grady
 */
public class GoodWeatherTrigger extends SimpleTrigger {
    /**
     * The title of the notification.
     */
    private static final String NOTIFICATION_TITLE = "Weather Trigger";

    /**
     * The text of this notification.
     */
    private static final String NOTIFICATION_TEXT =
            "It's %s outside, let's go for a walk or a run shall we?";

    /**
     * The data source holder
     */
    private ContextAPI mContextHolder;

    /**
     * The current weather forecast.
     */
    private WeatherResult mForeCast;

    /**
     * Constructs a new SimpleTrigger.
     *
     * @param holder the data source holder for accessing data
     */
    GoodWeatherTrigger(ContextAPI holder) {
        super(holder);
        this.mContextHolder = holder;
    }

    @Override
    public String getNotificationTitle() {
        return NOTIFICATION_TITLE;
    }

    @Override
    public String getNotificationMessage() {
        return String.format(NOTIFICATION_TEXT, mForeCast.getForecast().toString());
    }

    @Override
    public Boolean isTriggered() {
        mForeCast = mContextHolder.getWeatherForecast();
        switch (mForeCast.getForecast()) {
            case CLEAR:
            case CLOUDY:
                return true;
            default:
                return false;
        }
    }
}
