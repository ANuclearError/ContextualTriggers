package com.aidanogrady.contextualtriggers.triggers;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.util.Pair;

import com.aidanogrady.contextualtriggers.R;
import com.aidanogrady.contextualtriggers.context.ContextAPI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.NOTIFICATION_SERVICE;

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
