package com.aidanogrady.contextualtriggers.triggers;

import android.content.Context;

import com.aidanogrady.contextualtriggers.context.ContextAPI;

/**
 * A simple trigger is one that requires a single data source to determine whether or not the user
 * should be prompted to perform new behaviour.
 *
 * @author Aidan O'Grady
 */
abstract class SimpleTrigger implements Trigger {
    /**
     * Constructs a new SimpleTrigger.
     *
     * @param holder  the data source holder for accessing data
     */
    SimpleTrigger(ContextAPI holder) {
    }

    @Override
    public int getComplexity() {
        // Single a single trigger requires a single data.
        return 1;
    }
}
