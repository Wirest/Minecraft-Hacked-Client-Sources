// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http.cors;

import java.util.Date;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import io.netty.util.internal.StringUtil;
import java.util.Iterator;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaders;
import java.util.Collections;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.concurrent.Callable;
import java.util.Map;
import io.netty.handler.codec.http.HttpMethod;
import java.util.Set;

public final class CorsConfig
{
    private final Set<String> origins;
    private final boolean anyOrigin;
    private final boolean enabled;
    private final Set<String> exposeHeaders;
    private final boolean allowCredentials;
    private final long maxAge;
    private final Set<HttpMethod> allowedRequestMethods;
    private final Set<String> allowedRequestHeaders;
    private final boolean allowNullOrigin;
    private final Map<CharSequence, Callable<?>> preflightHeaders;
    private final boolean shortCurcuit;
    
    private CorsConfig(final Builder builder) {
        this.origins = new LinkedHashSet<String>(builder.origins);
        this.anyOrigin = builder.anyOrigin;
        this.enabled = builder.enabled;
        this.exposeHeaders = builder.exposeHeaders;
        this.allowCredentials = builder.allowCredentials;
        this.maxAge = builder.maxAge;
        this.allowedRequestMethods = builder.requestMethods;
        this.allowedRequestHeaders = builder.requestHeaders;
        this.allowNullOrigin = builder.allowNullOrigin;
        this.preflightHeaders = builder.preflightHeaders;
        this.shortCurcuit = builder.shortCurcuit;
    }
    
    public boolean isCorsSupportEnabled() {
        return this.enabled;
    }
    
    public boolean isAnyOriginSupported() {
        return this.anyOrigin;
    }
    
    public String origin() {
        return this.origins.isEmpty() ? "*" : this.origins.iterator().next();
    }
    
    public Set<String> origins() {
        return this.origins;
    }
    
    public boolean isNullOriginAllowed() {
        return this.allowNullOrigin;
    }
    
    public Set<String> exposedHeaders() {
        return Collections.unmodifiableSet((Set<? extends String>)this.exposeHeaders);
    }
    
    public boolean isCredentialsAllowed() {
        return this.allowCredentials;
    }
    
    public long maxAge() {
        return this.maxAge;
    }
    
    public Set<HttpMethod> allowedRequestMethods() {
        return Collections.unmodifiableSet((Set<? extends HttpMethod>)this.allowedRequestMethods);
    }
    
    public Set<String> allowedRequestHeaders() {
        return Collections.unmodifiableSet((Set<? extends String>)this.allowedRequestHeaders);
    }
    
    public HttpHeaders preflightResponseHeaders() {
        if (this.preflightHeaders.isEmpty()) {
            return HttpHeaders.EMPTY_HEADERS;
        }
        final HttpHeaders preflightHeaders = new DefaultHttpHeaders();
        for (final Map.Entry<CharSequence, Callable<?>> entry : this.preflightHeaders.entrySet()) {
            final Object value = getValue(entry.getValue());
            if (value instanceof Iterable) {
                preflightHeaders.add(entry.getKey(), (Iterable<?>)value);
            }
            else {
                preflightHeaders.add(entry.getKey(), value);
            }
        }
        return preflightHeaders;
    }
    
    public boolean isShortCurcuit() {
        return this.shortCurcuit;
    }
    
    private static <T> T getValue(final Callable<T> callable) {
        try {
            return callable.call();
        }
        catch (Exception e) {
            throw new IllegalStateException("Could not generate value for callable [" + callable + ']', e);
        }
    }
    
    @Override
    public String toString() {
        return StringUtil.simpleClassName(this) + "[enabled=" + this.enabled + ", origins=" + this.origins + ", anyOrigin=" + this.anyOrigin + ", exposedHeaders=" + this.exposeHeaders + ", isCredentialsAllowed=" + this.allowCredentials + ", maxAge=" + this.maxAge + ", allowedRequestMethods=" + this.allowedRequestMethods + ", allowedRequestHeaders=" + this.allowedRequestHeaders + ", preflightHeaders=" + this.preflightHeaders + ']';
    }
    
    public static Builder withAnyOrigin() {
        return new Builder();
    }
    
