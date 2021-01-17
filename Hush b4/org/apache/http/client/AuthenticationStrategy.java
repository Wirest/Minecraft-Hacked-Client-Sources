// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client;

import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthOption;
import java.util.Queue;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.Header;
import java.util.Map;
import org.apache.http.protocol.HttpContext;
import org.apache.http.HttpResponse;
import org.apache.http.HttpHost;

public interface AuthenticationStrategy
{
    boolean isAuthenticationRequested(final HttpHost p0, final HttpResponse p1, final HttpContext p2);
    
    Map<String, Header> getChallenges(final HttpHost p0, final HttpResponse p1, final HttpContext p2) throws MalformedChallengeException;
    
    Queue<AuthOption> select(final Map<String, Header> p0, final HttpHost p1, final HttpResponse p2, final HttpContext p3) throws MalformedChallengeException;
    
    void authSucceeded(final HttpHost p0, final AuthScheme p1, final HttpContext p2);
    
    void authFailed(final HttpHost p0, final AuthScheme p1, final HttpContext p2);
}
