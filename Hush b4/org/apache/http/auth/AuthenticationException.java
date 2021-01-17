// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.auth;

import org.apache.http.annotation.Immutable;
import org.apache.http.ProtocolException;

@Immutable
public class AuthenticationException extends ProtocolException
{
    private static final long serialVersionUID = -6794031905674764776L;
    
    public AuthenticationException() {
    }
    
    public AuthenticationException(final String message) {
        super(message);
    }
    
    public AuthenticationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
