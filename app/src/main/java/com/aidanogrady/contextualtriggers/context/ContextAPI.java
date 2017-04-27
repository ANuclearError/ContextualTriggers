package com.aidanogrady.contextualtriggers.context;


import android.content.SharedPreferences;
import android.util.Pair;

import com.aidanogrady.contextualtriggers.context.data.CalendarEvent;
import com.aidanogrady.contextualtriggers.context.data.FoursquareResult;
import com.aidanogrady.contextualtriggers.context.data.WeatherResult;

import java.util.List;

/**
 * Created by thomas on 10/04/17.
 */

public interface ContextAPI  {

    Pair<Double, Double> getLocation();

    int getSteps(long date);

    WeatherResult getWeatherForecast();

    int getBatteryLevel();

    FoursquareResult getNearbyFoursquareData();

    List<CalendarEvent> getTodaysEvents();

    SharedPreferences getSharedPreferences(String fileName);

    boolean getIsAtWork();

    long getEndOfDayTime();

    boolean getIsAtHome();
}
