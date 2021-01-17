// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.ssl;

import javax.net.ssl.SSLException;

public class NotSslRecordException extends SSLException
{
    private static final long serialVersionUID = -4316784434770656841L;
    
    public NotSslRecordException() {
        super("");
    }
    
    public NotSslRecordException(final String message) {
        super(message);
    }
    
    public NotSslRecordException(final Throwable cause) {
        super(cause);
    }
    
    public NotSslRecordException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
