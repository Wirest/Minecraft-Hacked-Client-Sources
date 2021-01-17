// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.util.Iterator;
import java.io.InterruptedIOException;
import java.util.LinkedHashMap;
import java.net.URL;
import java.io.IOException;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

public class HttpPipeline
{
    private static Map mapConnections;
    public static final String HEADER_USER_AGENT = "User-Agent";
    public static final String HEADER_HOST = "Host";
    public static final String HEADER_ACCEPT = "Accept";
    public static final String HEADER_LOCATION = "Location";
    public static final String HEADER_KEEP_ALIVE = "Keep-Alive";
    public static final String HEADER_CONNECTION = "Connection";
    public static final String HEADER_VALUE_KEEP_ALIVE = "keep-alive";
    public static final String HEADER_TRANSFER_ENCODING = "Transfer-Encoding";
    public static final String HEADER_VALUE_CHUNKED = "chunked";
    
    static {
        HttpPipeline.mapConnections = new HashMap();
    }
    
    public static void addRequest(final String p_addRequest_0_, final HttpListener p_addRequest_1_) throws IOException {
        addRequest(p_addRequest_0_, p_addRequest_1_, Proxy.NO_PROXY);
    }
    
    public static void addRequest(final String p_addRequest_0_, final HttpListener p_addRequest_1_, final Proxy p_addRequest_2_) throws IOException {
        final HttpRequest httprequest = makeRequest(p_addRequest_0_, p_addRequest_2_);
        final HttpPipelineRequest httppipelinerequest = new HttpPipelineRequest(httprequest, p_addRequest_1_);
        addRequest(httppipelinerequest);
    }
    
    public static HttpRequest makeRequest(final String p_makeRequest_0_, final Proxy p_makeRequest_1_) throws IOException {
        final URL url = new URL(p_makeRequest_0_);
        if (!url.getProtocol().equals("http")) {
            throw new IOException("Only protocol http is supported: " + url);
        }
        final String s = url.getFile();
        final String s2 = url.getHost();
        int i = url.getPort();
        if (i <= 0) {
            i = 80;
        }
        final String s3 = "GET";
        final String s4 = "HTTP/1.1";
        final Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("User-Agent", "Java/" + System.getProperty("java.version"));
        map.put("Host", s2);
        map.put("Accept", "text/html, image/gif, image/png");
        map.put("Connection", "keep-alive");
        final byte[] abyte = new byte[0];
        final HttpRequest httprequest = new HttpRequest(s2, i, p_makeRequest_1_, s3, s, s4, map, abyte);
        return httprequest;
    }
    
    public static void addRequest(final HttpPipelineRequest p_addRequest_0_) {
        final HttpRequest httprequest = p_addRequest_0_.getHttpRequest();
        for (HttpPipelineConnection httppipelineconnection = getConnection(httprequest.getHost(), httprequest.getPort(), httprequest.getProxy()); !httppipelineconnection.addRequest(p_addRequest_0_); httppipelineconnection = getConnection(httprequest.getHost(), httprequest.getPort(), httprequest.getProxy())) {
            removeConnection(httprequest.getHost(), httprequest.getPort(), httprequest.getProxy(), httppipelineconnection);
        }
    }
    
    private static synchronized HttpPipelineConnection getConnection(final String p_getConnection_0_, final int p_getConnection_1_, final Proxy p_getConnection_2_) {
        final String s = makeConnectionKey(p_getConnection_0_, p_getConnection_1_, p_getConnection_2_);
        HttpPipelineConnection httppipelineconnection = HttpPipeline.mapConnections.get(s);
        if (httppipelineconnection == null) {
            httppipelineconnection = new HttpPipelineConnection(p_getConnection_0_, p_getConnection_1_, p_getConnection_2_);
            HttpPipeline.mapConnections.put(s, httppipelineconnection);
        }
        return httppipelineconnection;
    }
    
    private static synchronized void removeConnection(final String p_removeConnection_0_, final int p_removeConnection_1_, final Proxy p_removeConnection_2_, final HttpPipelineConnection p_removeConnection_3_) {
        final String s = makeConnectionKey(p_removeConnection_0_, p_removeConnection_1_, p_removeConnection_2_);
        final HttpPipelineConnection httppipelineconnection = HttpPipeline.mapConnections.get(s);
        if (httppipelineconnection == p_removeConnection_3_) {
            HttpPipeline.mapConnections.remove(s);
        }
    }
    
    private static String makeConnectionKey(final String p_makeConnectionKey_0_, final int p_makeConnectionKey_1_, final Proxy p_makeConnectionKey_2_) {
        final String s = String.valueOf(p_makeConnectionKey_0_) + ":" + p_makeConnectionKey_1_ + "-" + p_makeConnectionKey_2_;
        return s;
    }
    
    public static byte[] get(final String p_get_0_) throws IOException {
        return get(p_get_0_, Proxy.NO_PROXY);
    }
    
    public static byte[] get(final String p_get_0_, final Proxy p_get_1_) throws IOException {
        final HttpRequest httprequest = makeRequest(p_get_0_, p_get_1_);
        final HttpResponse httpresponse = executeRequest(httprequest);
        if (httpresponse.getStatus() / 100 != 2) {
            throw new IOException("HTTP response: " + httpresponse.getStatus());
        }
        return httpresponse.getBody();
    }
    
    public static HttpResponse executeRequest(final HttpRequest p_executeRequest_0_) throws IOException {
        final Map<String, Object> map = new HashMap<String, Object>();
        final String s = "Response";
        final String s2 = "Exception";
        final HttpListener httplistener = new HttpListener() {
            @Override
            public void finished(final HttpRequest p_finished_1_, final HttpResponse p_finished_2_) {
                synchronized (map) {
                    map.put("Response", p_finished_2_);
                    map.notifyAll();
                }
                // monitorexit(this.val$map)
            }
            
            @Override
            public void failed(final HttpRequest p_failed_1_, final Exception p_failed_2_) {
                synchronized (map) {
                    map.put("Exception", p_failed_2_);
                    map.notifyAll();
                }
                // monitorexit(this.val$map)
            }
        };
        synchronized (map) {
            final HttpPipelineRequest httppipelinerequest = new HttpPipelineRequest(p_executeRequest_0_, httplistener);
            addRequest(httppipelinerequest);
            try {
                map.wait();
            }
            catch (InterruptedException var10) {
                throw new InterruptedIOException("Interrupted");
            }
            final Exception exception = map.get("Exception");
            if (exception != null) {
                if (exception instanceof IOException) {
                    throw (IOException)exception;
                }
                if (exception instanceof RuntimeException) {
                    throw (RuntimeException)exception;
                }
                throw new RuntimeException(exception.getMessage(), exception);
            }
            else {
                final HttpResponse httpresponse = map.get("Response");
                if (httpresponse == null) {
                    throw new IOException("Response is null");
                }
                // monitorexit(map)
                return httpresponse;
            }
        }
    }
    
    public static boolean hasActiveRequests() {
        for (final Object httppipelineconnection : HttpPipeline.mapConnections.values()) {
            if (((HttpPipelineConnection)httppipelineconnection).hasActiveRequests()) {
                return true;
            }
        }
        return false;
    }
}
