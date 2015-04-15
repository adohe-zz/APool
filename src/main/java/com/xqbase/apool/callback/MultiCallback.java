package com.xqbase.apool.callback;

import com.xqbase.apool.util.None;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A callback which accumulates a specified number of success or failure calls
 * before invoking the original callback with success of failure as appropriate.
 *
 * @author Tony He
 */
public class MultiCallback implements Callback<None> {

    private final AtomicInteger count;
    private final Callback<None> original;
    private final Collection<Throwable> exceptions;

    public MultiCallback(final int count, final Callback<None> original) {
        if (count < 1) {
            throw new IllegalArgumentException();
        }
        this.count = new AtomicInteger(count);
        this.original = original;
        this.exceptions = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void onError(Throwable e) {
        exceptions.add(e);
        checkDone();
    }

    @Override
    public void onSuccess(None result) {
        checkDone();
    }

    private void checkDone() {
        if (count.decrementAndGet() == 0) {
            if (exceptions.isEmpty()) {
                original.onSuccess(None.none());
            } else {
                original.onError(new Throwable());
            }
        }
    }
}
