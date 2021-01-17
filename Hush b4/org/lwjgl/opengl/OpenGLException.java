// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

public class OpenGLException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    
    public OpenGLException(final int gl_error_code) {
        this(createErrorMessage(gl_error_code));
    }
    
    private static String createErrorMessage(final int gl_error_code) {
        final String error_string = Util.translateGLErrorString(gl_error_code);
        return error_string + " (" + gl_error_code + ")";
    }
    
    public OpenGLException() {
    }
    
    public OpenGLException(final String message) {
        super(message);
    }
    
    public OpenGLException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public OpenGLException(final Throwable cause) {
        super(cause);
    }
}
