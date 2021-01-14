package io.netty.channel;

import io.netty.util.concurrent.AbstractFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.concurrent.TimeUnit;

final class VoidChannelPromise
        extends AbstractFuture<Void>
        implements ChannelPromise {
    private final Channel channel;
    private final boolean fireException;

    VoidChannelPromise(Channel paramChannel, boolean paramBoolean) {
        if (paramChannel == null) {
            throw new NullPointerException("channel");
        }
        this.channel = paramChannel;
        this.fireException = paramBoolean;
    }

    private static void fail() {
        throw new IllegalStateException("void future");
    }

    public VoidChannelPromise addListener(GenericFutureListener<? extends Future<? super Void>> paramGenericFutureListener) {
        fail();
        return this;
    }

    public VoidChannelPromise addListeners(GenericFutureListener<? extends Future<? super Void>>... paramVarArgs) {
        fail();
        return this;
    }

    public VoidChannelPromise removeListener(GenericFutureListener<? extends Future<? super Void>> paramGenericFutureListener) {
        return this;
    }

    public VoidChannelPromise removeListeners(GenericFutureListener<? extends Future<? super Void>>... paramVarArgs) {
        return this;
    }

    public VoidChannelPromise await()
            throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        return this;
    }

    public boolean await(long paramLong, TimeUnit paramTimeUnit) {
        fail();
        return false;
    }

    public boolean await(long paramLong) {
        fail();
        return false;
    }

    public VoidChannelPromise awaitUninterruptibly() {
        fail();
        return this;
    }

    public boolean awaitUninterruptibly(long paramLong, TimeUnit paramTimeUnit) {
        fail();
        return false;
    }

    public boolean awaitUninterruptibly(long paramLong) {
        fail();
        return false;
    }

    public Channel channel() {
        return this.channel;
    }

    public boolean isDone() {
        return false;
    }

    public boolean isSuccess() {
        return false;
    }

    public VoidChannelPromise setSuccess(Void paramVoid) {
        return this;
    }

    public boolean setUncancellable() {
        return true;
    }

    public boolean isCancellable() {
        return false;
    }

    public boolean isCancelled() {
        return false;
    }

    public Throwable cause() {
        return null;
    }

    public VoidChannelPromise sync() {
        fail();
        return this;
    }

    public VoidChannelPromise syncUninterruptibly() {
        fail();
        return this;
    }

    public VoidChannelPromise setFailure(Throwable paramThrowable) {
        fireException(paramThrowable);
        return this;
    }

    public VoidChannelPromise setSuccess() {
        return this;
    }

    public boolean tryFailure(Throwable paramThrowable) {
        fireException(paramThrowable);
        return false;
    }

    public boolean cancel(boolean paramBoolean) {
        return false;
    }

    public boolean trySuccess() {
        return false;
    }

    public boolean trySuccess(Void paramVoid) {
        return false;
    }

    public Void getNow() {
        return null;
    }

    private void fireException(Throwable paramThrowable) {
        if ((this.fireException) && (this.channel.isRegistered())) {
            this.channel.pipeline().fireExceptionCaught(paramThrowable);
        }
    }
}




