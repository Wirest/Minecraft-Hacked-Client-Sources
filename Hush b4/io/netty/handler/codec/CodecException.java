// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec;

public class CodecException extends RuntimeException
{
    private static final long serialVersionUID = -1464830400709348473L;
    
    public CodecException() {
    }
    
    public CodecException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public CodecException(final String message) {
        super(message);
    }
    
    public CodecException(final Throwable cause) {
        super(cause);
    }
}
