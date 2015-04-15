package com.xqbase.apool.stats;

/**
 * The Async Pool Stats.
 *
 * @author Tony He
 */
public class AsyncPoolStats implements PoolStats {

    private final String poolName;
    private final int maxSize;
    private final int minSize;
    private final int poolSize;

    private final int totalCreated;
    private final int totalCreateErrors;
    private final int totalDestroyed;
    private final int totalDestroyErrors;
    private final int totalBadDestroyed;
    private final int totalTimeout;
    private final int checkedOut;
    private final int idleCount;

    public AsyncPoolStats(String poolName,
                          int maxSize,
                          int minSize,
                          int poolSize,
                          int totalCreated,
                          int totalCreateErrors,
                          int totalDestroyed,
                          int totalDestroyErrors,
                          int totalBadDestroyed,
                          int totalTimeout,
                          int checkedOut,
                          int idleCount) {
        this.poolName = poolName;
        this.maxSize = maxSize;
        this.minSize = minSize;
        this.poolSize = poolSize;
        this.totalCreated = totalCreated;
        this.totalCreateErrors = totalCreateErrors;
        this.totalDestroyed = totalDestroyed;
        this.totalDestroyErrors = totalDestroyErrors;
        this.totalBadDestroyed = totalBadDestroyed;
        this.totalTimeout = totalTimeout;
        this.checkedOut = checkedOut;
        this.idleCount = idleCount;
    }

    @Override
    public int getTotalCreated() {
        return totalCreated;
    }

    @Override
    public int getTotalDestroyed() {
        return totalDestroyed;
    }

    @Override
    public int getTotalCreatedErrors() {
        return totalCreateErrors;
    }

    @Override
    public int getTotalDestroyErrors() {
        return totalDestroyErrors;
    }

    @Override
    public int getTotalBadDestroyed() {
        return totalBadDestroyed;
    }

    @Override
    public int getTotalTimeout() {
        return totalTimeout;
    }

    @Override
    public int getCheckedOut() {
        return checkedOut;
    }

    @Override
    public int getMaxPoolSize() {
        return maxSize;
    }

    @Override
    public int getMinPoolSize() {
        return minSize;
    }

    @Override
    public int getPoolSize() {
        return poolSize;
    }

    @Override
    public int getIdleCount() {
        return idleCount;
    }

    @Override
    public String getPoolName() {
        return poolName;
    }
}
