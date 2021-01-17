// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.client;

import org.apache.http.util.VersionInfo;
import java.util.Iterator;
import org.apache.http.impl.cookie.RFC2109SpecFactory;
import org.apache.http.impl.cookie.IgnoreSpecFactory;
import org.apache.http.impl.cookie.NetscapeDraftSpecFactory;
import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
import org.apache.http.impl.cookie.RFC2965SpecFactory;
import org.apache.http.impl.cookie.BestMatchSpecFactory;
import org.apache.http.impl.auth.KerberosSchemeFactory;
import org.apache.http.impl.auth.SPNegoSchemeFactory;
import org.apache.http.impl.auth.NTLMSchemeFactory;
import org.apache.http.impl.auth.DigestSchemeFactory;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.execchain.BackoffStrategyExec;
import org.apache.http.impl.execchain.ServiceUnavailableRetryExec;
import org.apache.http.impl.execchain.RedirectExec;
import org.apache.http.impl.conn.DefaultRoutePlanner;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import java.net.ProxySelector;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.DefaultSchemePortResolver;
import org.apache.http.impl.execchain.RetryExec;
import org.apache.http.impl.execchain.ProtocolExec;
import org.apache.http.client.protocol.ResponseContentEncoding;
import org.apache.http.client.protocol.ResponseProcessCookies;
import org.apache.http.client.protocol.RequestAuthCache;
import org.apache.http.client.protocol.RequestAcceptEncoding;
import org.apache.http.client.protocol.RequestAddCookies;
import org.apache.http.client.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.client.protocol.RequestClientConnControl;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestContent;
import org.apache.http.client.protocol.RequestDefaultHeaders;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.impl.execchain.MainClientExec;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.config.Registry;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ssl.SSLContexts;
import javax.net.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.util.TextUtils;
import java.util.ArrayList;
import org.apache.http.impl.execchain.ClientExecChain;
import java.io.Closeable;
import java.util.List;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.Header;
import java.util.Collection;
import org.apache.http.HttpHost;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.config.Lookup;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.BackoffManager;
import org.apache.http.client.ConnectionBackoffStrategy;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpRequestInterceptor;
import java.util.LinkedList;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.HttpClientConnectionManager;
import javax.net.ssl.SSLContext;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class HttpClientBuilder
{
    private HttpRequestExecutor requestExec;
    private X509HostnameVerifier hostnameVerifier;
    private LayeredConnectionSocketFactory sslSocketFactory;
    private SSLContext sslcontext;
    private HttpClientConnectionManager connManager;
    private SchemePortResolver schemePortResolver;
    private ConnectionReuseStrategy reuseStrategy;
    private ConnectionKeepAliveStrategy keepAliveStrategy;
    private AuthenticationStrategy targetAuthStrategy;
    private AuthenticationStrategy proxyAuthStrategy;
    private UserTokenHandler userTokenHandler;
    private HttpProcessor httpprocessor;
    private LinkedList<HttpRequestInterceptor> requestFirst;
    private LinkedList<HttpRequestInterceptor> requestLast;
    private LinkedList<HttpResponseInterceptor> responseFirst;
    private LinkedList<HttpResponseInterceptor> responseLast;
    private HttpRequestRetryHandler retryHandler;
    private HttpRoutePlanner routePlanner;
    private RedirectStrategy redirectStrategy;
    private ConnectionBackoffStrategy connectionBackoffStrategy;
    private BackoffManager backoffManager;
    private ServiceUnavailableRetryStrategy serviceUnavailStrategy;
    private Lookup<AuthSchemeProvider> authSchemeRegistry;
    private Lookup<CookieSpecProvider> cookieSpecRegistry;
    private CookieStore cookieStore;
    private CredentialsProvider credentialsProvider;
    private String userAgent;
    private HttpHost proxy;
    private Collection<? extends Header> defaultHeaders;
    private SocketConfig defaultSocketConfig;
    private ConnectionConfig defaultConnectionConfig;
    private RequestConfig defaultRequestConfig;
    private boolean systemProperties;
    private boolean redirectHandlingDisabled;
    private boolean automaticRetriesDisabled;
    private boolean contentCompressionDisabled;
    private boolean cookieManagementDisabled;
    private boolean authCachingDisabled;
    private boolean connectionStateDisabled;
    private int maxConnTotal;
    private int maxConnPerRoute;
    private List<Closeable> closeables;
    static final String DEFAULT_USER_AGENT;
    
    public static HttpClientBuilder create() {
        return new HttpClientBuilder();
    }
    
    protected HttpClientBuilder() {
        this.maxConnTotal = 0;
        this.maxConnPerRoute = 0;
    }
    
    public final HttpClientBuilder setRequestExecutor(final HttpRequestExecutor requestExec) {
        this.requestExec = requestExec;
        return this;
    }
    
    public final HttpClientBuilder setHostnameVerifier(final X509HostnameVerifier hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier;
        return this;
    }
    
    public final HttpClientBuilder setSslcontext(final SSLContext sslcontext) {
        this.sslcontext = sslcontext;
        return this;
    }
    
    public final HttpClientBuilder setSSLSocketFactory(final LayeredConnectionSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
        return this;
    }
    
    public final HttpClientBuilder setMaxConnTotal(final int maxConnTotal) {
        this.maxConnTotal = maxConnTotal;
        return this;
    }
    
    public final HttpClientBuilder setMaxConnPerRoute(final int maxConnPerRoute) {
        this.maxConnPerRoute = maxConnPerRoute;
        return this;
    }
    
    public final HttpClientBuilder setDefaultSocketConfig(final SocketConfig config) {
        this.defaultSocketConfig = config;
        return this;
    }
    
    public final HttpClientBuilder setDefaultConnectionConfig(final ConnectionConfig config) {
        this.defaultConnectionConfig = config;
        return this;
    }
    
    public final HttpClientBuilder setConnectionManager(final HttpClientConnectionManager connManager) {
        this.connManager = connManager;
        return this;
    }
    
    public final HttpClientBuilder setConnectionReuseStrategy(final ConnectionReuseStrategy reuseStrategy) {
        this.reuseStrategy = reuseStrategy;
        return this;
    }
    
    public final HttpClientBuilder setKeepAliveStrategy(final ConnectionKeepAliveStrategy keepAliveStrategy) {
        this.keepAliveStrategy = keepAliveStrategy;
        return this;
    }
    
    public final HttpClientBuilder setTargetAuthenticationStrategy(final AuthenticationStrategy targetAuthStrategy) {
        this.targetAuthStrategy = targetAuthStrategy;
        return this;
    }
    
    public final HttpClientBuilder setProxyAuthenticationStrategy(final AuthenticationStrategy proxyAuthStrategy) {
        this.proxyAuthStrategy = proxyAuthStrategy;
        return this;
    }
    
    public final HttpClientBuilder setUserTokenHandler(final UserTokenHandler userTokenHandler) {
        this.userTokenHandler = userTokenHandler;
        return this;
    }
    
    public final HttpClientBuilder disableConnectionState() {
        this.connectionStateDisabled = true;
        return this;
    }
    
    public final HttpClientBuilder setSchemePortResolver(final SchemePortResolver schemePortResolver) {
        this.schemePortResolver = schemePortResolver;
        return this;
    }
    
    public final HttpClientBuilder setUserAgent(final String userAgent) {
        this.userAgent = userAgent;
        return this;
    }
    
    public final HttpClientBuilder setDefaultHeaders(final Collection<? extends Header> defaultHeaders) {
        this.defaultHeaders = defaultHeaders;
        return this;
    }
    
    public final HttpClientBuilder addInterceptorFirst(final HttpResponseInterceptor itcp) {
        if (itcp == null) {
            return this;
        }
        if (this.responseFirst == null) {
            this.responseFirst = new LinkedList<HttpResponseInterceptor>();
        }
        this.responseFirst.addFirst(itcp);
        return this;
    }
    
    public final HttpClientBuilder addInterceptorLast(final HttpResponseInterceptor itcp) {
        if (itcp == null) {
            return this;
        }
        if (this.responseLast == null) {
            this.responseLast = new LinkedList<HttpResponseInterceptor>();
        }
        this.responseLast.addLast(itcp);
        return this;
    }
    
    public final HttpClientBuilder addInterceptorFirst(final HttpRequestInterceptor itcp) {
        if (itcp == null) {
            return this;
        }
        if (this.requestFirst == null) {
            this.requestFirst = new LinkedList<HttpRequestInterceptor>();
        }
        this.requestFirst.addFirst(itcp);
        return this;
    }
    
    public final HttpClientBuilder addInterceptorLast(final HttpRequestInterceptor itcp) {
        if (itcp == null) {
            return this;
        }
        if (this.requestLast == null) {
            this.requestLast = new LinkedList<HttpRequestInterceptor>();
        }
        this.requestLast.addLast(itcp);
        return this;
    }
    
    public final HttpClientBuilder disableCookieManagement() {
        this.cookieManagementDisabled = true;
        return this;
    }
    
    public final HttpClientBuilder disableContentCompression() {
        this.contentCompressionDisabled = true;
        return this;
    }
    
    public final HttpClientBuilder disableAuthCaching() {
        this.authCachingDisabled = true;
        return this;
    }
    
    public final HttpClientBuilder setHttpProcessor(final HttpProcessor httpprocessor) {
        this.httpprocessor = httpprocessor;
        return this;
    }
    
    public final HttpClientBuilder setRetryHandler(final HttpRequestRetryHandler retryHandler) {
        this.retryHandler = retryHandler;
        return this;
    }
    
    public final HttpClientBuilder disableAutomaticRetries() {
        this.automaticRetriesDisabled = true;
        return this;
    }
    
    public final HttpClientBuilder setProxy(final HttpHost proxy) {
        this.proxy = proxy;
        return this;
    }
    
    public final HttpClientBuilder setRoutePlanner(final HttpRoutePlanner routePlanner) {
        this.routePlanner = routePlanner;
        return this;
    }
    
    public final HttpClientBuilder setRedirectStrategy(final RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
        return this;
    }
    
    public final HttpClientBuilder disableRedirectHandling() {
        this.redirectHandlingDisabled = true;
        return this;
    }
    
    public final HttpClientBuilder setConnectionBackoffStrategy(final ConnectionBackoffStrategy connectionBackoffStrategy) {
        this.connectionBackoffStrategy = connectionBackoffStrategy;
        return this;
    }
    
    public final HttpClientBuilder setBackoffManager(final BackoffManager backoffManager) {
        this.backoffManager = backoffManager;
        return this;
    }
    
    public final HttpClientBuilder setServiceUnavailableRetryStrategy(final ServiceUnavailableRetryStrategy serviceUnavailStrategy) {
        this.serviceUnavailStrategy = serviceUnavailStrategy;
        return this;
    }
    
    public final HttpClientBuilder setDefaultCookieStore(final CookieStore cookieStore) {
        this.cookieStore = cookieStore;
        return this;
    }
    
    public final HttpClientBuilder setDefaultCredentialsProvider(final CredentialsProvider credentialsProvider) {
        this.credentialsProvider = credentialsProvider;
        return this;
    }
    
    public final HttpClientBuilder setDefaultAuthSchemeRegistry(final Lookup<AuthSchemeProvider> authSchemeRegistry) {
        this.authSchemeRegistry = authSchemeRegistry;
        return this;
    }
    
    public final HttpClientBuilder setDefaultCookieSpecRegistry(final Lookup<CookieSpecProvider> cookieSpecRegistry) {
        this.cookieSpecRegistry = cookieSpecRegistry;
        return this;
    }
    
    public final HttpClientBuilder setDefaultRequestConfig(final RequestConfig config) {
        this.defaultRequestConfig = config;
        return this;
    }
    
    public final HttpClientBuilder useSystemProperties() {
        this.systemProperties = true;
        return this;
    }
    
    protected ClientExecChain decorateMainExec(final ClientExecChain mainExec) {
        return mainExec;
    }
    
    protected ClientExecChain decorateProtocolExec(final ClientExecChain protocolExec) {
        return protocolExec;
    }
    
    protected void addCloseable(final Closeable closeable) {
        if (closeable == null) {
            return;
        }
        if (this.closeables == null) {
            this.closeables = new ArrayList<Closeable>();
        }
        this.closeables.add(closeable);
    }
    
    private static String[] split(final String s) {
        if (TextUtils.isBlank(s)) {
            return null;
        }
        return s.split(" *, *");
    }
    
    public CloseableHttpClient build() {
        HttpRequestExecutor requestExec = this.requestExec;
        if (requestExec == null) {
            requestExec = new HttpRequestExecutor();
        }
        HttpClientConnectionManager connManager = this.connManager;
        if (connManager == null) {
            LayeredConnectionSocketFactory sslSocketFactory = this.sslSocketFactory;
            if (sslSocketFactory == null) {
                final String[] supportedProtocols = (String[])(this.systemProperties ? split(System.getProperty("https.protocols")) : null);
                final String[] supportedCipherSuites = (String[])(this.systemProperties ? split(System.getProperty("https.cipherSuites")) : null);
                X509HostnameVerifier hostnameVerifier = this.hostnameVerifier;
                if (hostnameVerifier == null) {
                    hostnameVerifier = SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
                }
                if (this.sslcontext != null) {
                    sslSocketFactory = new SSLConnectionSocketFactory(this.sslcontext, supportedProtocols, supportedCipherSuites, hostnameVerifier);
                }
                else if (this.systemProperties) {
                    sslSocketFactory = new SSLConnectionSocketFactory((SSLSocketFactory)SSLSocketFactory.getDefault(), supportedProtocols, supportedCipherSuites, hostnameVerifier);
                }
                else {
                    sslSocketFactory = new SSLConnectionSocketFactory(SSLContexts.createDefault(), hostnameVerifier);
                }
            }
            final PoolingHttpClientConnectionManager poolingmgr = new PoolingHttpClientConnectionManager((Registry<ConnectionSocketFactory>)RegistryBuilder.create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", (PlainConnectionSocketFactory)sslSocketFactory).build());
            if (this.defaultSocketConfig != null) {
                poolingmgr.setDefaultSocketConfig(this.defaultSocketConfig);
            }
            if (this.defaultConnectionConfig != null) {
                poolingmgr.setDefaultConnectionConfig(this.defaultConnectionConfig);
            }
            if (this.systemProperties) {
                String s = System.getProperty("http.keepAlive", "true");
                if ("true".equalsIgnoreCase(s)) {
                    s = System.getProperty("http.maxConnections", "5");
                    final int max = Integer.parseInt(s);
                    poolingmgr.setDefaultMaxPerRoute(max);
                    poolingmgr.setMaxTotal(2 * max);
                }
            }
            if (this.maxConnTotal > 0) {
                poolingmgr.setMaxTotal(this.maxConnTotal);
            }
            if (this.maxConnPerRoute > 0) {
                poolingmgr.setDefaultMaxPerRoute(this.maxConnPerRoute);
            }
            connManager = poolingmgr;
        }
        ConnectionReuseStrategy reuseStrategy = this.reuseStrategy;
        if (reuseStrategy == null) {
            if (this.systemProperties) {
                final String s2 = System.getProperty("http.keepAlive", "true");
                if ("true".equalsIgnoreCase(s2)) {
                    reuseStrategy = DefaultConnectionReuseStrategy.INSTANCE;
                }
                else {
                    reuseStrategy = NoConnectionReuseStrategy.INSTANCE;
                }
            }
            else {
                reuseStrategy = DefaultConnectionReuseStrategy.INSTANCE;
            }
        }
        ConnectionKeepAliveStrategy keepAliveStrategy = this.keepAliveStrategy;
        if (keepAliveStrategy == null) {
            keepAliveStrategy = DefaultConnectionKeepAliveStrategy.INSTANCE;
        }
        AuthenticationStrategy targetAuthStrategy = this.targetAuthStrategy;
        if (targetAuthStrategy == null) {
            targetAuthStrategy = TargetAuthenticationStrategy.INSTANCE;
        }
        AuthenticationStrategy proxyAuthStrategy = this.proxyAuthStrategy;
        if (proxyAuthStrategy == null) {
            proxyAuthStrategy = ProxyAuthenticationStrategy.INSTANCE;
        }
        UserTokenHandler userTokenHandler = this.userTokenHandler;
        if (userTokenHandler == null) {
            if (!this.connectionStateDisabled) {
                userTokenHandler = DefaultUserTokenHandler.INSTANCE;
            }
            else {
                userTokenHandler = NoopUserTokenHandler.INSTANCE;
            }
        }
        ClientExecChain execChain = new MainClientExec(requestExec, connManager, reuseStrategy, keepAliveStrategy, targetAuthStrategy, proxyAuthStrategy, userTokenHandler);
        execChain = this.decorateMainExec(execChain);
        HttpProcessor httpprocessor = this.httpprocessor;
        if (httpprocessor == null) {
            String userAgent = this.userAgent;
            if (userAgent == null) {
                if (this.systemProperties) {
                    userAgent = System.getProperty("http.agent");
                }
                if (userAgent == null) {
                    userAgent = HttpClientBuilder.DEFAULT_USER_AGENT;
                }
            }
            final HttpProcessorBuilder b = HttpProcessorBuilder.create();
            if (this.requestFirst != null) {
                for (final HttpRequestInterceptor i : this.requestFirst) {
                    b.addFirst(i);
                }
            }
            if (this.responseFirst != null) {
                for (final HttpResponseInterceptor j : this.responseFirst) {
                    b.addFirst(j);
                }
            }
            b.addAll(new RequestDefaultHeaders(this.defaultHeaders), new RequestContent(), new RequestTargetHost(), new RequestClientConnControl(), new RequestUserAgent(userAgent), new RequestExpectContinue());
            if (!this.cookieManagementDisabled) {
                b.add(new RequestAddCookies());
            }
            if (!this.contentCompressionDisabled) {
                b.add(new RequestAcceptEncoding());
            }
            if (!this.authCachingDisabled) {
                b.add(new RequestAuthCache());
            }
            if (!this.cookieManagementDisabled) {
                b.add(new ResponseProcessCookies());
            }
            if (!this.contentCompressionDisabled) {
                b.add(new ResponseContentEncoding());
            }
            if (this.requestLast != null) {
                for (final HttpRequestInterceptor i : this.requestLast) {
                    b.addLast(i);
                }
            }
            if (this.responseLast != null) {
                for (final HttpResponseInterceptor j : this.responseLast) {
                    b.addLast(j);
                }
            }
            httpprocessor = b.build();
        }
        execChain = new ProtocolExec(execChain, httpprocessor);
        execChain = this.decorateProtocolExec(execChain);
        if (!this.automaticRetriesDisabled) {
            HttpRequestRetryHandler retryHandler = this.retryHandler;
            if (retryHandler == null) {
                retryHandler = DefaultHttpRequestRetryHandler.INSTANCE;
            }
            execChain = new RetryExec(execChain, retryHandler);
        }
        HttpRoutePlanner routePlanner = this.routePlanner;
        if (routePlanner == null) {
            SchemePortResolver schemePortResolver = this.schemePortResolver;
            if (schemePortResolver == null) {
                schemePortResolver = DefaultSchemePortResolver.INSTANCE;
            }
            if (this.proxy != null) {
                routePlanner = new DefaultProxyRoutePlanner(this.proxy, schemePortResolver);
            }
            else if (this.systemProperties) {
                routePlanner = new SystemDefaultRoutePlanner(schemePortResolver, ProxySelector.getDefault());
            }
            else {
                routePlanner = new DefaultRoutePlanner(schemePortResolver);
            }
        }
        if (!this.redirectHandlingDisabled) {
            RedirectStrategy redirectStrategy = this.redirectStrategy;
            if (redirectStrategy == null) {
                redirectStrategy = DefaultRedirectStrategy.INSTANCE;
            }
            execChain = new RedirectExec(execChain, routePlanner, redirectStrategy);
        }
        final ServiceUnavailableRetryStrategy serviceUnavailStrategy = this.serviceUnavailStrategy;
        if (serviceUnavailStrategy != null) {
            execChain = new ServiceUnavailableRetryExec(execChain, serviceUnavailStrategy);
        }
        final BackoffManager backoffManager = this.backoffManager;
        final ConnectionBackoffStrategy connectionBackoffStrategy = this.connectionBackoffStrategy;
        if (backoffManager != null && connectionBackoffStrategy != null) {
            execChain = new BackoffStrategyExec(execChain, connectionBackoffStrategy, backoffManager);
        }
        Lookup<AuthSchemeProvider> authSchemeRegistry = this.authSchemeRegistry;
        if (authSchemeRegistry == null) {
            authSchemeRegistry = (Lookup<AuthSchemeProvider>)RegistryBuilder.create().register("Basic", new BasicSchemeFactory()).register("Digest", (BasicSchemeFactory)new DigestSchemeFactory()).register("NTLM", (BasicSchemeFactory)new NTLMSchemeFactory()).register("negotiate", (BasicSchemeFactory)new SPNegoSchemeFactory()).register("Kerberos", (BasicSchemeFactory)new KerberosSchemeFactory()).build();
        }
        Lookup<CookieSpecProvider> cookieSpecRegistry = this.cookieSpecRegistry;
        if (cookieSpecRegistry == null) {
            cookieSpecRegistry = (Lookup<CookieSpecProvider>)RegistryBuilder.create().register("best-match", new BestMatchSpecFactory()).register("standard", (BestMatchSpecFactory)new RFC2965SpecFactory()).register("compatibility", (BestMatchSpecFactory)new BrowserCompatSpecFactory()).register("netscape", (BestMatchSpecFactory)new NetscapeDraftSpecFactory()).register("ignoreCookies", (BestMatchSpecFactory)new IgnoreSpecFactory()).register("rfc2109", (BestMatchSpecFactory)new RFC2109SpecFactory()).register("rfc2965", (BestMatchSpecFactory)new RFC2965SpecFactory()).build();
        }
        CookieStore defaultCookieStore = this.cookieStore;
        if (defaultCookieStore == null) {
            defaultCookieStore = new BasicCookieStore();
        }
        CredentialsProvider defaultCredentialsProvider = this.credentialsProvider;
        if (defaultCredentialsProvider == null) {
            if (this.systemProperties) {
                defaultCredentialsProvider = new SystemDefaultCredentialsProvider();
            }
            else {
                defaultCredentialsProvider = new BasicCredentialsProvider();
            }
        }
        return new InternalHttpClient(execChain, connManager, routePlanner, cookieSpecRegistry, authSchemeRegistry, defaultCookieStore, defaultCredentialsProvider, (this.defaultRequestConfig != null) ? this.defaultRequestConfig : RequestConfig.DEFAULT, (this.closeables != null) ? new ArrayList<Closeable>(this.closeables) : null);
    }
    
    static {
        final VersionInfo vi = VersionInfo.loadVersionInfo("org.apache.http.client", HttpClientBuilder.class.getClassLoader());
        final String release = (vi != null) ? vi.getRelease() : "UNAVAILABLE";
        DEFAULT_USER_AGENT = "Apache-HttpClient/" + release + " (java 1.5)";
    }
}
