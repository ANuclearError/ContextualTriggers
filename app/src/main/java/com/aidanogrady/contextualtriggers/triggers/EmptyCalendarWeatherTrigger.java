package com.aidanogrady.contextualtriggers.triggers;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.aidanogrady.contextualtriggers.R;
import com.aidanogrady.contextualtriggers.context.ContextAPI;

import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * The EmptyCalendarWeatherTrigger analyses the user's calendar and weather to determine whether
 * or not to suggest ot the user that should go outside and do some activity.
 *
 * @author Aidan O'Grady
 */
public class EmptyCalendarWeatherTrigger extends CompositeTrigger {
    /**
     * Notification id.
     */
    private static final int NOTIFICATION_ID = 4;

    /**
     * The title of the notification.
     */
    private static final String NOTIFICATION_TITLE = "Empty Calendar & Good Weather Trigger";

    /**
     * The text of this notification.
     */
    private static final String NOTIFICATION_TEXT =
            "You've nothing planned today, and the weather's nice, why not go for a walk?";

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
    EmptyCalendarWeatherTrigger(List<Trigger> triggers, ContextAPI holder) {
        super(triggers, holder);
        mTriggers = triggers;
        mContextHolder = holder;
    }

    @Override
    public String getNotificationTitle() {
        return NOTIFICATION_TITLE;
    }

    @Override
    public String getNotificationMessage() {
        return NOTIFICATION_TEXT;
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
