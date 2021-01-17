// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.concurrent;

public class BlockingOperationException extends IllegalStateException
{
    private static final long serialVersionUID = 2462223247762460301L;
    
    public BlockingOperationException() {
    }
    
    public BlockingOperationException(final String s) {
        super(s);
    }
    
    public BlockingOperationException(final Throwable cause) {
        super(cause);
    }
    
    public BlockingOperationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
