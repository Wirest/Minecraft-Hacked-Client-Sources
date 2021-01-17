// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.conn;

import org.apache.http.HttpException;
import org.apache.http.protocol.HttpContext;
import org.apache.http.HttpRequest;
import org.apache.http.util.Args;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Immutable;

@Immutable
public class DefaultProxyRoutePlanner extends DefaultRoutePlanner
{
    private final HttpHost proxy;
    
    public DefaultProxyRoutePlanner(final HttpHost proxy, final SchemePortResolver schemePortResolver) {
        super(schemePortResolver);
        this.proxy = Args.notNull(proxy, "Proxy host");
    }
    
    public DefaultProxyRoutePlanner(final HttpHost proxy) {
        this(proxy, null);
    }
    
    @Override
    protected HttpHost determineProxy(final HttpHost target, final HttpRequest request, final HttpContext context) throws HttpException {
        return this.proxy;
    }
}
