// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client.config;

import java.util.Collection;
import java.net.InetAddress;
import org.apache.http.HttpHost;

public class RequestConfig implements Cloneable
{
    public static final RequestConfig DEFAULT;
    private final boolean expectContinueEnabled;
    private final HttpHost proxy;
    private final InetAddress localAddress;
    private final boolean staleConnectionCheckEnabled;
    private final String cookieSpec;
    private final boolean redirectsEnabled;
    private final boolean relativeRedirectsAllowed;
    private final boolean circularRedirectsAllowed;
    private final int maxRedirects;
    private final boolean authenticationEnabled;
    private final Collection<String> targetPreferredAuthSchemes;
    private final Collection<String> proxyPreferredAuthSchemes;
    private final int connectionRequestTimeout;
    private final int connectTimeout;
    private final int socketTimeout;
    
    RequestConfig(final boolean expectContinueEnabled, final HttpHost proxy, final InetAddress localAddress, final boolean staleConnectionCheckEnabled, final String cookieSpec, final boolean redirectsEnabled, final boolean relativeRedirectsAllowed, final boolean circularRedirectsAllowed, final int maxRedirects, final boolean authenticationEnabled, final Collection<String> targetPreferredAuthSchemes, final Collection<String> proxyPreferredAuthSchemes, final int connectionRequestTimeout, final int connectTimeout, final int socketTimeout) {
        this.expectContinueEnabled = expectContinueEnabled;
        this.proxy = proxy;
        this.localAddress = localAddress;
        this.staleConnectionCheckEnabled = staleConnectionCheckEnabled;
        this.cookieSpec = cookieSpec;
        this.redirectsEnabled = redirectsEnabled;
        this.relativeRedirectsAllowed = relativeRedirectsAllowed;
        this.circularRedirectsAllowed = circularRedirectsAllowed;
        this.maxRedirects = maxRedirects;
        this.authenticationEnabled = authenticationEnabled;
        this.targetPreferredAuthSchemes = targetPreferredAuthSchemes;
        this.proxyPreferredAuthSchemes = proxyPreferredAuthSchemes;
        this.connectionRequestTimeout = connectionRequestTimeout;
        this.connectTimeout = connectTimeout;
        this.socketTimeout = socketTimeout;
    }
    
    public boolean isExpectContinueEnabled() {
        return this.expectContinueEnabled;
    }
    
    public HttpHost getProxy() {
        return this.proxy;
    }
    
    public InetAddress getLocalAddress() {
        return this.localAddress;
    }
    
    public boolean isStaleConnectionCheckEnabled() {
        return this.staleConnectionCheckEnabled;
    }
    
    public String getCookieSpec() {
        return this.cookieSpec;
    }
    
    public boolean isRedirectsEnabled() {
        return this.redirectsEnabled;
    }
    
    public boolean isRelativeRedirectsAllowed() {
        return this.relativeRedirectsAllowed;
    }
    
    public boolean isCircularRedirectsAllowed() {
        return this.circularRedirectsAllowed;
    }
    
    public int getMaxRedirects() {
        return this.maxRedirects;
    }
    
    public boolean isAuthenticationEnabled() {
        return this.authenticationEnabled;
    }
    
    public Collection<String> getTargetPreferredAuthSchemes() {
        return this.targetPreferredAuthSchemes;
    }
    
    public Collection<String> getProxyPreferredAuthSchemes() {
        return this.proxyPreferredAuthSchemes;
    }
    
    public int getConnectionRequestTimeout() {
        return this.connectionRequestTimeout;
    }
    
    public int getConnectTimeout() {
        return this.connectTimeout;
    }
    
    public int getSocketTimeout() {
        return this.socketTimeout;
    }
    
    @Override
    protected RequestConfig clone() throws CloneNotSupportedException {
        return (RequestConfig)super.clone();
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(", expectContinueEnabled=").append(this.expectContinueEnabled);
        builder.append(", proxy=").append(this.proxy);
        builder.append(", localAddress=").append(this.localAddress);
        builder.append(", staleConnectionCheckEnabled=").append(this.staleConnectionCheckEnabled);
        builder.append(", cookieSpec=").append(this.cookieSpec);
        builder.append(", redirectsEnabled=").append(this.redirectsEnabled);
        builder.append(", relativeRedirectsAllowed=").append(this.relativeRedirectsAllowed);
        builder.append(", maxRedirects=").append(this.maxRedirects);
        builder.append(", circularRedirectsAllowed=").append(this.circularRedirectsAllowed);
        builder.append(", authenticationEnabled=").append(this.authenticationEnabled);
        builder.append(", targetPreferredAuthSchemes=").append(this.targetPreferredAuthSchemes);
        builder.append(", proxyPreferredAuthSchemes=").append(this.proxyPreferredAuthSchemes);
        builder.append(", connectionRequestTimeout=").append(this.connectionRequestTimeout);
        builder.append(", connectTimeout=").append(this.connectTimeout);
        builder.append(", socketTimeout=").append(this.socketTimeout);
        builder.append("]");
        return builder.toString();
    }
    
