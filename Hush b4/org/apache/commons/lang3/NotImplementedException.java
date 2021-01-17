// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3;

public class NotImplementedException extends UnsupportedOperationException
{
    private static final long serialVersionUID = 20131021L;
    private final String code;
    
    public NotImplementedException(final String message) {
        this(message, (String)null);
    }
    
    public NotImplementedException(final Throwable cause) {
        this(cause, null);
    }
    
    public NotImplementedException(final String message, final Throwable cause) {
        this(message, cause, null);
    }
    
    public NotImplementedException(final String message, final String code) {
        super(message);
        this.code = code;
    }
    
    public NotImplementedException(final Throwable cause, final String code) {
        super(cause);
        this.code = code;
    }
    
    public NotImplementedException(final String message, final Throwable cause, final String code) {
        super(message, cause);
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
}
