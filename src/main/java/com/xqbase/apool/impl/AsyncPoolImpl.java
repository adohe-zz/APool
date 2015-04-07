package com.xqbase.apool.impl;

import com.xqbase.apool.AsyncPool;
import com.xqbase.apool.callback.Callback;
import com.xqbase.apool.util.Cancellable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Deque;
import java.util.LinkedList;

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

    private enum State { NOT_YET_STARTED, RUNNING, SHUTTING_DOWN, STOPPED }

    private int poolSize = 0;
    private final Object lock = new Object();

    private final Deque<T> idle = new LinkedList<>();

    private int totalCreated = 0;
    private int totalDestroyed = 0;
    private int totalTimeout = 0;
    private int checkedOut = 0;

    public AsyncPoolImpl(String poolName, int maxSize, int minSize, int maxWaiters) {
        this.poolName = poolName;
        this.maxSize = maxSize;
        this.minSize = minSize;
        this.maxWaiters = maxWaiters;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void start() {

    }

    @Override
    public Cancellable get(Callback<T> callback) {
        return null;
    }

    @Override
    public void put(T obj) {

    }
}
