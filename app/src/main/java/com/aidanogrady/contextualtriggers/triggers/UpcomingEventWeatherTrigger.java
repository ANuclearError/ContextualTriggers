package com.aidanogrady.contextualtriggers.triggers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.aidanogrady.contextualtriggers.R;
import com.aidanogrady.contextualtriggers.context.ContextAPI;
import com.aidanogrady.contextualtriggers.context.data.CalendarEvent;

import java.util.Calendar;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

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
     * The data source containing information.
     */
    private ContextAPI mContextHolder;

    /**
     * The upcoming event on the calendar.
     */
    private CalendarEvent mNextEvent;

    /**
     * Constructs a new CompositeTrigger.
     *
     * @param triggers  the triggers that comprise of this trigger
     * @param holder  the data holder
     */
    UpcomingEventWeatherTrigger(List<Trigger> triggers, ContextAPI holder) {
        super(triggers, holder);
        mTriggers = triggers;
    }

    @Override
    public String getNotificationTitle() {
        return NOTIFICATION_TITLE;
    }

    //TODO Intent
//    public void notifyUser() {
//        mNextEvent = mContextHolder.getTodaysEvents().get(0);
//
//        Calendar cal = Calendar.getInstance();
//        cal.setTimeInMillis(mNextEvent.getStartTime());
//        String time = cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);
//        String location = mNextEvent.getLocation();
//
//        String notificationTitle = String.format(NOTIFICATION_TEXT, time, location);
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(mContext)
//                        .setSmallIcon(R.drawable.basic_notification_icon)
//                        .setContentTitle(NOTIFICATION_TITLE) // to do
//                        .setContentText(notificationTitle)
//                        .setContentIntent(getMapsIntent());
//        NotificationManager mNotifyMgr =
//                (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
//        mNotifyMgr.notify(NOTIFICATION_ID, mBuilder.build());
//    }

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
