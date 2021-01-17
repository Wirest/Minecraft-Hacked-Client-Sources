// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.execchain;

import org.apache.http.HttpResponse;
import org.apache.http.annotation.Immutable;
import org.apache.http.HttpException;

@Immutable
public class TunnelRefusedException extends HttpException
{
    private static final long serialVersionUID = -8646722842745617323L;
    private final HttpResponse response;
    
    public TunnelRefusedException(final String message, final HttpResponse response) {
        super(message);
        this.response = response;
    }
    
    public HttpResponse getResponse() {
        return this.response;
    }
}
