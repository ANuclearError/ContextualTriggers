package com.aidanogrady.contextualtriggers.context;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.aidanogrady.contextualtriggers.context.data.CalendarEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.BATTERY_SERVICE;

/**
 * Created by Kristine on 15/04/2017.
 */

public class ContextHolder implements ContextAPI {

    private int steps;
    private Map<String, Double> location;
    private String weatherMain;
    private String weatherId;
    private String nearbyFoursquareData;
    private List<CalendarEvent> calendarEvents;

    public ContextHolder() {
        // set default values
        this.steps = -1;
        this.location = new HashMap<>();
        this.weatherMain = "None";
        this.weatherId = "None";
        this.calendarEvents = null;
    }

    @Override
    public Map<String, Double> getLocation() {
        return location;
    }

    @Override
    public int getSteps() {
        return steps;
    }


    public void setSteps(int steps) {
        this.steps = steps;
    }

    public void setLocation(double latitude, double longitude) {
        location.put("Latitude", latitude);
        location.put("Longitude", longitude);
    }

    public String getWeatherId(){
        return weatherId;
    }

    public void setWeatherId(String id){
        weatherId = id;
    }

    public Date getCurrentTime() throws ParseException {

        Calendar currentCal = Calendar.getInstance();
        String currentTimeStr = new SimpleDateFormat("HH:mm:ss").format(currentCal.getTime());
        Date currentTime = new SimpleDateFormat("HH:mm:ss").parse(currentTimeStr);
        currentCal.setTime(currentTime);
        currentCal.add(Calendar.DATE, 1);

        return currentCal.getTime();

    }

    public int getBatteryLevel(Context context){
//        BatteryManager bm = (BatteryManager) context.getSystemService(BATTERY_SERVICE);
//        int batteryLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
//
//        System.out.println("BATTERY LEVEL: " + batteryLevel);
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);

        if (batteryStatus != null) {

            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale  = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            float batteryPct = (level / (float)scale) * 100;
            System.out.println("BATTERY LEVEL:" + batteryPct);
            return (int) batteryPct;
        }else{
            return -1;
        }
    }

    public void setNearbyFoursquareData(String nearby) {
        nearbyFoursquareData = nearby;
    }

    public String getNearbyFoursquareData(){
        return nearbyFoursquareData;
    }

    @Override
    public List<CalendarEvent> getTodaysEvents() {
        return calendarEvents;
    }

    public void setTodaysEvents(List<CalendarEvent> events) {
        this.calendarEvents = events;
    }
}
