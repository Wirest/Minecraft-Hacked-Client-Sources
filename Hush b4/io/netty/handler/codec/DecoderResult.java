// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec;

import io.netty.util.Signal;

public class DecoderResult
{
    protected static final Signal SIGNAL_UNFINISHED;
    protected static final Signal SIGNAL_SUCCESS;
    public static final DecoderResult UNFINISHED;
    public static final DecoderResult SUCCESS;
    private final Throwable cause;
    
    public static DecoderResult failure(final Throwable cause) {
        if (cause == null) {
            throw new NullPointerException("cause");
        }
        return new DecoderResult(cause);
    }
    
    protected DecoderResult(final Throwable cause) {
        if (cause == null) {
            throw new NullPointerException("cause");
        }
        this.cause = cause;
    }
    
    public boolean isFinished() {
        return this.cause != DecoderResult.SIGNAL_UNFINISHED;
    }
    
    public boolean isSuccess() {
        return this.cause == DecoderResult.SIGNAL_SUCCESS;
    }
    
    public boolean isFailure() {
        return this.cause != DecoderResult.SIGNAL_SUCCESS && this.cause != DecoderResult.SIGNAL_UNFINISHED;
    }
    
    public Throwable cause() {
        if (this.isFailure()) {
            return this.cause;
        }
        return null;
    }
    
    @Override
    public String toString() {
        if (!this.isFinished()) {
            return "unfinished";
        }
        if (this.isSuccess()) {
            return "success";
        }
        final String cause = this.cause().toString();
        final StringBuilder buf = new StringBuilder(cause.length() + 17);
        buf.append("failure(");
        buf.append(cause);
        buf.append(')');
        return buf.toString();
    }
    
    static {
        SIGNAL_UNFINISHED = Signal.valueOf(DecoderResult.class.getName() + ".UNFINISHED");
        SIGNAL_SUCCESS = Signal.valueOf(DecoderResult.class.getName() + ".SUCCESS");
        UNFINISHED = new DecoderResult(DecoderResult.SIGNAL_UNFINISHED);
        SUCCESS = new DecoderResult(DecoderResult.SIGNAL_SUCCESS);
    }
}
