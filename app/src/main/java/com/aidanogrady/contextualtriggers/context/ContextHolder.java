package com.aidanogrady.contextualtriggers.context;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.util.Pair;

import com.aidanogrady.contextualtriggers.context.data.CalendarEvent;
import com.aidanogrady.contextualtriggers.context.data.FoursquareResult;
import com.aidanogrady.contextualtriggers.context.data.WeatherResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ContextHolder implements ContextAPI {

    private Context mContext;
    private int steps;
    private Pair<Double, Double> location;
    private WeatherResult weatherForecast;
    private FoursquareResult nearbyFoursquareData;
    private List<CalendarEvent> calendarEvents;

    public ContextHolder(Context context) {
        // set default values

        this.mContext = context;
        this.steps = Integer.MAX_VALUE;
        this.location = null;
        this.weatherForecast = null;
        this.calendarEvents = null;
    }

    @Override
    public Pair<Double, Double> getLocation() {
        return location;
    }

    @Override
    public int getSteps() {
        return steps;
    }


    public void setSteps(int steps) {
        this.steps = steps;
    }

    public void setLocation(Pair<Double, Double> location) {
        this.location = location;
    }

    public WeatherResult getWeatherForecast(){
        return weatherForecast;
    }

    public void setWeatherForecast(WeatherResult forecast){
        weatherForecast = forecast;
    }

    public Date getCurrentTime() throws ParseException {
        Calendar currentCal = Calendar.getInstance();
        String currentTimeStr = new SimpleDateFormat("HH:mm:ss").format(currentCal.getTime());
        Date currentTime = new SimpleDateFormat("HH:mm:ss").parse(currentTimeStr);
        currentCal.setTime(currentTime);
        currentCal.add(Calendar.DATE, 1);

        return currentCal.getTime();
    }

    public int getBatteryLevel(){
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = mContext.registerReceiver(null, ifilter);

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

    public void setNearbyFoursquareData(FoursquareResult nearby){
        nearbyFoursquareData = nearby;
    }

    public FoursquareResult getNearbyFoursquareData(){
        return nearbyFoursquareData;
    }

    @Override
    public List<CalendarEvent> getTodaysEvents() {
        return calendarEvents;
    }

    @Override
    public SharedPreferences getSharedPreferences(String fileName) {
        return mContext.getSharedPreferences(fileName, 0);
    }

    public void setTodaysEvents(List<CalendarEvent> events) {
        this.calendarEvents = events;
    }
}
