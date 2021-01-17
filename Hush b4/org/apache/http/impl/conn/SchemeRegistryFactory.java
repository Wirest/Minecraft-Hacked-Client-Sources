// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.conn;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.scheme.SchemeSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.annotation.ThreadSafe;

@Deprecated
@ThreadSafe
public final class SchemeRegistryFactory
{
    public static SchemeRegistry createDefault() {
        final SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        registry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
        return registry;
    }
    
    public static SchemeRegistry createSystemDefault() {
        final SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        registry.register(new Scheme("https", 443, SSLSocketFactory.getSystemSocketFactory()));
        return registry;
    }
}
