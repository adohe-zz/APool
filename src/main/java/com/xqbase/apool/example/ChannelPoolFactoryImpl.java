package com.xqbase.apool.example;

import com.xqbase.apool.AsyncPool;
import com.xqbase.apool.impl.AsyncPoolImpl;
import com.xqbase.apool.impl.SimpleCreateLatch;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;

import java.net.SocketAddress;

/**
 * Channel Pool Factory default implementation.
 *
 * @author Tony He
 */
public class ChannelPoolFactoryImpl implements ChannelPoolFactory {

    private final int maxSize;
    private final int minSize;
    private final int maxWaitersSize;
    private final long idleTimeout;
    private final Bootstrap bootstrap;
    private final AsyncPoolImpl.Strategy strategy;

    public ChannelPoolFactoryImpl(int maxSize,
                int minSize,
                int maxWaitersSize,
                long idleTimeout,
                Bootstrap bootstrap,
                AsyncPoolImpl.Strategy strategy) {
        this.maxSize = maxSize;
        this.minSize = minSize;
        this.maxWaitersSize = maxWaitersSize;
        this.idleTimeout = idleTimeout;
        this.bootstrap = bootstrap;
        this.strategy = strategy;
    }

    @Override
    public AsyncPool<Channel> getPool(SocketAddress address) {
        return new AsyncPoolImpl<>(address.toString() + " Connection Pool",
                maxSize,
                minSize,
                maxWaitersSize,
                idleTimeout,
                null,
                new ChannelPoolLifeCycle(address, null, bootstrap),
                new SimpleCreateLatch(0, 0, null, 0),
                AsyncPoolImpl.Strategy.LRU);
    }
}
