package net.optifine.http;

import java.net.Proxy;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpRequest
{
    private String host;
    private int port;
    private Proxy proxy;
    private String method;
    private String file;
    private String http;
    private Map<String, String> headers;
    private byte[] body;
    private int redirects = 0;
    public static final String METHOD_GET = "GET";
    public static final String METHOD_HEAD = "HEAD";
    public static final String METHOD_POST = "POST";
    public static final String HTTP_1_0 = "HTTP/1.0";
    public static final String HTTP_1_1 = "HTTP/1.1";

    public HttpRequest(String host, int port, Proxy proxy, String method, String file, String http, Map<String, String> headers, byte[] body)
    {
        this.host = host;
        this.port = port;
        this.proxy = proxy;
        this.method = method;
        this.file = file;
        this.http = http;
        this.headers = headers;
        this.body = body;
    }

    public String getHost()
    {
        return this.host;
    }

    public int getPort()
    {
        return this.port;
    }

    public String getMethod()
    {
        return this.method;
    }

    public String getFile()
    {
        return this.file;
    }

    public String getHttp()
    {
        return this.http;
    }

    public Map<String, String> getHeaders()
    {
        return this.headers;
    }

    public byte[] getBody()
    {
        return this.body;
    }

    public int getRedirects()
    {
        return this.redirects;
    }

    public void setRedirects(int redirects)
    {
        this.redirects = redirects;
    }

    public Proxy getProxy()
    {
        return this.proxy;
    }
}
