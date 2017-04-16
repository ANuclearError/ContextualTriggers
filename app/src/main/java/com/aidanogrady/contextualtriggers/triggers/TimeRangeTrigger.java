package com.aidanogrady.contextualtriggers.triggers;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.aidanogrady.contextualtriggers.R;
import com.aidanogrady.contextualtriggers.context.ContextAPI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Calum on 16/04/2017.
 */

public class TimeRangeTrigger extends SimpleTrigger {

    private Context mContext;
    private ContextAPI mContextHolder;
    private String mNotificationTitle;
    private String mNotificationMessage;
    private List<Map<String, String>> mTimeRanges;

    /**
     * Constructs a new SimpleTrigger with the given name.
     *
     * @param name    the name of the service for this trigger.
     * @param context
     * @param holder
     */
    public TimeRangeTrigger(String name, Context context, ContextAPI holder, List<Map<String, String>> timeRanges) {
        super(name, context, holder);
        mContext = context;
        mContextHolder = holder;
        mTimeRanges = timeRanges;
    }

    @Override
    public void notifyUser() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.drawable.basic_notification_icon)
                        .setContentTitle("Time Range Notification")
                        .setContentText("Time within range");
        int mNotificationId = 004;
        NotificationManager mNotifyMgr =
                (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    @Override
    public void notifyIfTriggered() {

        if(checkTimeInRange()){
            notifyUser();
        }

    }

    @Override
    public Boolean isTriggered() {
        return checkTimeInRange();
    }

    private Boolean checkTimeInRange(){
        try {

            Date currentTime = mContextHolder.getCurrentTime();

            for (Map<String, String> range : mTimeRanges) {

                String strFrom = range.get("from");
                Date timeFrom = new SimpleDateFormat("HH:mm:ss").parse(strFrom);
                Calendar calendarFrom = Calendar.getInstance();
                calendarFrom.setTime(timeFrom);
                calendarFrom.add(Calendar.DATE, 1);

                String strTo = range.get("to");
                Date timeTo = new SimpleDateFormat("HH:mm:ss").parse(strTo);
                Calendar calendarTo = Calendar.getInstance();
                calendarTo.setTime(timeTo);
                calendarTo.add(Calendar.DATE, 1);

                System.out.println(calendarFrom.getTime());
                System.out.println(currentTime);
                System.out.println(calendarTo.getTime());

                if (currentTime.after(calendarFrom.getTime()) && currentTime.before(calendarTo.getTime())) {
                    return true;
                }

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }


}
