// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.client;

import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.impl.conn.ProxySelectorRoutePlanner;
import java.net.ProxySelector;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.SchemeRegistryFactory;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.apache.http.annotation.ThreadSafe;

@Deprecated
@ThreadSafe
public class SystemDefaultHttpClient extends DefaultHttpClient
{
    public SystemDefaultHttpClient(final HttpParams params) {
        super(null, params);
    }
    
    public SystemDefaultHttpClient() {
        super(null, null);
    }
    
    @Override
    protected ClientConnectionManager createClientConnectionManager() {
        final PoolingClientConnectionManager connmgr = new PoolingClientConnectionManager(SchemeRegistryFactory.createSystemDefault());
        String s = System.getProperty("http.keepAlive", "true");
        if ("true".equalsIgnoreCase(s)) {
            s = System.getProperty("http.maxConnections", "5");
            final int max = Integer.parseInt(s);
            connmgr.setDefaultMaxPerRoute(max);
            connmgr.setMaxTotal(2 * max);
        }
        return connmgr;
    }
    
    @Override
    protected HttpRoutePlanner createHttpRoutePlanner() {
        return new ProxySelectorRoutePlanner(this.getConnectionManager().getSchemeRegistry(), ProxySelector.getDefault());
    }
    
    @Override
    protected ConnectionReuseStrategy createConnectionReuseStrategy() {
        final String s = System.getProperty("http.keepAlive", "true");
        if ("true".equalsIgnoreCase(s)) {
            return new DefaultConnectionReuseStrategy();
        }
        return new NoConnectionReuseStrategy();
    }
}
