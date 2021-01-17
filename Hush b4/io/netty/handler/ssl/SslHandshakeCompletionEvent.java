// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.ssl;

public final class SslHandshakeCompletionEvent
{
    public static final SslHandshakeCompletionEvent SUCCESS;
    private final Throwable cause;
    
    private SslHandshakeCompletionEvent() {
        this.cause = null;
    }
    
    public SslHandshakeCompletionEvent(final Throwable cause) {
        if (cause == null) {
            throw new NullPointerException("cause");
        }
        this.cause = cause;
    }
    
    public boolean isSuccess() {
        return this.cause == null;
    }
    
    public Throwable cause() {
        return this.cause;
    }
    
    static {
        SUCCESS = new SslHandshakeCompletionEvent();
    }
}
