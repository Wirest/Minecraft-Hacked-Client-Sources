// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.authlib.yggdrasil;

public class ProfileIncompleteException extends RuntimeException
{
    public ProfileIncompleteException() {
    }
    
    public ProfileIncompleteException(final String message) {
        super(message);
    }
    
    public ProfileIncompleteException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public ProfileIncompleteException(final Throwable cause) {
        super(cause);
    }
}
