package com.aidanogrady.contextualtriggers.triggers;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
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

    private ContextAPI mContextHolder;
    private List<Trigger> mTriggers;
    private String mNotificationTitle;
    private String mNotificationMessage;

    WeatherLocationCompositeTrigger(List<Trigger> triggers, ContextAPI holder) {
        super(triggers, holder);
        mContextHolder = holder;
        mTriggers = triggers;
        mNotificationTitle = "Weather & Location Trigger";
        mNotificationMessage = "You're in a place and the weather is good.";
    }

    @Override
    public String getNotificationTitle() {
        return mNotificationTitle;
    }

    @Override
    public String getNotificationMessage() {
        return mNotificationMessage;
    }

    @Override
    public Intent getNotificationIntent() {
        return null;
    }

    @Override
    public Boolean isTriggered() {
        boolean allTriggersActivated = true;

        for(Trigger t: mTriggers){
            if(!t.isTriggered()){
                allTriggersActivated = false;
            }
        }

        return allTriggersActivated;
    }
}
