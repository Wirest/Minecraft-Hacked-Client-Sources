// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.auth;

import org.apache.http.HttpRequest;
import org.apache.http.protocol.HttpContext;
import java.util.Map;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.params.HttpParams;
import java.util.Locale;
import org.apache.http.util.Args;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.config.Lookup;

@Deprecated
@ThreadSafe
public final class AuthSchemeRegistry implements Lookup<AuthSchemeProvider>
{
    private final ConcurrentHashMap<String, AuthSchemeFactory> registeredSchemes;
    
    public AuthSchemeRegistry() {
        this.registeredSchemes = new ConcurrentHashMap<String, AuthSchemeFactory>();
    }
    
    public void register(final String name, final AuthSchemeFactory factory) {
        Args.notNull(name, "Name");
        Args.notNull(factory, "Authentication scheme factory");
        this.registeredSchemes.put(name.toLowerCase(Locale.ENGLISH), factory);
    }
    
    public void unregister(final String name) {
        Args.notNull(name, "Name");
        this.registeredSchemes.remove(name.toLowerCase(Locale.ENGLISH));
    }
    
    public AuthScheme getAuthScheme(final String name, final HttpParams params) throws IllegalStateException {
        Args.notNull(name, "Name");
        final AuthSchemeFactory factory = this.registeredSchemes.get(name.toLowerCase(Locale.ENGLISH));
        if (factory != null) {
            return factory.newInstance(params);
        }
        throw new IllegalStateException("Unsupported authentication scheme: " + name);
    }
    
    public List<String> getSchemeNames() {
        return new ArrayList<String>(this.registeredSchemes.keySet());
    }
    
    public void setItems(final Map<String, AuthSchemeFactory> map) {
        if (map == null) {
            return;
        }
        this.registeredSchemes.clear();
        this.registeredSchemes.putAll(map);
    }
    
    public AuthSchemeProvider lookup(final String name) {
        return new AuthSchemeProvider() {
            public AuthScheme create(final HttpContext context) {
                final HttpRequest request = (HttpRequest)context.getAttribute("http.request");
                return AuthSchemeRegistry.this.getAuthScheme(name, request.getParams());
            }
        };
    }
}
