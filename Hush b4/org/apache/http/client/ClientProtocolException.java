// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client;

import org.apache.http.annotation.Immutable;
import java.io.IOException;

@Immutable
public class ClientProtocolException extends IOException
{
    private static final long serialVersionUID = -5596590843227115865L;
    
    public ClientProtocolException() {
    }
    
    public ClientProtocolException(final String s) {
        super(s);
    }
    
    public ClientProtocolException(final Throwable cause) {
        this.initCause(cause);
    }
    
    public ClientProtocolException(final String message, final Throwable cause) {
        super(message);
        this.initCause(cause);
    }
}
