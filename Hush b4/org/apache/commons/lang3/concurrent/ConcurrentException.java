// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3.concurrent;

public class ConcurrentException extends Exception
{
    private static final long serialVersionUID = 6622707671812226130L;
    
    protected ConcurrentException() {
    }
    
    public ConcurrentException(final Throwable cause) {
        super(ConcurrentUtils.checkedException(cause));
    }
    
    public ConcurrentException(final String msg, final Throwable cause) {
        super(msg, ConcurrentUtils.checkedException(cause));
    }
}
