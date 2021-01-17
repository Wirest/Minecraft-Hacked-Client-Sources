// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client;

import org.apache.http.annotation.Immutable;
import org.apache.http.ProtocolException;

@Immutable
public class RedirectException extends ProtocolException
{
    private static final long serialVersionUID = 4418824536372559326L;
    
    public RedirectException() {
    }
    
    public RedirectException(final String message) {
        super(message);
    }
    
    public RedirectException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
