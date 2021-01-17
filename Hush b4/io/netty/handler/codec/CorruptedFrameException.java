// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec;

public class CorruptedFrameException extends DecoderException
{
    private static final long serialVersionUID = 3918052232492988408L;
    
    public CorruptedFrameException() {
    }
    
    public CorruptedFrameException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public CorruptedFrameException(final String message) {
        super(message);
    }
    
    public CorruptedFrameException(final Throwable cause) {
        super(cause);
    }
}
