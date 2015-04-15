package com.xqbase.apool.callback;

import com.xqbase.apool.util.None;

/**
 * Coordinate a sequence of callbacks.
 *
 * @author Tony He
 */
public class Callbacks {

    private static final NullCallback NULL_CALLBACK = new NullCallback();

    public static <T> NullCallback<T> empty() {
        return NULL_CALLBACK;
    }

    public static Callback<None> countDown(Callback<None> callback, int size) {
        if (size == 0) {
            callback.onSuccess(None.none());
            return empty();
        }

        return new MultiCallback(size, callback);
    }

    private static class NullCallback<T> implements Callback<T> {

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onSuccess(T result) {
        }
    }
}
