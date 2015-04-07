package com.xqbase.apool.callback;

/**
 * A callback provides a means for the user to get notification when
 * an asynchronous operation has completed.
 *
 * @author Tony He
 */
public interface Callback<T> extends SuccessCallback<T> {

    /**
     * Called if the asynchronous operation failed with an error.
     *
     * @param e the error
     */
    void onError(Throwable e);
}
