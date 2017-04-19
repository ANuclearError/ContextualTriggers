package com.aidanogrady.contextualtriggers.context;


import android.content.Context;

import com.aidanogrady.contextualtriggers.context.data.CalendarEvent;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by thomas on 10/04/17.
 */

public interface ContextAPI  {

    Map<String, Double> getLocation();

    int getSteps();

    String getWeatherId();

    Date getCurrentTime() throws ParseException;

    int getBatteryLevel(Context context);

    String getNearbyFoursquareData();

    List<CalendarEvent> getTodaysEvents();

    void setTodaysEvents(List<CalendarEvent> events);
}
