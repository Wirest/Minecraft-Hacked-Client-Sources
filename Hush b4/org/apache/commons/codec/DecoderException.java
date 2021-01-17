// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.codec;

public class DecoderException extends Exception
{
    private static final long serialVersionUID = 1L;
    
    public DecoderException() {
    }
    
    public DecoderException(final String message) {
        super(message);
    }
    
    public DecoderException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public DecoderException(final Throwable cause) {
        super(cause);
    }
}