    public static Builder custom() {
        return new Builder();
    }
    
    public static Builder copy(final RequestConfig config) {
        return new Builder().setExpectContinueEnabled(config.isExpectContinueEnabled()).setProxy(config.getProxy()).setLocalAddress(config.getLocalAddress()).setStaleConnectionCheckEnabled(config.isStaleConnectionCheckEnabled()).setCookieSpec(config.getCookieSpec()).setRedirectsEnabled(config.isRedirectsEnabled()).setRelativeRedirectsAllowed(config.isRelativeRedirectsAllowed()).setCircularRedirectsAllowed(config.isCircularRedirectsAllowed()).setMaxRedirects(config.getMaxRedirects()).setAuthenticationEnabled(config.isAuthenticationEnabled()).setTargetPreferredAuthSchemes(config.getTargetPreferredAuthSchemes()).setProxyPreferredAuthSchemes(config.getProxyPreferredAuthSchemes()).setConnectionRequestTimeout(config.getConnectionRequestTimeout()).setConnectTimeout(config.getConnectTimeout()).setSocketTimeout(config.getSocketTimeout());
    }
    
    static {
        DEFAULT = new Builder().build();
    }
    
    public static class Builder
    {
        private boolean expectContinueEnabled;
        private HttpHost proxy;
        private InetAddress localAddress;
        private boolean staleConnectionCheckEnabled;
        private String cookieSpec;
        private boolean redirectsEnabled;
        private boolean relativeRedirectsAllowed;
        private boolean circularRedirectsAllowed;
        private int maxRedirects;
        private boolean authenticationEnabled;
        private Collection<String> targetPreferredAuthSchemes;
        private Collection<String> proxyPreferredAuthSchemes;
        private int connectionRequestTimeout;
        private int connectTimeout;
        private int socketTimeout;
        
        Builder() {
            this.staleConnectionCheckEnabled = true;
            this.redirectsEnabled = true;
            this.maxRedirects = 50;
            this.relativeRedirectsAllowed = true;
            this.authenticationEnabled = true;
            this.connectionRequestTimeout = -1;
            this.connectTimeout = -1;
            this.socketTimeout = -1;
        }
        
        public Builder setExpectContinueEnabled(final boolean expectContinueEnabled) {
            this.expectContinueEnabled = expectContinueEnabled;
            return this;
        }
        
        public Builder setProxy(final HttpHost proxy) {
            this.proxy = proxy;
            return this;
        }
        
        public Builder setLocalAddress(final InetAddress localAddress) {
            this.localAddress = localAddress;
            return this;
        }
        
        public Builder setStaleConnectionCheckEnabled(final boolean staleConnectionCheckEnabled) {
            this.staleConnectionCheckEnabled = staleConnectionCheckEnabled;
            return this;
        }
        
        public Builder setCookieSpec(final String cookieSpec) {
            this.cookieSpec = cookieSpec;
            return this;
        }
        
        public Builder setRedirectsEnabled(final boolean redirectsEnabled) {
            this.redirectsEnabled = redirectsEnabled;
            return this;
        }
        
        public Builder setRelativeRedirectsAllowed(final boolean relativeRedirectsAllowed) {
            this.relativeRedirectsAllowed = relativeRedirectsAllowed;
            return this;
        }
        
        public Builder setCircularRedirectsAllowed(final boolean circularRedirectsAllowed) {
            this.circularRedirectsAllowed = circularRedirectsAllowed;
            return this;
        }
        
        public Builder setMaxRedirects(final int maxRedirects) {
            this.maxRedirects = maxRedirects;
            return this;
        }
        
        public Builder setAuthenticationEnabled(final boolean authenticationEnabled) {
            this.authenticationEnabled = authenticationEnabled;
            return this;
        }
        
        public Builder setTargetPreferredAuthSchemes(final Collection<String> targetPreferredAuthSchemes) {
            this.targetPreferredAuthSchemes = targetPreferredAuthSchemes;
            return this;
        }
        
        public Builder setProxyPreferredAuthSchemes(final Collection<String> proxyPreferredAuthSchemes) {
            this.proxyPreferredAuthSchemes = proxyPreferredAuthSchemes;
            return this;
        }
        
        public Builder setConnectionRequestTimeout(final int connectionRequestTimeout) {
            this.connectionRequestTimeout = connectionRequestTimeout;
            return this;
        }
        
        public Builder setConnectTimeout(final int connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }
        
        public Builder setSocketTimeout(final int socketTimeout) {
            this.socketTimeout = socketTimeout;
            return this;
        }
        
        public RequestConfig build() {
            return new RequestConfig(this.expectContinueEnabled, this.proxy, this.localAddress, this.staleConnectionCheckEnabled, this.cookieSpec, this.redirectsEnabled, this.relativeRedirectsAllowed, this.circularRedirectsAllowed, this.maxRedirects, this.authenticationEnabled, this.targetPreferredAuthSchemes, this.proxyPreferredAuthSchemes, this.connectionRequestTimeout, this.connectTimeout, this.socketTimeout);
        }
    }
}
