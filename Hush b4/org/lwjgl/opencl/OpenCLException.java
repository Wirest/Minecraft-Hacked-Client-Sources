// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl;

public class OpenCLException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    
    public OpenCLException() {
    }
    
    public OpenCLException(final String message) {
        super(message);
    }
    
    public OpenCLException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public OpenCLException(final Throwable cause) {
        super(cause);
    }
}
