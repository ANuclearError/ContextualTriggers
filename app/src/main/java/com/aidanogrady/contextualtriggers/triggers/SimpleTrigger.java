package com.aidanogrady.contextualtriggers.triggers;

/**
 * A simple trigger is one that requires a single data source to determine whether or not the user
 * should be prompted to perform new behaviour.
 *
 * @author Aidan O'Grady
 * @since 0.0
 */
public abstract class SimpleTrigger implements Trigger {
    @Override
    public int getComplexity() {
        // Single a single trigger requires a single data.
        return 1;
    }
}
