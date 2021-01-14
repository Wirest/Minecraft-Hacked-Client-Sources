package optifine;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpPipeline {
    public static final String HEADER_USER_AGENT = "User-Agent";
    public static final String HEADER_HOST = "Host";
    public static final String HEADER_ACCEPT = "Accept";
    public static final String HEADER_LOCATION = "Location";
    public static final String HEADER_KEEP_ALIVE = "Keep-Alive";
    public static final String HEADER_CONNECTION = "Connection";
    public static final String HEADER_VALUE_KEEP_ALIVE = "keep-alive";
    public static final String HEADER_TRANSFER_ENCODING = "Transfer-Encoding";
    public static final String HEADER_VALUE_CHUNKED = "chunked";
    private static Map mapConnections = new HashMap();

    public static void addRequest(String paramString, HttpListener paramHttpListener)
            throws IOException {
        addRequest(paramString, paramHttpListener, Proxy.NO_PROXY);
    }

    public static void addRequest(String paramString, HttpListener paramHttpListener, Proxy paramProxy)
            throws IOException {
        HttpRequest localHttpRequest = makeRequest(paramString, paramProxy);
        HttpPipelineRequest localHttpPipelineRequest = new HttpPipelineRequest(localHttpRequest, paramHttpListener);
        addRequest(localHttpPipelineRequest);
    }

    public static HttpRequest makeRequest(String paramString, Proxy paramProxy)
            throws IOException {
        URL localURL = new URL(paramString);
        if (!localURL.getProtocol().equals("http")) {
            throw new IOException("Only protocol http is supported: " + localURL);
        }
        String str1 = localURL.getFile();
        String str2 = localURL.getHost();
        int i = localURL.getPort();
        if (i <= 0) {
            i = 80;
        }
        String str3 = "GET";
        String str4 = "HTTP/1.1";
        LinkedHashMap localLinkedHashMap = new LinkedHashMap();
        localLinkedHashMap.put("User-Agent", "Java/" + System.getProperty("java.version"));
        localLinkedHashMap.put("Host", str2);
        localLinkedHashMap.put("Accept", "text/html, image/gif, image/png");
        localLinkedHashMap.put("Connection", "keep-alive");
        byte[] arrayOfByte = new byte[0];
        HttpRequest localHttpRequest = new HttpRequest(str2, i, paramProxy, str3, str1, str4, localLinkedHashMap, arrayOfByte);
        return localHttpRequest;
    }

    public static void addRequest(HttpPipelineRequest paramHttpPipelineRequest) {
        HttpRequest localHttpRequest = paramHttpPipelineRequest.getHttpRequest();
        for (HttpPipelineConnection localHttpPipelineConnection = getConnection(localHttpRequest.getHost(), localHttpRequest.getPort(), localHttpRequest.getProxy()); !localHttpPipelineConnection.addRequest(paramHttpPipelineRequest); localHttpPipelineConnection = getConnection(localHttpRequest.getHost(), localHttpRequest.getPort(), localHttpRequest.getProxy())) {
            removeConnection(localHttpRequest.getHost(), localHttpRequest.getPort(), localHttpRequest.getProxy(), localHttpPipelineConnection);
        }
    }

    private static synchronized HttpPipelineConnection getConnection(String paramString, int paramInt, Proxy paramProxy) {
        String str = makeConnectionKey(paramString, paramInt, paramProxy);
        HttpPipelineConnection localHttpPipelineConnection = (HttpPipelineConnection) mapConnections.get(str);
        if (localHttpPipelineConnection == null) {
            localHttpPipelineConnection = new HttpPipelineConnection(paramString, paramInt, paramProxy);
            mapConnections.put(str, localHttpPipelineConnection);
        }
        return localHttpPipelineConnection;
    }

    private static synchronized void removeConnection(String paramString, int paramInt, Proxy paramProxy, HttpPipelineConnection paramHttpPipelineConnection) {
        String str = makeConnectionKey(paramString, paramInt, paramProxy);
        HttpPipelineConnection localHttpPipelineConnection = (HttpPipelineConnection) mapConnections.get(str);
        if (localHttpPipelineConnection == paramHttpPipelineConnection) {
            mapConnections.remove(str);
        }
    }

    private static String makeConnectionKey(String paramString, int paramInt, Proxy paramProxy) {
        String str = paramString + ":" + paramInt + "-" + paramProxy;
        return str;
    }

    public static byte[] get(String paramString)
            throws IOException {
        return get(paramString, Proxy.NO_PROXY);
    }

    public static byte[] get(String paramString, Proxy paramProxy)
            throws IOException {
        HttpRequest localHttpRequest = makeRequest(paramString, paramProxy);
        HttpResponse localHttpResponse = executeRequest(localHttpRequest);
        if (-100 != 2) {
            throw new IOException("HTTP response: " + localHttpResponse.getStatus());
        }
        return localHttpResponse.getBody();
    }

    public static HttpResponse executeRequest(HttpRequest paramHttpRequest)
            throws IOException {
        HashMap localHashMap = new HashMap();
        String str1 = "Response";
        String str2 = "Exception";
        HttpListener local1 = new HttpListener() {
            public void finished(HttpRequest paramAnonymousHttpRequest, HttpResponse paramAnonymousHttpResponse) {
                synchronized (this.val$map) {
                    this.val$map.put("Response", paramAnonymousHttpResponse);
                    this.val$map.notifyAll();
                }
            }

            public void failed(HttpRequest paramAnonymousHttpRequest, Exception paramAnonymousException) {
                synchronized (this.val$map) {
                    this.val$map.put("Exception", paramAnonymousException);
                    this.val$map.notifyAll();
                }
            }
        };
        synchronized (localHashMap) {
            HttpPipelineRequest localHttpPipelineRequest = new HttpPipelineRequest(paramHttpRequest, local1);
            addRequest(localHttpPipelineRequest);
            try {
                localHashMap.wait();
            } catch (InterruptedException localInterruptedException) {
                throw new InterruptedIOException("Interrupted");
            }
            Exception localException = (Exception) localHashMap.get("Exception");
            if (localException != null) {
                if ((localException instanceof IOException)) {
                    throw ((IOException) localException);
                }
                if ((localException instanceof RuntimeException)) {
                    throw ((RuntimeException) localException);
                }
                throw new RuntimeException(localException.getMessage(), localException);
            }
            HttpResponse localHttpResponse = (HttpResponse) localHashMap.get("Response");
            if (localHttpResponse == null) {
                throw new IOException("Response is null");
            }
            return localHttpResponse;
        }
    }

    public static boolean hasActiveRequests() {
        Iterator localIterator = mapConnections.values().iterator();
        while (localIterator.hasNext()) {
            Object localObject = localIterator.next();
            if (((HttpPipelineConnection) localObject).hasActiveRequests()) {
                return true;
            }
        }
        return false;
    }
}




