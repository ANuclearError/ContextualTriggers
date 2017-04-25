package com.aidanogrady.contextualtriggers.triggers;

import com.aidanogrady.contextualtriggers.context.ContextAPI;


public class WeatherTrigger extends SimpleTrigger {
    /**
     * Constructs a new SimpleTrigger with the given name.
     *
     * @param name    the name of the service for this trigger.
     * @param context
     */

    private ContextAPI mContextHolder;
    private String mNotificationTitle;
    private String mNotificationMessage;

    public WeatherTrigger(ContextAPI holder) {
        super(holder);
        mContextHolder = holder;
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
        return handleWeatherInfo(mContextHolder.getWeatherId());
    }

    /**
     *
     * @param id
     * Codes based on: https://openweathermap.org/weather-conditions
     */

    private boolean handleWeatherInfo(String id){

        boolean isTriggered = true;
        if(id != null) {
            switch (id) {

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

                default:
                    isTriggered = false;
            }
        }

//        if(id.charAt(0) == '2'){
//            mNotificationTitle = "Thunderstorms!";
//            mNotificationMessage = "You should probably exercise indoors today!";
//            isTriggered = true;
//        }
//        if(id.charAt(0) == '3'){
//            mNotificationTitle = "Drizzle!";
//            mNotificationMessage = "You should probably exercise indoors today!";
//            isTriggered = true;
//        }
//        if(id.charAt(0) == '5'){
//            mNotificationTitle = "Rain!";
//            mNotificationMessage = "You should probably exercise indoors today!";
//            isTriggered = true;
//        }
//        if(id.charAt(0) == '6'){
//            mNotificationTitle = "Snow!";
//            mNotificationMessage = "You should probably exercise indoors today!";
//            isTriggered = true;
//        }

        return isTriggered;
    }
}
