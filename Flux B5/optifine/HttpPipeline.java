package optifine;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Proxy;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpPipeline
{
    private static Map mapConnections = new HashMap();
    public static final String HEADER_USER_AGENT = "User-Agent";
    public static final String HEADER_HOST = "Host";
    public static final String HEADER_ACCEPT = "Accept";
    public static final String HEADER_LOCATION = "Location";
    public static final String HEADER_KEEP_ALIVE = "Keep-Alive";
    public static final String HEADER_CONNECTION = "Connection";
    public static final String HEADER_VALUE_KEEP_ALIVE = "keep-alive";
    public static final String HEADER_TRANSFER_ENCODING = "Transfer-Encoding";
    public static final String HEADER_VALUE_CHUNKED = "chunked";

    public static void addRequest(String urlStr, HttpListener listener) throws IOException
    {
        addRequest(urlStr, listener, Proxy.NO_PROXY);
    }

    public static void addRequest(String urlStr, HttpListener listener, Proxy proxy) throws IOException
    {
        HttpRequest hr = makeRequest(urlStr, proxy);
        HttpPipelineRequest hpr = new HttpPipelineRequest(hr, listener);
        addRequest(hpr);
    }

    public static HttpRequest makeRequest(String urlStr, Proxy proxy) throws IOException
    {
        URL url = new URL(urlStr);

        if (!url.getProtocol().equals("http"))
        {
            throw new IOException("Only protocol http is supported: " + url);
        }
        else
        {
            String file = url.getFile();
            String host = url.getHost();
            int port = url.getPort();

            if (port <= 0)
            {
                port = 80;
            }

            String method = "GET";
            String http = "HTTP/1.1";
            LinkedHashMap headers = new LinkedHashMap();
            headers.put("User-Agent", "Java/" + System.getProperty("java.version"));
            headers.put("Host", host);
            headers.put("Accept", "text/html, image/gif, image/png");
            headers.put("Connection", "keep-alive");
            byte[] body = new byte[0];
            HttpRequest req = new HttpRequest(host, port, proxy, method, file, http, headers, body);
            return req;
        }
    }

    public static void addRequest(HttpPipelineRequest pr)
    {
        HttpRequest hr = pr.getHttpRequest();

        for (HttpPipelineConnection conn = getConnection(hr.getHost(), hr.getPort(), hr.getProxy()); !conn.addRequest(pr); conn = getConnection(hr.getHost(), hr.getPort(), hr.getProxy()))
        {
            removeConnection(hr.getHost(), hr.getPort(), hr.getProxy(), conn);
        }
    }

    private static synchronized HttpPipelineConnection getConnection(String host, int port, Proxy proxy)
    {
        String key = makeConnectionKey(host, port, proxy);
        HttpPipelineConnection conn = (HttpPipelineConnection)mapConnections.get(key);

        if (conn == null)
        {
            conn = new HttpPipelineConnection(host, port, proxy);
            mapConnections.put(key, conn);
        }

        return conn;
    }

    private static synchronized void removeConnection(String host, int port, Proxy proxy, HttpPipelineConnection hpc)
    {
        String key = makeConnectionKey(host, port, proxy);
        HttpPipelineConnection conn = (HttpPipelineConnection)mapConnections.get(key);

        if (conn == hpc)
        {
            mapConnections.remove(key);
        }
    }

    private static String makeConnectionKey(String host, int port, Proxy proxy)
    {
        String hostPort = host + ":" + port + "-" + proxy;
        return hostPort;
    }

    public static byte[] get(String urlStr) throws IOException
    {
        return get(urlStr, Proxy.NO_PROXY);
    }

    public static byte[] get(String urlStr, Proxy proxy) throws IOException
    {
        HttpRequest req = makeRequest(urlStr, proxy);
        HttpResponse resp = executeRequest(req);

        if (resp.getStatus() / 100 != 2)
        {
            throw new IOException("HTTP response: " + resp.getStatus());
        }
        else
        {
            return resp.getBody();
        }
    }

    public static HttpResponse executeRequest(HttpRequest req) throws IOException
    {
        final HashMap map = new HashMap();
        String KEY_RESPONSE = "Response";
        String KEY_EXCEPTION = "Exception";
        HttpListener l = new HttpListener()
        {
            public void finished(HttpRequest req, HttpResponse resp)
            {
                Map var3 = map;

                synchronized (map)
                {
                    map.put("Response", resp);
                    map.notifyAll();
                }
            }
            public void failed(HttpRequest req, Exception e)
            {
                Map var3 = map;

                synchronized (map)
                {
                    map.put("Exception", e);
                    map.notifyAll();
                }
            }
        };

        synchronized (map)
        {
            HttpPipelineRequest hpr = new HttpPipelineRequest(req, l);
            addRequest(hpr);

            try
            {
                map.wait();
            }
            catch (InterruptedException var10)
            {
                throw new InterruptedIOException("Interrupted");
            }

            Exception e = (Exception)map.get("Exception");

            if (e != null)
            {
                if (e instanceof IOException)
                {
                    throw(IOException)e;
                }
                else if (e instanceof RuntimeException)
                {
                    throw(RuntimeException)e;
                }
                else
                {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
            else
            {
                HttpResponse resp = (HttpResponse)map.get("Response");

                if (resp == null)
                {
                    throw new IOException("Response is null");
                }
                else
                {
                    return resp;
                }
            }
        }
    }

    public static boolean hasActiveRequests()
    {
        Collection conns = mapConnections.values();
        Iterator it = conns.iterator();
        HttpPipelineConnection conn;

        do
        {
            if (!it.hasNext())
            {
                return false;
            }

            conn = (HttpPipelineConnection)it.next();
        }
        while (!conn.hasActiveRequests());

        return true;
    }
}
