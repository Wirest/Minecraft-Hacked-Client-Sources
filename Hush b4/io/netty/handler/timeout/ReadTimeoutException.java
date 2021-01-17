// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.timeout;

public final class ReadTimeoutException extends TimeoutException
{
    private static final long serialVersionUID = 169287984113283421L;
    public static final ReadTimeoutException INSTANCE;
    
    private ReadTimeoutException() {
    }
    
    static {
        INSTANCE = new ReadTimeoutException();
    }
}
