package com.aidanogrady.contextualtriggers.triggers;

import android.content.Intent;

import com.aidanogrady.contextualtriggers.context.ContextAPI;
import com.aidanogrady.contextualtriggers.context.DBHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Kristine on 18/04/2017.
 */

public class StepsTrigger extends SimpleTrigger {
    /**
     * The title of the notification.
     */
    private static final String NOTIFICATION_TITLE = "Steps Trigger";

    /**
     * The text of this notification.
     */
    private static final String NOTIFICATION_TEXT =
            "You've walked %s steps, you need to do more.";


    private static final int TARGET_STEPS = 10000;

    private ContextAPI mContextHolder;

    private int mSteps;

    public StepsTrigger(ContextAPI holder) {
        super(holder);
        mContextHolder = holder;
    }

    @Override
    public String getNotificationTitle() {
        return NOTIFICATION_TITLE;
    }

    @Override
    public String getNotificationMessage() {
        return String.format(NOTIFICATION_TEXT, String.valueOf(mSteps));
    }

    @Override
    public Intent getNotificationIntent() {
        return null;
    }

    @Override
    public Boolean isTriggered() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        mSteps = mContextHolder.getSteps(cal.getTimeInMillis());
        System.out.println("Steps walked today: " + mSteps);
        int averageSteps = getWeeklyAverage();
        System.out.println("Average steps: " + averageSteps);
        int estimation = getEstimation(mSteps);
        System.out.println("Estimation: " + estimation);
        return estimation < TARGET_STEPS;
    }

    /**
     * Provides an estimation of how many steps the user will walk today based on the given number
     * of steps walked so far today.
     *
     * @param steps  steps walked so far today
     * @return estimated total number of steps
     */
    private int getEstimation(int steps) {
        long millisInDay = 24 * 60 * 60 * 1000;

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long startToday = cal.getTimeInMillis();

        long timeToday = System.currentTimeMillis() - startToday;

        double percentage = (double) timeToday / millisInDay;
        return (int) (steps / percentage);
    }

    /**
     * Returns the average number of steps walked by the user over the previous week.
     *
     * @return average number of steps.
     */
    private int getWeeklyAverage() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        List<Integer> stepsWeek = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            cal.add(Calendar.DAY_OF_YEAR, -1);
            int steps = DBHelper.getSteps(cal.getTimeInMillis());
            if (steps > 0) {
                System.out.println((i + 1) + " days ago: " + steps);
                stepsWeek.add(steps);
            }
        }

        int averageSteps = 0;
        int count = 0;
        for (Integer steps: stepsWeek) {
            if (steps > 0) {
                averageSteps += steps;
                count++;
            }
        }

        if (count > 0) {
            averageSteps /= count;
        } else {
            averageSteps = 0;
        }
        return averageSteps;
    }
}
