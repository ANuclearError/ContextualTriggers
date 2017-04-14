package com.aidanogrady.contextualtriggers.triggers;

import java.util.ArrayList;
import java.util.List;

public class TriggerManager {

    private List<Trigger> mTriggers;

    public TriggerManager(){
        mTriggers = new ArrayList<>();

        //Triggers
        mTriggers.add(new LocationTrigger());
    }

    public void update(){
        for(Trigger t: mTriggers){
            t.checkForContextChange();
        }
    }


}
