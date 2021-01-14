package io.netty.channel;

import io.netty.util.concurrent.EventExecutor;

final class SucceededChannelFuture
        extends CompleteChannelFuture {
    SucceededChannelFuture(Channel paramChannel, EventExecutor paramEventExecutor) {
        super(paramChannel, paramEventExecutor);
    }

    public Throwable cause() {
        return null;
    }

    public boolean isSuccess() {
        return true;
    }
}