    public static Builder withOrigin(final String origin) {
        if ("*".equals(origin)) {
            return new Builder();
        }
        return new Builder(new String[] { origin });
    }
    
    public static Builder withOrigins(final String... origins) {
        return new Builder(origins);
    }
    
    public static class Builder
    {
        private final Set<String> origins;
        private final boolean anyOrigin;
        private boolean allowNullOrigin;
        private boolean enabled;
        private boolean allowCredentials;
        private final Set<String> exposeHeaders;
        private long maxAge;
        private final Set<HttpMethod> requestMethods;
        private final Set<String> requestHeaders;
        private final Map<CharSequence, Callable<?>> preflightHeaders;
        private boolean noPreflightHeaders;
        private boolean shortCurcuit;
        
        public Builder(final String... origins) {
            this.enabled = true;
            this.exposeHeaders = new HashSet<String>();
            this.requestMethods = new HashSet<HttpMethod>();
            this.requestHeaders = new HashSet<String>();
            this.preflightHeaders = new HashMap<CharSequence, Callable<?>>();
            this.origins = new LinkedHashSet<String>(Arrays.asList(origins));
            this.anyOrigin = false;
        }
        
        public Builder() {
            this.enabled = true;
            this.exposeHeaders = new HashSet<String>();
            this.requestMethods = new HashSet<HttpMethod>();
            this.requestHeaders = new HashSet<String>();
            this.preflightHeaders = new HashMap<CharSequence, Callable<?>>();
            this.anyOrigin = true;
            this.origins = Collections.emptySet();
        }
        
        public Builder allowNullOrigin() {
            this.allowNullOrigin = true;
            return this;
        }
        
        public Builder disable() {
            this.enabled = false;
            return this;
        }
        
        public Builder exposeHeaders(final String... headers) {
            this.exposeHeaders.addAll(Arrays.asList(headers));
            return this;
        }
        
        public Builder allowCredentials() {
            this.allowCredentials = true;
            return this;
        }
        
        public Builder maxAge(final long max) {
            this.maxAge = max;
            return this;
        }
        
        public Builder allowedRequestMethods(final HttpMethod... methods) {
            this.requestMethods.addAll(Arrays.asList(methods));
            return this;
        }
        
        public Builder allowedRequestHeaders(final String... headers) {
            this.requestHeaders.addAll(Arrays.asList(headers));
            return this;
        }
        
        public Builder preflightResponseHeader(final CharSequence name, final Object... values) {
            if (values.length == 1) {
                this.preflightHeaders.put(name, new ConstantValueGenerator(values[0]));
            }
            else {
                this.preflightResponseHeader(name, Arrays.asList(values));
            }
            return this;
        }
        
        public <T> Builder preflightResponseHeader(final CharSequence name, final Iterable<T> value) {
            this.preflightHeaders.put(name, new ConstantValueGenerator((Object)value));
            return this;
        }
        
        public <T> Builder preflightResponseHeader(final String name, final Callable<T> valueGenerator) {
            this.preflightHeaders.put(name, valueGenerator);
            return this;
        }
        
        public Builder noPreflightResponseHeaders() {
            this.noPreflightHeaders = true;
            return this;
        }
        
        public CorsConfig build() {
            if (this.preflightHeaders.isEmpty() && !this.noPreflightHeaders) {
                this.preflightHeaders.put("Date", new DateValueGenerator());
                this.preflightHeaders.put("Content-Length", new ConstantValueGenerator((Object)"0"));
            }
            return new CorsConfig(this, null);
        }
        
        public Builder shortCurcuit() {
            this.shortCurcuit = true;
            return this;
        }
    }
    
    private static final class ConstantValueGenerator implements Callable<Object>
    {
        private final Object value;
        
        private ConstantValueGenerator(final Object value) {
            if (value == null) {
                throw new IllegalArgumentException("value must not be null");
            }
            this.value = value;
        }
        
        @Override
        public Object call() {
            return this.value;
        }
    }
    
    public static final class DateValueGenerator implements Callable<Date>
    {
        @Override
        public Date call() throws Exception {
            return new Date();
        }
    }
}
