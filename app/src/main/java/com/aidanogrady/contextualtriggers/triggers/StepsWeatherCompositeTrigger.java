package com.aidanogrady.contextualtriggers.triggers;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.aidanogrady.contextualtriggers.R;
import com.aidanogrady.contextualtriggers.context.ContextAPI;

import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;


public class StepsWeatherCompositeTrigger extends CompositeTrigger {

    private ContextAPI mContextHolder;
    private List<Trigger> mTriggers;
    private String mNotificationTitle;
    private String mNotificationMessage;

    StepsWeatherCompositeTrigger(List<Trigger> triggers, ContextAPI holder) {
        super(triggers, holder);

        mContextHolder = holder;
        mTriggers = triggers;
        mNotificationTitle = "Steps & Weather Trigger";
        mNotificationMessage = "The weather is nice, go for a walk!";
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
