// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.client;

import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.protocol.ResponseContentEncoding;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.protocol.RequestAcceptEncoding;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.params.HttpParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.annotation.ThreadSafe;

@Deprecated
@ThreadSafe
public class ContentEncodingHttpClient extends DefaultHttpClient
{
    public ContentEncodingHttpClient(final ClientConnectionManager conman, final HttpParams params) {
        super(conman, params);
    }
    
    public ContentEncodingHttpClient(final HttpParams params) {
        this(null, params);
    }
    
    public ContentEncodingHttpClient() {
        this((HttpParams)null);
    }
    
    @Override
    protected BasicHttpProcessor createHttpProcessor() {
        final BasicHttpProcessor result = super.createHttpProcessor();
        result.addRequestInterceptor(new RequestAcceptEncoding());
        result.addResponseInterceptor(new ResponseContentEncoding());
        return result;
    }
}
