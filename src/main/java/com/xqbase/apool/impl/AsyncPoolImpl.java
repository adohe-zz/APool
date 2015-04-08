package com.xqbase.apool.impl;

import com.xqbase.apool.AsyncPool;
import com.xqbase.apool.callback.Callback;
import com.xqbase.apool.util.Cancellable;
import com.xqbase.apool.util.LinkedDeque;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The Async Pool Implementation.
 *
 * @author Tony He
 */
public class AsyncPoolImpl<T> implements AsyncPool<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncPoolImpl.class);

    private final String poolName;
    private final int maxSize;
    private final int minSize;
    private final int maxWaiters;
    private final long idleTimeout;
    private final ScheduledExecutorService timeoutExecutor;

    private enum State { NOT_YET_STARTED, RUNNING, SHUTTING_DOWN, STOPPED }

    private int poolSize = 0;
    private final Object lock = new Object();

    private final Deque<T> idle = new LinkedList<>();
    private final LinkedDeque<Callback<T>> waiters = new LinkedDeque<>();

    private State state = State.NOT_YET_STARTED;

    private int totalCreated = 0;
    private int totalDestroyed = 0;
    private int totalTimeout = 0;
    private int checkedOut = 0;

    public AsyncPoolImpl(String poolName, int maxSize, int minSize, int maxWaiters,
                long idleTimeout, ScheduledExecutorService timeoutExecutor) {
        this.poolName = poolName;
        this.maxSize = maxSize;
        this.minSize = minSize;
        this.maxWaiters = maxWaiters;
        this.idleTimeout = idleTimeout;
        this.timeoutExecutor = timeoutExecutor;
    }

    @Override
    public String getName() {
        return poolName;
    }

    @Override
    public void start() {
        synchronized (lock) {
            if (state != State.NOT_YET_STARTED) {
                throw new IllegalStateException(poolName + " is " + state);
            }
            state = State.RUNNING;
            if (idleTimeout > 0) {
                long freq = Math.min(idleTimeout, 1000);
                timeoutExecutor.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, freq, freq, TimeUnit.MILLISECONDS);
            }
        }

        // make the minimum required number of objects now
        for (int i = 0; i < minSize; i++) {
            if (shouldCreate()) {
                create();
            }
        }
    }

    @Override
    public Cancellable get(Callback<T> callback) {
        return null;
    }

    @Override
    public void put(T obj) {

    }

    /**
     * Whether another object creation should be initiated.
     *
     * @return true if another object create should be initiated.
     */
    public boolean shouldCreate() {
        boolean result = false;
        synchronized (lock) {
            if (state == State.RUNNING) {

            }
        }
        return result;
    }

    /**
     * The real object creation method.
     */
    public void create() {

    }
}
