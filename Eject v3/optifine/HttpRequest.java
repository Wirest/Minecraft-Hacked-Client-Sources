package optifine;

import java.net.Proxy;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpRequest {
    public static final String METHOD_GET = "GET";
    public static final String METHOD_HEAD = "HEAD";
    public static final String METHOD_POST = "POST";
    public static final String HTTP_1_0 = "HTTP/1.0";
    public static final String HTTP_1_1 = "HTTP/1.1";
    private String host = null;
    private int port = 0;
    private Proxy proxy = Proxy.NO_PROXY;
    private String method = null;
    private String file = null;
    private String http = null;
    private Map<String, String> headers = new LinkedHashMap();
    private byte[] body = null;
    private int redirects = 0;

    public HttpRequest(String paramString1, int paramInt, Proxy paramProxy, String paramString2, String paramString3, String paramString4, Map<String, String> paramMap, byte[] paramArrayOfByte) {
        this.host = paramString1;
        this.port = paramInt;
        this.proxy = paramProxy;
        this.method = paramString2;
        this.file = paramString3;
        this.http = paramString4;
        this.headers = paramMap;
        this.body = paramArrayOfByte;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public String getMethod() {
        return this.method;
    }

    public String getFile() {
        return this.file;
    }

    public String getHttp() {
        return this.http;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public byte[] getBody() {
        return this.body;
    }

    public int getRedirects() {
        return this.redirects;
    }

    public void setRedirects(int paramInt) {
        this.redirects = paramInt;
    }

    public Proxy getProxy() {
        return this.proxy;
    }
}




