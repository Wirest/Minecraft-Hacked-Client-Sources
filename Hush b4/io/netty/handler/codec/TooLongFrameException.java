// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec;

public class TooLongFrameException extends DecoderException
{
    private static final long serialVersionUID = -1995801950698951640L;
    
    public TooLongFrameException() {
    }
    
    public TooLongFrameException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public TooLongFrameException(final String message) {
        super(message);
    }
    
    public TooLongFrameException(final Throwable cause) {
        super(cause);
    }
}
