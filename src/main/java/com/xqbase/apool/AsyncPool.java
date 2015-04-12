package com.xqbase.apool;

import com.xqbase.apool.callback.Callback;
import com.xqbase.apool.stats.PoolStats;
import com.xqbase.apool.util.Cancellable;

/**
 * The AsyncPool Interface
 *
 * @author Tony He
 */
public interface AsyncPool<T> {

    /**
     * Get the pool's name.
     *
     * @return The pool's name.
     */
    String getName();

    /**
     * Start the pool.
     */
    void start();

    /**
     * Get an object from the pool.
     *
     * If a valid object is available, it will be passed to the callback (possibly by
     * the thread that invoked <code>get</code>).
     *
     * The pool will determine if an idle object is valid.
     *
     * If none is available, the method returns immediately. If the pool is not yet at
     * max capacity, object creation will be initiated.
     *
     * Callbacks will be executed in FIFO order as objects are returned to the pool (
     * either by other users, or as new object creation completes) or as timeout expires.
     *
     * After finishing with the object, the user must return the object to the pool
     * with <code>put</code>.
     *
     * @param callback the callback to receive the checked out object
     * @return A {@link Cancellable} which, if invoked before the callback, will cancel
     * the pending get request.
     */
    Cancellable get(Callback<T> callback);

    /**
     * Return a previously checked out object to the pool. It is an error to return an object
     * to the pool that is not currently checked out from the pool.
     *
     * @param obj the object to be returned.
     */
    void put(T obj);

    /**
     * Dispose of a checked out object which is not operated correctly. It is an
     * error to dispose a object which is not checked out from this pool.
     *
     * @param obj the object to be disposed.
     */
    void dispose(T obj);

    /**
     * Get the current stats of the async pool.
     *
     * @return stats of pool when called this method.
     */
    PoolStats getStats();
}
