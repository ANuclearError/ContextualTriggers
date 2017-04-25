package com.aidanogrady.contextualtriggers.triggers;

import android.content.Context;

import com.aidanogrady.contextualtriggers.context.ContextAPI;

import java.util.ArrayList;
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
    private Context mContext;
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

    /**
     * Adds the given trigger to the composite trigger.
     *
     * @param trigger  the new trigger to be added.
     */
    public void addTrigger(Trigger trigger) {
        mTriggers.add(trigger);
    }

    /**
     * Removes the given trigger to the composite trigger.
     *
     * @param trigger  the trigger to be removed.
     */
    public void removeTrigger(Trigger trigger) {
        mTriggers.remove(trigger);
    }

    /**
     * Returns the trigger at the given index.
     *
     * @param index  the index to retrieve
     * @return null if the given index is invalid, otherwise the trigger at the given index
     */
    public Trigger getTrigger(int index) {
        if (index < 0 || index >= mTriggers.size())
            return null;
        return mTriggers.get(index);
    }
}
