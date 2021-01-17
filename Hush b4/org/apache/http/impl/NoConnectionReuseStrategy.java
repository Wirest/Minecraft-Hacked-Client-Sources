// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl;

import org.apache.http.protocol.HttpContext;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Immutable;
import org.apache.http.ConnectionReuseStrategy;

@Immutable
public class NoConnectionReuseStrategy implements ConnectionReuseStrategy
{
    public static final NoConnectionReuseStrategy INSTANCE;
    
    public boolean keepAlive(final HttpResponse response, final HttpContext context) {
        return false;
    }
    
    static {
        INSTANCE = new NoConnectionReuseStrategy();
    }
}
