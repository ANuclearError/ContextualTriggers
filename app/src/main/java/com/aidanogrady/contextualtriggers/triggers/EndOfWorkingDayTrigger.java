package com.aidanogrady.contextualtriggers.triggers;

import android.content.Intent;

import com.aidanogrady.contextualtriggers.context.ContextAPI;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Kristine on 16/04/2017.
 */

public class EndOfWorkingDayTrigger extends SimpleTrigger {

    private ContextAPI mContextHolder;
    private String mNotificationTitle;
    private String mNotificationMessage;
    private long endOfDayTime;


    public EndOfWorkingDayTrigger(ContextAPI holder) {
        super(holder);
        mContextHolder = holder;
        mNotificationTitle = "End of Working Day Notification";
        mNotificationMessage = "End of working day is approaching";
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
    public int getComplexity() {
        return 24601;
    }

    @Override
    public Boolean isTriggered() {
        endOfDayTime = mContextHolder.getEndOfDayTime();
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(endOfDayTime);
        int endHour = calendar.get(Calendar.HOUR_OF_DAY);

        calendar.setTime(new Date());
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        return (endHour - currentHour)<=1 && (endHour - currentHour)>=0;
    }

}
