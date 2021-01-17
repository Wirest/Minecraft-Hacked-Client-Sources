// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.timeout;

public final class WriteTimeoutException extends TimeoutException
{
    private static final long serialVersionUID = -144786655770296065L;
    public static final WriteTimeoutException INSTANCE;
    
    private WriteTimeoutException() {
    }
    
    static {
        INSTANCE = new WriteTimeoutException();
    }
}
