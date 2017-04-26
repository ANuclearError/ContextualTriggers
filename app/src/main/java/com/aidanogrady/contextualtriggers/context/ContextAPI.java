package com.aidanogrady.contextualtriggers.context;


import android.util.Pair;
import android.content.SharedPreferences;

import com.aidanogrady.contextualtriggers.context.data.CalendarEvent;
import com.aidanogrady.contextualtriggers.context.data.FoursquareResult;
import com.aidanogrady.contextualtriggers.context.data.WeatherResult;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by thomas on 10/04/17.
 */

public interface ContextAPI  {

    Pair<Double, Double> getLocation();

    int getSteps();

    WeatherResult getWeatherForecast();

    Date getCurrentTime() throws ParseException;

    int getBatteryLevel();

    FoursquareResult getNearbyFoursquareData();

    List<CalendarEvent> getTodaysEvents();

    SharedPreferences getSharedPreferences(String fileName);

}
