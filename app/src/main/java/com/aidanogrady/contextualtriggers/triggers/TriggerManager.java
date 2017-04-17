package com.aidanogrady.contextualtriggers.triggers;

import android.content.Context;

import com.aidanogrady.contextualtriggers.context.ContextAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TriggerManager {

    private List<Trigger> mTriggers;
    private Context mContext;
    private ContextAPI mContextHolder;

    public TriggerManager(Context c, ContextAPI holder){
        mContext = c;
        mContextHolder = holder;
        mTriggers = new ArrayList<>();

        Trigger locationTrigger = new LocationTrigger("LocationTrigger", mContext, mContextHolder);
        Trigger weatherTrigger = new WeatherTrigger("WeatherTrigger", mContext, mContextHolder);

        List<Trigger> weatherLocationList = new ArrayList<>();
        weatherLocationList.add(locationTrigger);
        weatherLocationList.add(weatherTrigger);

        Trigger weatherLocationComposite = new WeatherLocationCompositeTrigger(weatherLocationList, mContext, mContextHolder);


        List<Map<String, String>> timeList = new ArrayList<Map<String,String>>();
        Map<String,String> range_1 = new HashMap<>();
        range_1.put("from", "22:00:00");
        range_1.put("to", "23:59:00");
        timeList.add(range_1);

        Trigger timeRangeTrigger = new TimeRangeTrigger("TimeRangeTrigger", mContext, mContextHolder, timeList);
        Trigger batteryTrigger = new BatteryTrigger("BatteryTrigger", mContext, mContextHolder, 75);
        Trigger foursquareTrigger = new FoursquareTrigger("FoursquareTrigger", mContext, mContextHolder);


        //Triggers
        mTriggers.add(batteryTrigger);
        mTriggers.add(timeRangeTrigger);
        mTriggers.add(locationTrigger);
        mTriggers.add(weatherTrigger);
        mTriggers.add(weatherLocationComposite);
        mTriggers.add(foursquareTrigger);
    }

    public void update(){
        for(Trigger t: mTriggers){
            t.notifyIfTriggered();
        }
    }


}
