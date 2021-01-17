// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.client;

import org.apache.http.client.protocol.RequestProxyAuthentication;
import org.apache.http.client.protocol.RequestTargetAuthentication;
import org.apache.http.client.protocol.RequestAuthCache;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.protocol.ResponseProcessCookies;
import org.apache.http.client.protocol.RequestAddCookies;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.client.protocol.RequestClientConnControl;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestContent;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.protocol.RequestDefaultHeaders;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.ProtocolVersion;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.HttpVersion;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.annotation.ThreadSafe;

@Deprecated
@ThreadSafe
public class DefaultHttpClient extends AbstractHttpClient
{
    public DefaultHttpClient(final ClientConnectionManager conman, final HttpParams params) {
        super(conman, params);
    }
    
    public DefaultHttpClient(final ClientConnectionManager conman) {
        super(conman, null);
    }
    
    public DefaultHttpClient(final HttpParams params) {
        super(null, params);
    }
    
    public DefaultHttpClient() {
        super(null, null);
    }
    
    @Override
    protected HttpParams createHttpParams() {
        final HttpParams params = new SyncBasicHttpParams();
        setDefaultHttpParams(params);
        return params;
    }
    
    public static void setDefaultHttpParams(final HttpParams params) {
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.DEF_CONTENT_CHARSET.name());
        HttpConnectionParams.setTcpNoDelay(params, true);
        HttpConnectionParams.setSocketBufferSize(params, 8192);
        HttpProtocolParams.setUserAgent(params, HttpClientBuilder.DEFAULT_USER_AGENT);
    }
    
    @Override
    protected BasicHttpProcessor createHttpProcessor() {
        final BasicHttpProcessor httpproc = new BasicHttpProcessor();
        httpproc.addInterceptor(new RequestDefaultHeaders());
        httpproc.addInterceptor(new RequestContent());
        httpproc.addInterceptor(new RequestTargetHost());
        httpproc.addInterceptor(new RequestClientConnControl());
        httpproc.addInterceptor(new RequestUserAgent());
        httpproc.addInterceptor(new RequestExpectContinue());
        httpproc.addInterceptor(new RequestAddCookies());
        httpproc.addInterceptor(new ResponseProcessCookies());
        httpproc.addInterceptor(new RequestAuthCache());
        httpproc.addInterceptor(new RequestTargetAuthentication());
        httpproc.addInterceptor(new RequestProxyAuthentication());
        return httpproc;
    }
}
