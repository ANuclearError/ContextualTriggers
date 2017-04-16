package com.aidanogrady.contextualtriggers.triggers;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.aidanogrady.contextualtriggers.R;
import com.aidanogrady.contextualtriggers.context.ContextAPI;
import com.aidanogrady.contextualtriggers.context.ContextHolder;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Calum on 15/04/2017.
 */

public class WeatherTrigger extends SimpleTrigger {
    /**
     * Constructs a new SimpleTrigger with the given name.
     *
     * @param name    the name of the service for this trigger.
     * @param context
     */

    private Context mContext;
    private ContextAPI mContextHolder;
    private String mNotificationTitle;
    private String mNotificationMessage;

    public WeatherTrigger(String name, Context context, ContextAPI holder) {
        super(name, context, holder);
        mContext = context;
        mContextHolder = holder;
    }

    @Override
    public void notifyUser() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.drawable.basic_notification_icon)
                        .setContentTitle(mNotificationTitle)
                        .setContentText(mNotificationMessage);
        int mNotificationId = 002;
        NotificationManager mNotifyMgr =
                (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    @Override
    public void notifyIfTriggered() {

        String id = mContextHolder.getWeatherId();

        if(id != null){
            if(handleWeatherInfo(id)){
                notifyUser();
            }
        }

    }

    @Override
    public Boolean isTriggered() {
        String id = mContextHolder.getWeatherId();

        if(id != null){
            if(handleWeatherInfo(id)){
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param id
     * Codes based on: https://openweathermap.org/weather-conditions
     */

    private boolean handleWeatherInfo(String id){

        boolean isTriggered = true;

        switch (id){

            case "800":
                mNotificationTitle = "Clear Skies!";
                mNotificationMessage = "The weather is clear right now, perfect for getting active!";
                break;
            case "801":
                mNotificationTitle = "Barely cloudy!";
                mNotificationMessage = "The weather is pretty clear right now, great for getting active!";
                break;
            case "802":
                mNotificationTitle = "A touch of cloud";
                mNotificationMessage = "The weather is only a little cloudy, good for getting active!";
                break;
            case "803":
                mNotificationTitle = "A bit cloudy";
                mNotificationMessage = "Some broken clouds won't stop you, why not get active?";
                break;
            case "904":
                mNotificationTitle = "Scorchio!";
                mNotificationMessage = "It's roasting today! Why not put on some sunscreen and get moving!";
                break;
            case "951":
                mNotificationTitle = "Nice and calm";
                mNotificationMessage = "The weather is lovely right now, great for getting active!";
                break;
            case "952":
                mNotificationTitle = "Light and breezy";
                mNotificationMessage = "The weather is light and breezy right now, great for getting active!";
                break;
            case "953":
                mNotificationTitle = "Gentle breeze";
                mNotificationMessage = "The weather is gentle and breezy right now, great for getting active!";
                break;
            case "954":
                mNotificationTitle = "A little breezy";
                mNotificationMessage = "The weather is only a little breezy right now, great for getting active!";
                break;
            case "955":
                mNotificationTitle = "A nice, fresh breeze";
                mNotificationMessage = "There's a fresh breeze right now, great for getting active!";
                break;
            case "521":
                mNotificationTitle = "It's rainy! (Debug)";
                mNotificationMessage = "The weather isn't nice but I need to test this app!";
                break;

            default: isTriggered = false;
        }

        return isTriggered;
    }
}
