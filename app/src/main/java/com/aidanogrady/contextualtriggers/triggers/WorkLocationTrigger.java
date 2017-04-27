package com.aidanogrady.contextualtriggers.triggers;

import com.aidanogrady.contextualtriggers.context.ContextAPI;

/**
 * Created by Kristine on 27/04/2017.
 */

public class WorkLocationTrigger extends SimpleTrigger {

    private static final String NOTIFICATION_TITLE = "At Work Trigger";
    private static final String NOTIFICATION_TEXT =
            "It's %s outside, let's go for a walk after work!";

    private ContextAPI mContextHolder;
    private boolean atWork;

    WorkLocationTrigger(ContextAPI holder) {
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
    public Boolean isTriggered() {
        atWork = mContextHolder.getIsAtWork();
        return atWork;
    }
}
