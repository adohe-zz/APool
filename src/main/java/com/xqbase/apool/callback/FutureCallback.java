package com.xqbase.apool.callback;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Simple future that does not support cancellation.
 *
 * @author Tony He
 */
public class FutureCallback<T> implements Future<T>, Callback<T> {

    private final AtomicReference<Result<T>> result = new AtomicReference<>();
    private final CountDownLatch doneLatch = new CountDownLatch(1);

    @Override
    public void onError(Throwable e) {
        if (e == null) {
            throw new NullPointerException();
        }

        safeSetValue(Result.<T>createError(e));
        doneLatch.countDown();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return doneLatch.getCount() == 0;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        doneLatch.await();
        return unwrapResult();
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (!doneLatch.await(timeout, unit)) {
            throw new TimeoutException();
        }
        return unwrapResult();
    }

    @Override
    public void onSuccess(T result) {
        safeSetValue(Result.createSuccess(result));
        doneLatch.countDown();
    }

    private void safeSetValue(final Result<T> result) {
        if (!this.result.compareAndSet(null, result)) {
            throw new IllegalStateException("Callback already invoked. Value will not be changed.");
        }
    }

    private T unwrapResult() throws ExecutionException {
        try {
            return unwrapResultRaw();
        } catch (Throwable e) {
            throw new ExecutionException(e);
        }
    }

    private T unwrapResultRaw() throws Throwable {
        final Result<T> t = result.get();

        if (t.isSuccess) {
            return t.getResult();
        }

        throw t.getEx();
    }

    /**
     * This internal result class represents the result of
     * an asynchronous operation.
     */
    private static final class Result<T> {

        private final boolean isSuccess;
        private final T result;
        private final Throwable ex;

        public static <T> Result<T> createSuccess(final T t) {
            return new Result<T>(t, null, true);
        }

        public static <T> Result<T> createError(final Throwable e) {
            return new Result<T>(null, e, false);
        }

        private Result(final T result, final Throwable ex, final boolean isSuccess) {
            this.result = result;
            this.ex = ex;
            this.isSuccess = isSuccess;
        }

        public boolean isSuccess() {
            return isSuccess;
        }

        public Throwable getEx() {
            return ex;
        }

        public T getResult() {
            return result;
        }

        public Object getValue() {
            return isSuccess() ? result : ex;
        }
    }
}
