package com.aidanogrady.contextualtriggers.triggers;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class TriggerManager {

    private List<Trigger> mTriggers;
    private Context mContext;

    public TriggerManager(Context c){
        mContext = c;
        mTriggers = new ArrayList<>();

        //Triggers
        mTriggers.add(new LocationTrigger());
    }

    public void update(){
        for(Trigger t: mTriggers){
            t.checkForContextChange(mContext);
        }
    }


}
