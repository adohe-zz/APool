package com.xqbase.apool.callback;

/**
 * A Success Callback.
 *
 * @author Tony He
 */
public interface SuccessCallback<T> {

    /**
     * Called if the asynchronous operation completed with a successful result.
     *
     * @param result the result of the asynchronous operation
     */
    void onSuccess(T result);
}
