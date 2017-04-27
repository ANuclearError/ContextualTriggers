package com.aidanogrady.contextualtriggers.triggers;

import android.content.Intent;

import com.aidanogrady.contextualtriggers.context.ContextAPI;

import java.util.List;

/**
 * Created by Kristine on 27/04/2017.
 */

public class WeatherLocationTimeCompositeTrigger extends CompositeTrigger {

    private ContextAPI mContextHolder;
    private List<Trigger> mTriggers;
    private String mNotificationTitle;
    private String mNotificationMessage;

    WeatherLocationTimeCompositeTrigger(List<Trigger> triggers, ContextAPI holder) {
        super(triggers, holder);
        mContextHolder = holder;
        mTriggers = triggers;
        mNotificationTitle = "Weather, Location & Time Trigger";
        mNotificationMessage = "The weather seems nice, why not walk after work?";
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
