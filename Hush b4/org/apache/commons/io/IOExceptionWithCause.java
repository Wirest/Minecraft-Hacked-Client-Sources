// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io;

import java.io.IOException;

public class IOExceptionWithCause extends IOException
{
    private static final long serialVersionUID = 1L;
    
    public IOExceptionWithCause(final String message, final Throwable cause) {
        super(message);
        this.initCause(cause);
    }
    
    public IOExceptionWithCause(final Throwable cause) {
        super((cause == null) ? null : cause.toString());
        this.initCause(cause);
    }
}
