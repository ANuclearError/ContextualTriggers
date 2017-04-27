package com.aidanogrady.contextualtriggers.triggers;

import android.content.Intent;

import com.aidanogrady.contextualtriggers.context.ContextAPI;

import java.util.List;


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
