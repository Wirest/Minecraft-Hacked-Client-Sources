// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client;

import org.apache.http.annotation.Immutable;

@Immutable
public class CircularRedirectException extends RedirectException
{
    private static final long serialVersionUID = 6830063487001091803L;
    
    public CircularRedirectException() {
    }
    
    public CircularRedirectException(final String message) {
        super(message);
    }
    
    public CircularRedirectException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
