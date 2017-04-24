package com.aidanogrady.contextualtriggers.triggers;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.aidanogrady.contextualtriggers.R;
import com.aidanogrady.contextualtriggers.context.ContextAPI;
import com.aidanogrady.contextualtriggers.context.data.CalendarEvent;

import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * The trigger is for event the calendar indicates that today is empty.
 *
 * @author Aidan O'Grady
 */
public class EmptyCalendarTrigger extends SimpleTrigger {
    /**
     * Notification id.
     */
    private static final int NOTIFICATION_ID = 4;

    /**
     * The title of the notification.
     */
    private static final String NOTIFICATION_TITLE = "Empty Calendar Trigger";

    /**
     * The text of this notification.
     */
    private static final String NOTIFICATION_TEXT =
            "You've nothing planned today, why not go for a walk?";

    /**
     * The name of the trigger.
     */
    private String mName;

    /**
     * Android context for handling intents etc.
     */
    private Context mContext;

    /**
     * The data source holder
     */
    private ContextAPI mContextHolder;


    /**
     * Constructs a new SimpleTrigger with the given name.
     *
     * @param name    the name of the service for this trigger
     * @param context the Android context for calling intents etc
     * @param holder  the data source holder for accessing data
     */
    EmptyCalendarTrigger(String name, Context context, ContextAPI holder) {
        super(name, context, holder);
        this.mName = name;
        this.mContext = context;
        this.mContextHolder = holder;
    }

    @Override
    public void notifyUser() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.drawable.basic_notification_icon)
                        .setContentTitle(NOTIFICATION_TITLE) // to do
                        .setContentText(NOTIFICATION_TEXT);
        NotificationManager mNotifyMgr =
                (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(NOTIFICATION_ID, mBuilder.build());
    }

    @Override
    public Boolean isTriggered() {
        List<CalendarEvent> events = mContextHolder.getTodaysEvents();
        System.out.printf("Events is null: %s", events == null);
        return events != null && events.isEmpty();
    }
}
