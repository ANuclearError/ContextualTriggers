package com.aidanogrady.contextualtriggers.triggers;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.aidanogrady.contextualtriggers.R;
import com.aidanogrady.contextualtriggers.context.ContextAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.NOTIFICATION_SERVICE;

public class TriggerManager {

    private List<Trigger> mTriggers;
    private Context mContext;
    private ContextAPI mContextHolder;

    public TriggerManager(Context context, ContextAPI holder){
        mContextHolder = holder;
        mContext = context;
        mTriggers = new ArrayList<>();

        Trigger locationTrigger = new LocationTrigger(mContextHolder);
        Trigger goodWeatherTrigger = new WeatherTrigger(mContextHolder);

        List<Trigger> weatherLocationList = new ArrayList<>();
        weatherLocationList.add(locationTrigger);
        weatherLocationList.add(goodWeatherTrigger);

        Trigger weatherLocationComposite = new WeatherLocationCompositeTrigger(weatherLocationList, mContextHolder);

        Trigger timeRangeTrigger = new TimeRangeTrigger(mContextHolder);
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

        //Triggers
//        mTriggers.add(batteryTrigger);
//        mTriggers.add(timeRangeTrigger);
//        mTriggers.add(locationTrigger);
//        mTriggers.add(weatherLocationComposite);
//        mTriggers.add(foursquareTrigger);
//        mTriggers.add(goodWeatherTrigger);
//        mTriggers.add(emptyCalendarWeatherTrigger);
//        mTriggers.add(upcomingEventWeatherTrigger);
    }

    public void update(){
        List<Trigger> activatedTriggers = new ArrayList<>();
        for(Trigger t: mTriggers){
            if(t.isTriggered()){
                activatedTriggers.add(t);
            }
        }
        handleNotifications(activatedTriggers);
    }

    public void handleNotifications(List<Trigger> activatedTriggers){
        int count = 100;
        for (Trigger t: activatedTriggers) {
            sendNotification(count, t.getNotificationTitle(), t.getNotificationMessage());
            count++;
        }
    }


    public void sendNotification(int id, String title, String message){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.drawable.basic_notification_icon)
                        .setContentTitle(title)
                        .setContentText(message);


        NotificationManager mNotifyMgr =
                (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(id, mBuilder.build());
    }
}
