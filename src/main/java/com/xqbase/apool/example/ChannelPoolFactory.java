package com.xqbase.apool.example;

import com.xqbase.apool.AsyncPool;
import io.netty.channel.Channel;

import java.net.SocketAddress;

/**
 * Interface of channel pool factory.
 *
 * @author Tony He
 */
public interface ChannelPoolFactory {

    AsyncPool<Channel> getPool(SocketAddress address);
}
