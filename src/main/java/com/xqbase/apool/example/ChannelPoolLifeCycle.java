package com.xqbase.apool.example;

import com.xqbase.apool.LifeCycle;
import com.xqbase.apool.callback.Callback;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
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
    public void create(final Callback<Channel> callback) {
        bootstrap.connect(remoteAddress).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    Channel c = future.channel();
                    channelGroup.add(c);
                    callback.onSuccess(c);
                } else {
                    callback.onError(future.cause());
                }
            }
        });
    }

    @Override
    public boolean validateGet(Channel c) {
        return c.isActive();
    }

    @Override
    public boolean validatePut(Channel c) {
        return c.isActive();
    }

    @Override
    public void destroy(Channel c, boolean error, final Callback<Channel> callback) {
        if (c.isOpen()) {
            c.close().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        callback.onSuccess(future.channel());
                    } else {
                        callback.onError(future.cause());
                    }
                }
            });
        } else {
            callback.onSuccess(c);
        }
    }
}
