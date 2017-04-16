package com.aidanogrady.contextualtriggers.triggers;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.aidanogrady.contextualtriggers.R;
import com.aidanogrady.contextualtriggers.context.ContextAPI;
import com.aidanogrady.contextualtriggers.context.ContextHolder;

import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Calum on 16/04/2017.
 */

public class WeatherLocationCompositeTrigger extends CompositeTrigger {

    private Context mContext;
    private ContextAPI mContextHolder;
    private List<Trigger> mTriggers;

    WeatherLocationCompositeTrigger(List<Trigger> triggers, Context c, ContextAPI holder) {
        super(triggers, c, holder);
        mContext = c;
        mContextHolder = holder;
        mTriggers = triggers;
    }

    @Override
    public void notifyUser() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.drawable.basic_notification_icon)
                        .setContentTitle("Weather & Location Trigger")
                        .setContentText("You're in a place and the weather is good.");
        int mNotificationId = 003;
        NotificationManager mNotifyMgr =
                (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    @Override
    public void notifyIfTriggered() {

        boolean allTriggersActivated = true;

        for(Trigger t: mTriggers){
            if(!t.isTriggered()){
                allTriggersActivated = false;
            }
        }

        if(allTriggersActivated){
            notifyUser();
        }

    }

    @Override
    public Boolean isTriggered() {
        return null;
    }
}
