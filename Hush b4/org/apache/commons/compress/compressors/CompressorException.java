// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.compressors;

public class CompressorException extends Exception
{
    private static final long serialVersionUID = -2932901310255908814L;
    
    public CompressorException(final String message) {
        super(message);
    }
    
    public CompressorException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
