// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.compression;

import io.netty.handler.codec.DecoderException;

public class DecompressionException extends DecoderException
{
    private static final long serialVersionUID = 3546272712208105199L;
    
    public DecompressionException() {
    }
    
    public DecompressionException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public DecompressionException(final String message) {
        super(message);
    }
    
    public DecompressionException(final Throwable cause) {
        super(cause);
    }
}
