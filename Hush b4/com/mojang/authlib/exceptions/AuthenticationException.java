// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.authlib.exceptions;

public class AuthenticationException extends Exception
{
    public AuthenticationException() {
    }
    
    public AuthenticationException(final String message) {
        super(message);
    }
    
    public AuthenticationException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public AuthenticationException(final Throwable cause) {
        super(cause);
    }
}
