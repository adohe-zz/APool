package com.xqbase.apool.callback;

/**
 * A callback that is invoked when an operation is complete. This
 * can be used to get a notification when an operation has completed
 * (regardless of success or error).
 *
 * @author Tony He
 */
public interface SimpleCallback {

    /**
     * Get invoked when the operation has completed.
     */
    void onDone();
}
