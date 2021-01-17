// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.openal;

public class OpenALException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    
    public OpenALException() {
    }
    
    public OpenALException(final int error_code) {
        super("OpenAL error: " + AL10.alGetString(error_code) + " (" + error_code + ")");
    }
    
    public OpenALException(final String message) {
        super(message);
    }
    
    public OpenALException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public OpenALException(final Throwable cause) {
        super(cause);
    }
}
