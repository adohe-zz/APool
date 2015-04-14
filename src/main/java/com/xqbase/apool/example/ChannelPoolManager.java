package com.xqbase.apool.example;

import com.xqbase.apool.AsyncPool;
import com.xqbase.apool.stats.PoolStats;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * This manager maintains a collection of channel pools.
 *
 * @author Tony He
 */
public class ChannelPoolManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelPoolManager.class);

    // All modifications of pool and all access to state must be blocked on mutex.
    private Object mutex = new Object();
    // The concurrency level is set to 1, since this is accessed in a synchronized block.
    private ConcurrentMap<SocketAddress, AsyncPool<Channel>> pool = new ConcurrentHashMap<>(256, 0.75f, 1);

    private enum State {RUNNING, SHUTTING_DOWN, SHUT_DOWN}
    private State state = State.RUNNING;

    private final ChannelPoolFactory poolFactory;
    private final String name;

    public ChannelPoolManager(ChannelPoolFactory poolFactory, String name) {
        this.poolFactory = poolFactory;
        this.name = name;
    }

    /**
     * Get the channel pool associated with the socket address.
     *
     * @param address the address
     * @return a channel pool of this address
     */
    public AsyncPool<Channel> getPoolForAddress(SocketAddress address) {
        AsyncPool<Channel> channelPool = pool.get(address);
        if (channelPool != null) {
            return channelPool;
        }

        synchronized (mutex) {
            if (state != State.RUNNING) {
                throw new IllegalStateException(name + " is " + state);
            }

            channelPool = pool.get(address);
            if (pool == null) {
                channelPool = poolFactory.getPool(address);
                channelPool.start();
                pool.put(address, channelPool);
            }
        }

        return channelPool;
    }

    /**
     * Shutdown the all the channel pools under this manager.
     */
    public void shutdown() {
        final Collection<AsyncPool<Channel>> pools;
        final State innerState;

        synchronized (mutex) {
            innerState = state;
            pools = pool.values();
            if (innerState == State.RUNNING) {
                state = State.SHUTTING_DOWN;
            }
        }

        if (innerState != State.RUNNING) {
            throw new IllegalStateException(name + " is " + innerState);
        }

        for (AsyncPool<Channel> p : pools) {
            p.shutdown(null);
        }
    }

    /**
     * Get statistics for each pool. The map keys represent pool name,
     * while the value is the corresponding {@link com.xqbase.apool.stats.AsyncPoolStats} object.
     *
     * @return a map of pool names and statistics.
     */
    public Map<String, PoolStats> getStats() {
        Map<String, PoolStats> map = new HashMap<>();
        for (AsyncPool<Channel> p : pool.values()) {
            map.put(p.getName(), p.getStats());
        }
        return map;
    }

    /**
     * Get the name of the manager.
     */
    public String getName() {
        return name;
    }

}
