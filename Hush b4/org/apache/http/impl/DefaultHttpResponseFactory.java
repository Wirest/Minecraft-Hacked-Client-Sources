// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl;

import org.apache.http.StatusLine;
import java.util.Locale;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ProtocolVersion;
import org.apache.http.util.Args;
import org.apache.http.ReasonPhraseCatalog;
import org.apache.http.annotation.Immutable;
import org.apache.http.HttpResponseFactory;

@Immutable
public class DefaultHttpResponseFactory implements HttpResponseFactory
{
    public static final DefaultHttpResponseFactory INSTANCE;
    protected final ReasonPhraseCatalog reasonCatalog;
    
    public DefaultHttpResponseFactory(final ReasonPhraseCatalog catalog) {
        this.reasonCatalog = Args.notNull(catalog, "Reason phrase catalog");
    }
    
    public DefaultHttpResponseFactory() {
        this(EnglishReasonPhraseCatalog.INSTANCE);
    }
    
    public HttpResponse newHttpResponse(final ProtocolVersion ver, final int status, final HttpContext context) {
        Args.notNull(ver, "HTTP version");
        final Locale loc = this.determineLocale(context);
        final String reason = this.reasonCatalog.getReason(status, loc);
        final StatusLine statusline = new BasicStatusLine(ver, status, reason);
        return new BasicHttpResponse(statusline, this.reasonCatalog, loc);
    }
    
    public HttpResponse newHttpResponse(final StatusLine statusline, final HttpContext context) {
        Args.notNull(statusline, "Status line");
        return new BasicHttpResponse(statusline, this.reasonCatalog, this.determineLocale(context));
    }
    
    protected Locale determineLocale(final HttpContext context) {
        return Locale.getDefault();
    }
    
    static {
        INSTANCE = new DefaultHttpResponseFactory();
    }
}
