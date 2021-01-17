// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.client;

import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.annotation.Immutable;

@Immutable
public class HttpClients
{
    private HttpClients() {
    }
    
    public static HttpClientBuilder custom() {
        return HttpClientBuilder.create();
    }
    
    public static CloseableHttpClient createDefault() {
        return HttpClientBuilder.create().build();
    }
    
    public static CloseableHttpClient createSystem() {
        return HttpClientBuilder.create().useSystemProperties().build();
    }
    
    public static CloseableHttpClient createMinimal() {
        return new MinimalHttpClient(new PoolingHttpClientConnectionManager());
    }
    
    public static CloseableHttpClient createMinimal(final HttpClientConnectionManager connManager) {
        return new MinimalHttpClient(connManager);
    }
}
