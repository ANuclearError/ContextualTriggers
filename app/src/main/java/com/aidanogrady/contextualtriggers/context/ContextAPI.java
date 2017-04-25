package com.aidanogrady.contextualtriggers.context;


import android.content.Context;
import android.util.Pair;

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

    int getBatteryLevel(Context context);

    String getNearbyFoursquareData();

    List<CalendarEvent> getTodaysEvents();

}
