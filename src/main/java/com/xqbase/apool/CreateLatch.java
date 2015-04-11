package com.xqbase.apool;

import com.xqbase.apool.callback.SimpleCallback;
import java.util.Collection;

/**
 * The pool object creation latch.
/**
 * This latch controls the pool object creation rate.
 *
 * @author Tony He
 */
public interface CreateLatch {

    /**
     * Submit a new {@link Task}. The {@link Task} may be executed right away
     * or sometime later after the period.
     *
     * @param t the {@link Task} to be executed.
     */
    void submit(Task t);

    /**
     * Set the rate-limit period.
     *
     * @param period New value for period, in milliseconds.
     */
    void setPeriod(long period);

    /**
     * Increment the period.
     */
    void incrementPeriod();

    /**
     * Cancel all pending {@link Task}s that are submitted to the {@link CreateLatch} but
     * haven't been executed.
     *
     * @return a {@link Collection} of {@link Task}s have been cancelled.
     */
    Collection<Task> cancelPendingTasks();

    /**
     * The minimum scheduling unit to apply rate control.
     */
    public interface Task {

        void run(SimpleCallback callback);
    }
}
