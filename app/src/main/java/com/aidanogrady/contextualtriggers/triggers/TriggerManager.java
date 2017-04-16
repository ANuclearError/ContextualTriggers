package com.aidanogrady.contextualtriggers.triggers;

import android.content.Context;

import com.aidanogrady.contextualtriggers.context.ContextAPI;

import java.util.ArrayList;
import java.util.List;

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

        //Triggers
        mTriggers.add(locationTrigger);
        mTriggers.add(weatherTrigger);
        mTriggers.add(weatherLocationComposite);
    }

    public void update(){
        for(Trigger t: mTriggers){
            t.notifyIfTriggered();
        }
    }


}
