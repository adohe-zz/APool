package com.xqbase.apool.impl;

import com.xqbase.apool.CreateLatch;
import com.xqbase.apool.callback.SimpleCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * The default implementation of create latch logical.
 *
 * @author Tony He
 */
public class SimpleCreateLatch implements CreateLatch {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleCreateLatch.class);

    private final ScheduledExecutorService executor;
    private final int maxRunningTasks;
    private final Queue<Task> pendingTasks = new LinkedList<>();
    private final int maxPeriod;
    private final int minPeriod;
    private long period;
    private int runningTasks;
    private ScheduledFuture<?> task;

    private SimpleCallback callback = new SimpleCallback() {
        @Override
        public void onDone() {
            synchronized (SimpleCreateLatch.this) {
                runningTasks --;
                schedule();
            }
        }
    };

    private Runnable latch = new Runnable() {
        @Override
        public void run() {
            Task t = null;
            synchronized (SimpleCreateLatch.this) {
                task = null;
                if (runningTasks < maxRunningTasks && !pendingTasks.isEmpty()) {
                    runningTasks ++;
                    t = pendingTasks.poll();
                }
                schedule();
            }
            if (t != null) {
                try {
                    t.run(callback);
                } catch (Exception e) {
                    LOGGER.error("Uncaught exception while running task", e);
                }
            }
        }
    };

    public SimpleCreateLatch(int maxPeriod, int minPeriod, ScheduledExecutorService executor, int maxRunningTasks) {
        this.maxPeriod = maxPeriod;
        this.minPeriod = minPeriod;
        this.executor = executor;
        this.maxRunningTasks = maxRunningTasks;
    }

    @Override
    public void submit(Task t) {
        boolean runNow = false;
        synchronized (this) {
            if (period == 0 && pendingTasks.isEmpty() && runningTasks < maxRunningTasks) {
                runningTasks ++;
                runNow = true;
            } else {
                pendingTasks.add(t);
                schedule();
            }
        }

        if (runNow) {
            t.run(callback);
        }
    }

    @Override
    public void setPeriod(long ms) {
        Long previous = null;
        ms = Math.min(maxPeriod, Math.max(minPeriod, ms));
        synchronized (this) {
            if (ms != period) {
                previous = period;
                period = ms;
                if (!pendingTasks.isEmpty() && (task == null || task.cancel(false))) {
                    long adjustPeriod = period;
                    if (task != null) {
                        long elapsedTime = previous - task.getDelay(TimeUnit.MILLISECONDS);
                        adjustPeriod = Math.max(period - elapsedTime, 0);
                        task = null;
                    }
                    schedule(adjustPeriod);
                }
            }
        }
    }

    @Override
    public void incrementPeriod() {

    }

    @Override
    public Collection<Task> cancelPendingTasks() {
        synchronized (this) {
            Collection<Task> cancelled = new ArrayList<>(pendingTasks.size());
            for (Task item; (item = pendingTasks.poll()) != null;) {
                cancelled.add(item);
            }
            return cancelled;
        }
    }

    /**
     * Schedule a rate-limit task if necessary. Lock must be acquired before call this method.
     */
    private void schedule() {
        schedule(period);
    }

    /**
     * Schedule a rate-limit task if necessary.
     *
     * @param delay time to delay before running the next task.
     */
    private void schedule(long delay) {
        if (runningTasks < maxRunningTasks && !pendingTasks.isEmpty()
                && task == null) {
            task = executor.schedule(latch, delay, TimeUnit.MILLISECONDS);
        }
    }
}
