// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http;

import java.util.Locale;

public interface HttpResponse extends HttpMessage
{
    StatusLine getStatusLine();
    
    void setStatusLine(final StatusLine p0);
    
    void setStatusLine(final ProtocolVersion p0, final int p1);
    
    void setStatusLine(final ProtocolVersion p0, final int p1, final String p2);
    
    void setStatusCode(final int p0) throws IllegalStateException;
    
    void setReasonPhrase(final String p0) throws IllegalStateException;
    
    HttpEntity getEntity();
    
    void setEntity(final HttpEntity p0);
    
    Locale getLocale();
    
    void setLocale(final Locale p0);
}
