// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.ProtocolException;
import org.apache.http.protocol.HttpContext;
import org.apache.http.HttpResponse;
import org.apache.http.HttpRequest;

public interface RedirectStrategy
{
    boolean isRedirected(final HttpRequest p0, final HttpResponse p1, final HttpContext p2) throws ProtocolException;
    
    HttpUriRequest getRedirect(final HttpRequest p0, final HttpResponse p1, final HttpContext p2) throws ProtocolException;
}
