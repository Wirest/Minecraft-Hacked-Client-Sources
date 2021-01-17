// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

public class IllegalIcuArgumentException extends IllegalArgumentException
{
    private static final long serialVersionUID = 3789261542830211225L;
    
    public IllegalIcuArgumentException(final String errorMessage) {
        super(errorMessage);
    }
    
    public IllegalIcuArgumentException(final Throwable cause) {
        super(cause);
    }
    
    public IllegalIcuArgumentException(final String errorMessage, final Throwable cause) {
        super(errorMessage, cause);
    }
    
    @Override
    public synchronized IllegalIcuArgumentException initCause(final Throwable cause) {
        return (IllegalIcuArgumentException)super.initCause(cause);
    }
}
