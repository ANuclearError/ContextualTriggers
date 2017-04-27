package com.aidanogrady.contextualtriggers.triggers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.aidanogrady.contextualtriggers.R;
import com.aidanogrady.contextualtriggers.context.ContextAPI;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

public class TriggerManager {

    private List<Trigger> mTriggers;
    private Context mContext;
    private ContextAPI mContextHolder;

    private double lastNotificationTime;

    public TriggerManager(Context context, ContextAPI holder){
        mContextHolder = holder;
        mContext = context;
        mTriggers = new ArrayList<>();

        Trigger locationTrigger = new LocationTrigger(mContextHolder);
        Trigger goodWeatherTrigger = new GoodWeatherTrigger(mContextHolder);

        List<Trigger> weatherLocationList = new ArrayList<>();
        weatherLocationList.add(locationTrigger);
        weatherLocationList.add(goodWeatherTrigger);

        Trigger weatherLocationComposite = new WeatherLocationCompositeTrigger(weatherLocationList, mContextHolder);

        Trigger endOfWorkingDayTrigger = new EndOfWorkingDayTrigger(mContextHolder);
        Trigger atHomeTrigger = new AtHomeTrigger(mContextHolder);
        Trigger workLocationTrigger = new WorkLocationTrigger(mContextHolder);
        Trigger batteryTrigger = new BatteryTrigger(mContextHolder);
        Trigger foursquareTrigger = new FoursquareTrigger(mContextHolder);

        Trigger emptyCalendarTrigger = new EmptyCalendarTrigger(mContextHolder);
        List<Trigger> emptyCalendarWeatherTriggers = new ArrayList<>();
        emptyCalendarWeatherTriggers.add(emptyCalendarTrigger);
        emptyCalendarWeatherTriggers.add(goodWeatherTrigger);
        Trigger emptyCalendarWeatherTrigger = new EmptyCalendarWeatherTrigger(
                emptyCalendarWeatherTriggers,
                mContextHolder);

        Trigger upcomingEventTrigger = new UpcomingEventTrigger(
                mContextHolder);

        List<Trigger> upcomingEventWeatherTriggerList = new ArrayList<>();
        upcomingEventWeatherTriggerList.add(upcomingEventTrigger);
        upcomingEventWeatherTriggerList.add(goodWeatherTrigger);
        Trigger upcomingEventWeatherTrigger = new UpcomingEventWeatherTrigger(
                upcomingEventWeatherTriggerList,
                mContextHolder);

        Trigger stepsTrigger = new StepsTrigger(mContextHolder);

        List<Trigger> endOfWorkDayTriggerList = new ArrayList<>();
        endOfWorkDayTriggerList.add(endOfWorkingDayTrigger);
        endOfWorkDayTriggerList.add(workLocationTrigger);
        endOfWorkDayTriggerList.add(goodWeatherTrigger);
        Trigger endOfWorkDayTrigger = new WeatherLocationTimeCompositeTrigger(
                endOfWorkDayTriggerList,
                mContextHolder);

        List<Trigger> atHomeGoodWeatherTriggerList = new ArrayList<>();
        atHomeGoodWeatherTriggerList.add(atHomeTrigger);
        atHomeGoodWeatherTriggerList.add(goodWeatherTrigger);
        Trigger atHomeGoodWeatherTrigger = new HomeGoodWeatherCompositeTrigger(
                atHomeGoodWeatherTriggerList,
                mContextHolder);

        //Triggers
        mTriggers.add(batteryTrigger);
        mTriggers.add(locationTrigger);
        mTriggers.add(weatherLocationComposite);
        mTriggers.add(foursquareTrigger);
        mTriggers.add(goodWeatherTrigger);
        mTriggers.add(emptyCalendarWeatherTrigger);
        mTriggers.add(upcomingEventWeatherTrigger);
        mTriggers.add(stepsTrigger);
        mTriggers.add(endOfWorkDayTrigger);
        mTriggers.add(atHomeGoodWeatherTrigger);
    }

    public void update(){
        if(getTime() > 6 && getTime() < 20) {
            List<Trigger> activatedTriggers = new ArrayList<>();
            for (Trigger t : mTriggers) {
                if (t.isTriggered()) {
                    activatedTriggers.add(t);
                }
            }
            handleNotifications(activatedTriggers);
        }
    }

    private void handleNotifications(List<Trigger> activatedTriggers){
        int count = 100;
        for (Trigger t: activatedTriggers) {
            sendNotification(count, t.getNotificationTitle(), t.getNotificationMessage(),
                    t.getNotificationIntent());
            count++;
        }
    }


    private void sendNotification(int id, String title, String message, Intent intent){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.drawable.basic_notification_icon)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message));

        if(intent != null){
            mBuilder.setContentIntent(PendingIntent.getActivity(mContext, 0, intent, 0));
        }

        NotificationManager mNotifyMgr =
                (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(id, mBuilder.build());
    }


    private Double getTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH.mm");
        return Double.parseDouble(dateFormat.format(cal.getTime()));
    }
}
