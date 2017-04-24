package com.aidanogrady.contextualtriggers.triggers;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.aidanogrady.contextualtriggers.R;
import com.aidanogrady.contextualtriggers.context.ContextAPI;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Calum on 17/04/2017.
 */

public class BatteryTrigger extends SimpleTrigger {

    private Context mContext;
    private ContextAPI mContextHolder;
    private int mThreshold;

    /**
     * Constructs a new SimpleTrigger with the given name.
     *
     * @param name    the name of the service for this trigger.
     * @param context
     * @param holder
     */

    public BatteryTrigger(String name, Context context, ContextAPI holder, int threshold) {
        super(name, context, holder);

        mContext = context;
        mContextHolder = holder;
        mThreshold = threshold;
    }

    @Override
    public void notifyUser() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.drawable.basic_notification_icon)
                        .setContentTitle("Battery Notification")
                        .setContentText("Battery below " + mThreshold + "%");

        int mNotificationId = 005;


        NotificationManager mNotifyMgr =
                (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    @Override
    public Boolean isTriggered() {

        int batteryLevel = mContextHolder.getBatteryLevel(mContext);
        return batteryLevel <= mThreshold && batteryLevel != -1;

    }
}
