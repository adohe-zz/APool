package com.xqbase.apool.util;

/**
 * Cancellable Interface.
 *
 * @author Tony He
 */
public interface Cancellable {

    /**
     * Attempts to cancel the action represented by the Cancellable.
     *
     * @return true if the action was cancelled; false the action
     * could not be cancelled, either because it has already be
     * cancelled or for some other reasons.
     */
    boolean cancel();
}
