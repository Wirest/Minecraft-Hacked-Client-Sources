// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.conn;

import java.util.List;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URISyntaxException;
import org.apache.http.HttpException;
import java.net.URI;
import org.apache.http.protocol.HttpContext;
import org.apache.http.HttpRequest;
import org.apache.http.HttpHost;
import org.apache.http.conn.SchemePortResolver;
import java.net.ProxySelector;
import org.apache.http.annotation.Immutable;

@Immutable
public class SystemDefaultRoutePlanner extends DefaultRoutePlanner
{
    private final ProxySelector proxySelector;
    
    public SystemDefaultRoutePlanner(final SchemePortResolver schemePortResolver, final ProxySelector proxySelector) {
        super(schemePortResolver);
        this.proxySelector = ((proxySelector != null) ? proxySelector : ProxySelector.getDefault());
    }
    
    public SystemDefaultRoutePlanner(final ProxySelector proxySelector) {
        this(null, proxySelector);
    }
    
    @Override
    protected HttpHost determineProxy(final HttpHost target, final HttpRequest request, final HttpContext context) throws HttpException {
        URI targetURI;
        try {
            targetURI = new URI(target.toURI());
        }
        catch (URISyntaxException ex) {
            throw new HttpException("Cannot convert host to URI: " + target, ex);
        }
        final List<Proxy> proxies = this.proxySelector.select(targetURI);
        final Proxy p = this.chooseProxy(proxies);
        HttpHost result = null;
        if (p.type() == Proxy.Type.HTTP) {
            if (!(p.address() instanceof InetSocketAddress)) {
                throw new HttpException("Unable to handle non-Inet proxy address: " + p.address());
            }
            final InetSocketAddress isa = (InetSocketAddress)p.address();
            result = new HttpHost(this.getHost(isa), isa.getPort());
        }
        return result;
    }
    
    private String getHost(final InetSocketAddress isa) {
        return isa.isUnresolved() ? isa.getHostName() : isa.getAddress().getHostAddress();
    }
    
    private Proxy chooseProxy(final List<Proxy> proxies) {
        Proxy result = null;
        for (int i = 0; result == null && i < proxies.size(); ++i) {
            final Proxy p = proxies.get(i);
            switch (p.type()) {
                case DIRECT:
                case HTTP: {
                    result = p;
                    break;
                }
            }
        }
        if (result == null) {
            result = Proxy.NO_PROXY;
        }
        return result;
    }
}
