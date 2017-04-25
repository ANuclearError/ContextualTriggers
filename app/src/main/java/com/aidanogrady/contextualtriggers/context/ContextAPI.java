package com.aidanogrady.contextualtriggers.context;


import android.util.Pair;
import android.content.SharedPreferences;

import com.aidanogrady.contextualtriggers.context.data.CalendarEvent;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by thomas on 10/04/17.
 */

public interface ContextAPI  {

    Pair<Double, Double> getLocation();

    int getSteps();

    String getWeatherId();

    Date getCurrentTime() throws ParseException;

    int getBatteryLevel();

    String getNearbyFoursquareData();

    List<CalendarEvent> getTodaysEvents();

    SharedPreferences getSharedPreferences(String fileName);

}
