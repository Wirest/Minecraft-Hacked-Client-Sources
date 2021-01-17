// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http;

import java.util.HashMap;
import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import java.util.Map;

public class HttpMethod implements Comparable<HttpMethod>
{
    public static final HttpMethod OPTIONS;
    public static final HttpMethod GET;
    public static final HttpMethod HEAD;
    public static final HttpMethod POST;
    public static final HttpMethod PUT;
    public static final HttpMethod PATCH;
    public static final HttpMethod DELETE;
    public static final HttpMethod TRACE;
    public static final HttpMethod CONNECT;
    private static final Map<String, HttpMethod> methodMap;
    private final String name;
    private final byte[] bytes;
    
    public static HttpMethod valueOf(String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        name = name.trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("empty name");
        }
        final HttpMethod result = HttpMethod.methodMap.get(name);
        if (result != null) {
            return result;
        }
        return new HttpMethod(name);
    }
    
    public HttpMethod(final String name) {
        this(name, false);
    }
    
    private HttpMethod(String name, final boolean bytes) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        name = name.trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("empty name");
        }
        for (int i = 0; i < name.length(); ++i) {
            if (Character.isISOControl(name.charAt(i)) || Character.isWhitespace(name.charAt(i))) {
                throw new IllegalArgumentException("invalid character in name");
            }
        }
        this.name = name;
        if (bytes) {
            this.bytes = name.getBytes(CharsetUtil.US_ASCII);
        }
        else {
            this.bytes = null;
        }
    }
    
    public String name() {
        return this.name;
    }
    
    @Override
    public int hashCode() {
        return this.name().hashCode();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof HttpMethod)) {
            return false;
        }
        final HttpMethod that = (HttpMethod)o;
        return this.name().equals(that.name());
    }
    
    @Override
    public String toString() {
        return this.name();
    }
    
    @Override
    public int compareTo(final HttpMethod o) {
        return this.name().compareTo(o.name());
    }
    
    void encode(final ByteBuf buf) {
        if (this.bytes == null) {
            HttpHeaders.encodeAscii0(this.name, buf);
        }
        else {
            buf.writeBytes(this.bytes);
        }
    }
    
    static {
        OPTIONS = new HttpMethod("OPTIONS", true);
        GET = new HttpMethod("GET", true);
        HEAD = new HttpMethod("HEAD", true);
        POST = new HttpMethod("POST", true);
        PUT = new HttpMethod("PUT", true);
        PATCH = new HttpMethod("PATCH", true);
        DELETE = new HttpMethod("DELETE", true);
        TRACE = new HttpMethod("TRACE", true);
        CONNECT = new HttpMethod("CONNECT", true);
        (methodMap = new HashMap<String, HttpMethod>()).put(HttpMethod.OPTIONS.toString(), HttpMethod.OPTIONS);
        HttpMethod.methodMap.put(HttpMethod.GET.toString(), HttpMethod.GET);
        HttpMethod.methodMap.put(HttpMethod.HEAD.toString(), HttpMethod.HEAD);
        HttpMethod.methodMap.put(HttpMethod.POST.toString(), HttpMethod.POST);
        HttpMethod.methodMap.put(HttpMethod.PUT.toString(), HttpMethod.PUT);
        HttpMethod.methodMap.put(HttpMethod.PATCH.toString(), HttpMethod.PATCH);
        HttpMethod.methodMap.put(HttpMethod.DELETE.toString(), HttpMethod.DELETE);
        HttpMethod.methodMap.put(HttpMethod.TRACE.toString(), HttpMethod.TRACE);
        HttpMethod.methodMap.put(HttpMethod.CONNECT.toString(), HttpMethod.CONNECT);
    }
}
