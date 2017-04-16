package com.aidanogrady.contextualtriggers.triggers;

import android.app.IntentService;
import android.content.Context;

import com.aidanogrady.contextualtriggers.context.ContextAPI;
import com.aidanogrady.contextualtriggers.context.ContextHolder;

/**
 * A simple trigger is one that requires a single data source to determine whether or not the user
 * should be prompted to perform new behaviour.
 *
 * @author Aidan O'Grady
 * @since 0.0
 */
public abstract class SimpleTrigger implements Trigger {
    /**
     * Constructs a new SimpleTrigger with the given name.
     *
     * @param name  the name of the service for this trigger.
     */
    public SimpleTrigger(String name, Context context, ContextAPI holder) {
    }

    @Override
    public int getComplexity() {
        // Single a single trigger requires a single data.
        return 1;
    }
}
