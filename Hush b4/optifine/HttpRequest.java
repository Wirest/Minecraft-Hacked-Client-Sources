// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.util.LinkedHashMap;
import java.util.Map;
import java.net.Proxy;

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
    private int redirects;
    public static final String METHOD_GET = "GET";
    public static final String METHOD_HEAD = "HEAD";
    public static final String METHOD_POST = "POST";
    public static final String HTTP_1_0 = "HTTP/1.0";
    public static final String HTTP_1_1 = "HTTP/1.1";
    
    public HttpRequest(final String p_i60_1_, final int p_i60_2_, final Proxy p_i60_3_, final String p_i60_4_, final String p_i60_5_, final String p_i60_6_, final Map<String, String> p_i60_7_, final byte[] p_i60_8_) {
        this.host = null;
        this.port = 0;
        this.proxy = Proxy.NO_PROXY;
        this.method = null;
        this.file = null;
        this.http = null;
        this.headers = new LinkedHashMap<String, String>();
        this.body = null;
        this.redirects = 0;
        this.host = p_i60_1_;
        this.port = p_i60_2_;
        this.proxy = p_i60_3_;
        this.method = p_i60_4_;
        this.file = p_i60_5_;
        this.http = p_i60_6_;
        this.headers = p_i60_7_;
        this.body = p_i60_8_;
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
    
    public void setRedirects(final int p_setRedirects_1_) {
        this.redirects = p_setRedirects_1_;
    }
    
    public Proxy getProxy() {
        return this.proxy;
    }
}
