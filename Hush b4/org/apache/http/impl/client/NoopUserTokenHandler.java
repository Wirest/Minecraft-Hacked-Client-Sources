// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.client;

import org.apache.http.protocol.HttpContext;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.UserTokenHandler;

@Immutable
public class NoopUserTokenHandler implements UserTokenHandler
{
    public static final NoopUserTokenHandler INSTANCE;
    
    public Object getUserToken(final HttpContext context) {
        return null;
    }
    
    static {
        INSTANCE = new NoopUserTokenHandler();
    }
}
