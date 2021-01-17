// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.authlib.exceptions;

public class UserMigratedException extends InvalidCredentialsException
{
    public UserMigratedException() {
    }
    
    public UserMigratedException(final String message) {
        super(message);
    }
    
    public UserMigratedException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public UserMigratedException(final Throwable cause) {
        super(cause);
    }
}
