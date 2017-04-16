package com.aidanogrady.contextualtriggers.triggers;

import android.content.Context;

import com.aidanogrady.contextualtriggers.context.ContextAPI;
import com.aidanogrady.contextualtriggers.context.ContextHolder;

import java.util.ArrayList;
import java.util.List;

public class TriggerManager {

    private List<Trigger> mTriggers;
    private Context mContext;
    private ContextAPI mContextHolder;

    public TriggerManager(Context c, ContextHolder holder){
        mContext = c;
        mContextHolder = holder;
        mTriggers = new ArrayList<>();

        //Triggers
        mTriggers.add(new LocationTrigger("LocationTrigger", mContext, holder));
        mTriggers.add(new WeatherTrigger("WeatherTrigger", mContext, holder));
    }

    public void update(){
        for(Trigger t: mTriggers){
            t.checkForContextChange();
        }
    }


}
