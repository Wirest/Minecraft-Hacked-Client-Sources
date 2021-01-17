// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.client;

import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.Header;
import java.util.Queue;
import org.apache.http.HttpResponse;
import java.util.Map;
import org.apache.http.protocol.HttpContext;
import org.apache.http.auth.AuthScheme;
import org.apache.http.HttpHost;
import java.util.Collection;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.annotation.Immutable;

@Immutable
public class TargetAuthenticationStrategy extends AuthenticationStrategyImpl
{
    public static final TargetAuthenticationStrategy INSTANCE;
    
    public TargetAuthenticationStrategy() {
        super(401, "WWW-Authenticate");
    }
    
    @Override
    Collection<String> getPreferredAuthSchemes(final RequestConfig config) {
        return config.getTargetPreferredAuthSchemes();
    }
    
    static {
        INSTANCE = new TargetAuthenticationStrategy();
    }
}
