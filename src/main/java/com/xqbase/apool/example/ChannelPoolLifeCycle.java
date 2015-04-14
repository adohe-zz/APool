package com.xqbase.apool.example;

import com.xqbase.apool.LifeCycle;
import com.xqbase.apool.callback.Callback;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

import java.net.SocketAddress;

/**
 * The channel pool lifecycle control the lifecycle of a channel.
 *
 * @author Tony He
 */
public class ChannelPoolLifeCycle implements LifeCycle<Channel> {

    private final SocketAddress remoteAddress;
    private final ChannelGroup channelGroup;
    private final Bootstrap bootstrap;

    public ChannelPoolLifeCycle(SocketAddress remoteAddress, ChannelGroup channelGroup, Bootstrap bootstrap) {
        this.remoteAddress = remoteAddress;
        this.channelGroup = channelGroup;
        this.bootstrap = bootstrap;
    }

    @Override
    public void create(Callback<Channel> callback) {

    }

    @Override
    public boolean validateGet(Channel obj) {
        return false;
    }

    @Override
    public boolean validatePut(Channel obj) {
        return false;
    }

    @Override
    public void destroy(Channel obj, boolean error, Callback<Channel> callback) {

    }
}
