// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec;

public class EncoderException extends CodecException
{
    private static final long serialVersionUID = -5086121160476476774L;
    
    public EncoderException() {
    }
    
    public EncoderException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public EncoderException(final String message) {
        super(message);
    }
    
    public EncoderException(final Throwable cause) {
        super(cause);
    }
}
