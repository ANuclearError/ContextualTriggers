package com.aidanogrady.contextualtriggers.triggers;

import android.content.Intent;

import com.aidanogrady.contextualtriggers.context.ContextAPI;

/**
 * Created by Kristine on 27/04/2017.
 */

public class AtWorkTrigger extends SimpleTrigger {

    private static final String NOTIFICATION_TITLE = "At Work Trigger";
    private static final String NOTIFICATION_TEXT =
            "You should take a small break from work and go for a walk.";

    private ContextAPI mContextHolder;
    private boolean atWork;

    AtWorkTrigger(ContextAPI holder) {
        super(holder);
        this.mContextHolder = holder;
    }
    @Override
    public String getNotificationTitle() {
        return NOTIFICATION_TITLE;
    }

    @Override
    public String getNotificationMessage() {
        return NOTIFICATION_TEXT;
    }

    @Override
    public Intent getNotificationIntent() {
        return null;
    }

    @Override
    public Boolean isTriggered() {
        atWork = mContextHolder.getIsAtWork();
        return atWork;
    }
}
