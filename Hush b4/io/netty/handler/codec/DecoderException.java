// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec;

public class DecoderException extends CodecException
{
    private static final long serialVersionUID = 6926716840699621852L;
    
    public DecoderException() {
    }
    
    public DecoderException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public DecoderException(final String message) {
        super(message);
    }
    
    public DecoderException(final Throwable cause) {
        super(cause);
    }
}
