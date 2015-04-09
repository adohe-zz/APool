package com.xqbase.apool;

import com.xqbase.apool.callback.Callback;

/**
 * This class maintains the lifecycle of the pool.
 *
 * @author Tony He
 */
public interface LifeCycle<T> {

    /**
     * Create a pool object.
     *
     * @param callback {@link Callback} invoked when the operation done.
     */
    void create(Callback<T> callback);

    /**
     * Whether the get is validate or not.
     *
     * @param obj the pool object
     * @return true if validate otherwise false
     */
    boolean validateGet(T obj);

    /**
     * Whether the put is validate or not.
     *
     * @param obj the pool object
     * @return true if validate otherwise false
     */
    boolean validatePut(T obj);

    /**
     * Destroy the pool object.
     *
     * @param obj the pool object
     * @param error whether has error
     * @param callback {@link Callback} invoked when operation done
     */
    void destroy(T obj, boolean error, Callback<T> callback);
}
