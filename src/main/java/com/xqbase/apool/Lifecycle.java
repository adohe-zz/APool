package com.xqbase.apool;

import com.xqbase.apool.callback.Callback;

/**
 * This class maintains the lifecycle of the pool.
 *
 * @author Tony He
 */
public interface Lifecycle<T> {

    /**
     * Create the pool object.
     *
     * @param callback {@link Callback} invoked when the create done.
     */
    void create(Callback<T> callback);

    /**
     * Whether the get is validate or not.
     *
     * @param obj the object.
     * @return true if the get is validate otherwise false.
     */
    boolean validateGet(T obj);

    /**
     * Whether the put is validate or not.
     *
     * @param obj the object.
     * @return true if the put is validate otherwise false.
     */
    boolean validatePut(T obj);

    /**
     * Destroy the pool object.
     *
     * @param obj the object to be destroyed.
     * @param error whether has error.
     * @param callback {@link Callback} invoked when the destroy done.
     */
    void destroy(T obj, boolean error, Callback<T> callback);
}
