package com.aidanogrady.contextualtriggers.triggers;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Pair;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * The TriggerManager is responsible for taking the various triggers and is responsible for
 * presenting them to the user. The manager ensures that the user is not overwhelmed with
 * multiple notifications in the event where several triggers are activated at the same time.
 *
 * @author Aidan O'Grady
 * @since 0.0
 */
public class TriggerManager extends BroadcastReceiver {
    /**
     * The key for the stored last ID for the notification manager.
     */
    private static final String LAST_ID = "LAST_TRIGGER_NOTIFICATION_ID";

    /**
     * The minimum time between each notification.
     */
    private static final int LAST_RECEIVED_TIMEOUT = 5 * 60 * 1000;

    /**
     * The number of milliseconds since the last time a trigger broadcast to the trigger manager.
     */
    private long mTimeSinceLastReceived = 0;

    /**
     * The intents received by the manager since the last notification was sent.
     */
    private Set<Pair<Intent, Integer>> mIntents = new HashSet<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        long time = System.currentTimeMillis();

        // Send notification if 5 minutes have passed since last notification
        if (time - mTimeSinceLastReceived > LAST_RECEIVED_TIMEOUT) {
            if (mIntents.isEmpty()) {
                sendNotification(context, intent);
            } else {
                mIntents.add(new Pair<>(intent, intent.getIntExtra("priority", 0)));
                sendNotification(context, getHighestPriorityIntent());
            }

            mTimeSinceLastReceived = time;
            mIntents.clear();
        }
    }

    /**
     * Returns the intent with the highest priority.
     *
     * @return intent with highest priority
     */
    private Intent getHighestPriorityIntent() {
        Intent highest = null;
        int highestPriority = 0;
        for (Pair<Intent, Integer> pair: mIntents) {
            if (highest == null || pair.second > highestPriority) {
                highest = pair.first;
                highestPriority = pair.second;
            }
        }
        return highest;
    }

    /**
     * Sends a notification ot the user.
     */
    private void sendNotification(Context context, Intent intent) {
        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle("Contextual Triggers")
                .setContentText(intent.getStringExtra("message"));

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        AtomicInteger id = new AtomicInteger(prefs.getInt(LAST_ID, 0));

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(id.incrementAndGet(), builder.build());

        prefs.edit()
                .putInt(LAST_ID, id.get())
                .apply();
    }
}
