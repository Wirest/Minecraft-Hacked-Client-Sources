// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.spdy;

import java.util.Collections;
import java.util.Set;
import java.util.List;
import java.util.Iterator;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpMethod;
import java.util.Map;

public abstract class SpdyHeaders implements Iterable<Map.Entry<String, String>>
{
    public static final SpdyHeaders EMPTY_HEADERS;
    
    public static String getHeader(final SpdyHeadersFrame frame, final String name) {
        return frame.headers().get(name);
    }
    
    public static String getHeader(final SpdyHeadersFrame frame, final String name, final String defaultValue) {
        final String value = frame.headers().get(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }
    
    public static void setHeader(final SpdyHeadersFrame frame, final String name, final Object value) {
        frame.headers().set(name, value);
    }
    
    public static void setHeader(final SpdyHeadersFrame frame, final String name, final Iterable<?> values) {
        frame.headers().set(name, values);
    }
    
    public static void addHeader(final SpdyHeadersFrame frame, final String name, final Object value) {
        frame.headers().add(name, value);
    }
    
    public static void removeHost(final SpdyHeadersFrame frame) {
        frame.headers().remove(":host");
    }
    
    public static String getHost(final SpdyHeadersFrame frame) {
        return frame.headers().get(":host");
    }
    
    public static void setHost(final SpdyHeadersFrame frame, final String host) {
        frame.headers().set(":host", host);
    }
    
    public static void removeMethod(final int spdyVersion, final SpdyHeadersFrame frame) {
        frame.headers().remove(":method");
    }
    
    public static HttpMethod getMethod(final int spdyVersion, final SpdyHeadersFrame frame) {
        try {
            return HttpMethod.valueOf(frame.headers().get(":method"));
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public static void setMethod(final int spdyVersion, final SpdyHeadersFrame frame, final HttpMethod method) {
        frame.headers().set(":method", method.name());
    }
    
    public static void removeScheme(final int spdyVersion, final SpdyHeadersFrame frame) {
        frame.headers().remove(":scheme");
    }
    
    public static String getScheme(final int spdyVersion, final SpdyHeadersFrame frame) {
        return frame.headers().get(":scheme");
    }
    
    public static void setScheme(final int spdyVersion, final SpdyHeadersFrame frame, final String scheme) {
        frame.headers().set(":scheme", scheme);
    }
    
    public static void removeStatus(final int spdyVersion, final SpdyHeadersFrame frame) {
        frame.headers().remove(":status");
    }
    
    public static HttpResponseStatus getStatus(final int spdyVersion, final SpdyHeadersFrame frame) {
        try {
            final String status = frame.headers().get(":status");
            final int space = status.indexOf(32);
            if (space == -1) {
                return HttpResponseStatus.valueOf(Integer.parseInt(status));
            }
            final int code = Integer.parseInt(status.substring(0, space));
            final String reasonPhrase = status.substring(space + 1);
            final HttpResponseStatus responseStatus = HttpResponseStatus.valueOf(code);
            if (responseStatus.reasonPhrase().equals(reasonPhrase)) {
                return responseStatus;
            }
            return new HttpResponseStatus(code, reasonPhrase);
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public static void setStatus(final int spdyVersion, final SpdyHeadersFrame frame, final HttpResponseStatus status) {
        frame.headers().set(":status", status.toString());
    }
    
    public static void removeUrl(final int spdyVersion, final SpdyHeadersFrame frame) {
        frame.headers().remove(":path");
    }
    
    public static String getUrl(final int spdyVersion, final SpdyHeadersFrame frame) {
        return frame.headers().get(":path");
    }
    
    public static void setUrl(final int spdyVersion, final SpdyHeadersFrame frame, final String path) {
        frame.headers().set(":path", path);
    }
    
    public static void removeVersion(final int spdyVersion, final SpdyHeadersFrame frame) {
        frame.headers().remove(":version");
    }
    
    public static HttpVersion getVersion(final int spdyVersion, final SpdyHeadersFrame frame) {
        try {
            return HttpVersion.valueOf(frame.headers().get(":version"));
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public static void setVersion(final int spdyVersion, final SpdyHeadersFrame frame, final HttpVersion httpVersion) {
        frame.headers().set(":version", httpVersion.text());
    }
    
    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        return this.entries().iterator();
    }
    
    public abstract String get(final String p0);
    
    public abstract List<String> getAll(final String p0);
    
    public abstract List<Map.Entry<String, String>> entries();
    
    public abstract boolean contains(final String p0);
    
    public abstract Set<String> names();
    
    public abstract SpdyHeaders add(final String p0, final Object p1);
    
    public abstract SpdyHeaders add(final String p0, final Iterable<?> p1);
    
    public abstract SpdyHeaders set(final String p0, final Object p1);
    
    public abstract SpdyHeaders set(final String p0, final Iterable<?> p1);
    
    public abstract SpdyHeaders remove(final String p0);
    
    public abstract SpdyHeaders clear();
    
    public abstract boolean isEmpty();
    
    static {
        EMPTY_HEADERS = new SpdyHeaders() {
            @Override
            public List<String> getAll(final String name) {
                return Collections.emptyList();
            }
            
            @Override
            public List<Map.Entry<String, String>> entries() {
                return Collections.emptyList();
            }
            
            @Override
            public boolean contains(final String name) {
                return false;
            }
            
            @Override
            public boolean isEmpty() {
                return true;
            }
            
            @Override
            public Set<String> names() {
                return Collections.emptySet();
            }
            
            @Override
            public SpdyHeaders add(final String name, final Object value) {
                throw new UnsupportedOperationException("read only");
            }
            
            @Override
            public SpdyHeaders add(final String name, final Iterable<?> values) {
                throw new UnsupportedOperationException("read only");
            }
            
            @Override
            public SpdyHeaders set(final String name, final Object value) {
                throw new UnsupportedOperationException("read only");
            }
            
            @Override
            public SpdyHeaders set(final String name, final Iterable<?> values) {
                throw new UnsupportedOperationException("read only");
            }
            
            @Override
            public SpdyHeaders remove(final String name) {
                throw new UnsupportedOperationException("read only");
            }
            
            @Override
            public SpdyHeaders clear() {
                throw new UnsupportedOperationException("read only");
            }
            
            @Override
            public Iterator<Map.Entry<String, String>> iterator() {
                return this.entries().iterator();
            }
            
            @Override
            public String get(final String name) {
                return null;
            }
        };
    }
    
    public static final class HttpNames
    {
        public static final String HOST = ":host";
        public static final String METHOD = ":method";
        public static final String PATH = ":path";
        public static final String SCHEME = ":scheme";
        public static final String STATUS = ":status";
        public static final String VERSION = ":version";
        
        private HttpNames() {
        }
    }
}
