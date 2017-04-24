package com.aidanogrady.contextualtriggers.triggers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
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
    EmptyCalendarWeatherTrigger(List<Trigger> triggers, Context c, ContextAPI holder) {
        super(triggers, c, holder);
        mTriggers = triggers;
        mContext = c;
        mContextHolder = holder;
    }

    @Override
    public void notifyUser() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.drawable.basic_notification_icon)
                        .setContentText(NOTIFICATION_TEXT)
                        .setContentIntent(getCalendarIntent());
        NotificationManager mNotifyMgr =
                (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(NOTIFICATION_ID, mBuilder.build());
    }

    /**
     * Returns the intent for allowing for an event to be added to the calendar.
     *
     * @return calendar intent
     */
    private PendingIntent getCalendarIntent() {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, "Walking")
                .putExtra(CalendarContract.Events.DESCRIPTION, "Created by ContextualTriggers");
        return PendingIntent.getActivity(mContext, 0, intent, 0);
    }

    @Override
    public void notifyIfTriggered() {
        if (isTriggered()) {
            notifyUser();
        }
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
