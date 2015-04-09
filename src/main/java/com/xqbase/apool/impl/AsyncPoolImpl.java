package com.xqbase.apool.impl;

import com.xqbase.apool.AsyncPool;
import com.xqbase.apool.CreateLatch;
import com.xqbase.apool.LifeCycle;
import com.xqbase.apool.callback.Callback;
import com.xqbase.apool.callback.SimpleCallback;
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
    private final LifeCycle<T> lifeCycle;
    private final CreateLatch createLatch;

    private enum State { NOT_YET_STARTED, RUNNING, SHUTTING_DOWN, STOPPED }

    private int poolSize = 0;
    private final Object lock = new Object();

    private final Deque<TimedObject<T>> idle = new LinkedList<>();
    private final LinkedDeque<Callback<T>> waiters = new LinkedDeque<>();

    private State state = State.NOT_YET_STARTED;

    private int totalCreated = 0;
    private int totalDestroyed = 0;
    private int totalTimeout = 0;
    private int checkedOut = 0;

    public AsyncPoolImpl(String poolName, int maxSize,
                int minSize, int maxWaiters,
                long idleTimeout,
                ScheduledExecutorService timeoutExecutor,
                LifeCycle<T> lifeCycle,
                CreateLatch createLatch) {
        this.poolName = poolName;
        this.maxSize = maxSize;
        this.minSize = minSize;
        this.maxWaiters = maxWaiters;
        this.idleTimeout = idleTimeout;
        this.timeoutExecutor = timeoutExecutor;
        this.lifeCycle = lifeCycle;
        this.createLatch = createLatch;
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
                if (poolSize > maxSize) {

                } else if (waiters.size() > 0 || poolSize < minSize) {
                    poolSize ++;
                    result = true;
                }
            }
        }

        return result;
    }

    /**
     * The real object creation method.
     * PLEASE do not call this method while hold lock.
     */
    public void create() {
        createLatch.submit(new CreateLatch.Task() {
            @Override
            public void run(final SimpleCallback callback) {
                lifeCycle.create(new Callback<T>() {
                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onSuccess(T result) {
                        synchronized (lock) {
                            totalCreated ++;
                        }
                        add(result);
                        callback.onDone();
                    }
                });
            }
        });
    }

    /**
     * Add the newly created object to the idle pool
     * or directly transfer to the waiter.
     *
     * @param obj the newly created pool object.
     */
    private void add(T obj) {
        Callback<T> waiter;

        synchronized (lock) {
            // If we have waiters, the idle list must be empty.
            waiter = waiters.poll();
            if (waiter == null) {
                idle.offerLast(new TimedObject<T>(obj));
            } else {
                checkedOut ++;
            }
        }

        if (waiter != null) {
            waiter.onSuccess(obj);
        }
    }

    private static class TimedObject<T> {

        private final T obj;
        private final long time;

        private TimedObject(T obj) {
            this.obj = obj;
            this.time = System.currentTimeMillis();
        }

        public T getObj() {
            return obj;
        }

        public long getTime() {
            return time;
        }
    }
}
