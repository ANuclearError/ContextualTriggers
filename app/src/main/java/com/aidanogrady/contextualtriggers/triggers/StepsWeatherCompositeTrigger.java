package com.aidanogrady.contextualtriggers.triggers;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.aidanogrady.contextualtriggers.R;
import com.aidanogrady.contextualtriggers.context.ContextAPI;

import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Kristine on 18/04/2017.
 */

public class StepsWeatherCompositeTrigger extends CompositeTrigger {


    private Context mContext;
    private ContextAPI mContextHolder;
    private List<Trigger> mTriggers;

    StepsWeatherCompositeTrigger(List<Trigger> triggers, Context c, ContextAPI holder) {
        super(triggers, c, holder);
        mContext = c;
        mContextHolder = holder;
        mTriggers = triggers;
    }

    @Override
    public void notifyUser() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.drawable.basic_notification_icon)
                        .setContentTitle("Steps & Weather Trigger") // to do
                        .setContentText("The weather is nice, go for a walk!"); // to do
        int mNotificationId = 003;
        NotificationManager mNotifyMgr =
                (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    @Override
    public void notifyIfTriggered() {
        boolean allTriggersActivated = true;

        for(Trigger t: mTriggers){
            if(!t.isTriggered()){
                allTriggersActivated = false;
            }
        }

        if(allTriggersActivated){
            notifyUser();
        }
    }

    @Override
    public Boolean isTriggered() {
        return null;
    }
}
