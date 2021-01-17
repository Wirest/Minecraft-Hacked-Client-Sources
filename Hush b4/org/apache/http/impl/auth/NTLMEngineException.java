// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.auth;

import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthenticationException;

@Immutable
public class NTLMEngineException extends AuthenticationException
{
    private static final long serialVersionUID = 6027981323731768824L;
    
    public NTLMEngineException() {
    }
    
    public NTLMEngineException(final String message) {
        super(message);
    }
    
    public NTLMEngineException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
