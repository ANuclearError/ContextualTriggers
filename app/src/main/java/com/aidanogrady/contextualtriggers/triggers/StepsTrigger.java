package com.aidanogrady.contextualtriggers.triggers;

import android.content.Context;

import com.aidanogrady.contextualtriggers.context.ContextAPI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ASUS on 18/04/2017.
 */

public class StepsTrigger extends SimpleTrigger {

    private Context mContext;
    private ContextAPI mContextHolder;

    private int lowSteps = 1000;
    private int midSteps = 5000;
    private int highSteps = 10000;

    StepsTrigger(ContextAPI holder) {
        super(holder);
        mContextHolder = holder;
    }

    @Override
    public String getNotificationTitle() {
        return null;
    }

    @Override
    public String getNotificationMessage() {
        return null;
    }

//    @Override
//    public void notifyIfTriggered() {
//        int steps = mContextHolder.getSteps();
//        try {
//            Date currentTime = mContextHolder.getCurrentTime();
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(currentTime);
////            calendar.add(Calendar.DATE, 1);
//            int hour = calendar.HOUR_OF_DAY;
//
//            if (hour >= 12 && steps < lowSteps)
//                notifyUser();
//
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//    }

    @Override
    public Boolean isTriggered() {
        return false;
    }
}
