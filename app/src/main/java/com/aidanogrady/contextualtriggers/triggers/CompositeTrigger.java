package com.aidanogrady.contextualtriggers.triggers;

import com.aidanogrady.contextualtriggers.context.ContextAPI;

import java.util.List;

/**
 * A composite trigger is one that combines multiple {@link SimpleTrigger} objects into a single
 * trigger. Information from each {@link SimpleTrigger} is used to determine whether or not a single
 * prompt should be sent.
 *
 * @author Aidan O'Grady
 */
abstract class CompositeTrigger implements Trigger {
    /**
     * All triggers that compose this trigger.
     */
    private List<Trigger> mTriggers;
    private ContextAPI mHolder;

    /**
     * Constructs a new CompositeTrigger.
     */
    CompositeTrigger(List<Trigger> triggers, ContextAPI holder) {
        mTriggers = triggers;
        mHolder = holder;
    }

    @Override
    public int getComplexity() {
        int complexity = 0;
        for (Trigger trigger: mTriggers) {
            complexity += trigger.getComplexity();
        }
        return complexity;
    }

}
