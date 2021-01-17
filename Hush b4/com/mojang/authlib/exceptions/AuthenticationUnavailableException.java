// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.authlib.exceptions;

public class AuthenticationUnavailableException extends AuthenticationException
{
    public AuthenticationUnavailableException() {
    }
    
    public AuthenticationUnavailableException(final String message) {
        super(message);
    }
    
    public AuthenticationUnavailableException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public AuthenticationUnavailableException(final Throwable cause) {
        super(cause);
    }
}
