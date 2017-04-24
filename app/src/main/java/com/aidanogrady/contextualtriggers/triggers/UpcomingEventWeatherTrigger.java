package com.aidanogrady.contextualtriggers.triggers;

import android.content.Context;

import com.aidanogrady.contextualtriggers.context.ContextAPI;

import java.util.List;

/**
 * The UpcomingEventWeatherTrigger combines the upcoming event and weather triggers, to ensure that
 * the upcoming event trigger only occurs when the weather is good.
 *
 * @author Aidan O'Grady
 */
public class UpcomingEventWeatherTrigger extends CompositeTrigger {
    /**
     * Notification id.
     */
    private static final int NOTIFICATION_ID = 6;

    /**
     * The title of the notification.
     */
    private static final String NOTIFICATION_TITLE = "Upcoming Event";

    /**
     * The text of this notification.
     */
    private static final String NOTIFICATION_TEXT =
            "You have an event at %s and it's nice outside, why not walk to %s?";
    /**
     * The triggers that this trigger comprises of.
     */
    private List<Trigger> mTriggers;

    /**
     * The Android context of this trigger.
     */
    private Context mContext;

    /**
     * The data source containining information.
     */
    private ContextAPI mContextHolder;

    /**
     * Constructs a new CompositeTrigger.
     *
     * @param triggers  the triggers that comprise of this trigger
     * @param c  the context of the service
     * @param holder  the data holder
     */
    UpcomingEventWeatherTrigger(List<Trigger> triggers, Context c, ContextAPI holder) {
        super(triggers, c, holder);
    }

    @Override
    public void notifyUser() {

    }

    @Override
    public Boolean isTriggered() {
        boolean shouldTrigger = true;
        for (Trigger trigger: mTriggers) {
            shouldTrigger = shouldTrigger && trigger.isTriggered();
        }
        return shouldTrigger;
    }
}
