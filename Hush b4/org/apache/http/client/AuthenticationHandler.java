// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client;

import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.Header;
import java.util.Map;
import org.apache.http.protocol.HttpContext;
import org.apache.http.HttpResponse;

@Deprecated
public interface AuthenticationHandler
{
    boolean isAuthenticationRequested(final HttpResponse p0, final HttpContext p1);
    
    Map<String, Header> getChallenges(final HttpResponse p0, final HttpContext p1) throws MalformedChallengeException;
    
    AuthScheme selectScheme(final Map<String, Header> p0, final HttpResponse p1, final HttpContext p2) throws AuthenticationException;
}
