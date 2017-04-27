package com.aidanogrady.contextualtriggers.triggers;

import android.content.Intent;

import com.aidanogrady.contextualtriggers.context.ContextAPI;

public class BatteryTrigger extends SimpleTrigger {

    private ContextAPI mContextHolder;
    private String mTitle;
    private String mMessage;
    private int mThreshold;

    /**
     * Constructs a new BatteryTrigger
     *
     * @param holder
     */

    public BatteryTrigger(ContextAPI holder) {
        super(holder);
        mContextHolder = holder;
        mThreshold = 50;
        mTitle = "Battery Notification";
        mMessage = "Battery below " + mThreshold + "%";
    }

    @Override
    public String getNotificationTitle() {
        return mTitle;
    }

    @Override
    public String getNotificationMessage() {
        return mMessage;
    }

    @Override
    public Intent getNotificationIntent() {
        return null;
    }

    @Override
    public Boolean isTriggered() {

        int batteryLevel = mContextHolder.getBatteryLevel();
        return batteryLevel >= mThreshold && batteryLevel != -1;

    }
}
