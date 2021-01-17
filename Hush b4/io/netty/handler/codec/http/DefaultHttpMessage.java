// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http;

import java.util.Iterator;
import java.util.Map;
import io.netty.util.internal.StringUtil;

public abstract class DefaultHttpMessage extends DefaultHttpObject implements HttpMessage
{
    private HttpVersion version;
    private final HttpHeaders headers;
    
    protected DefaultHttpMessage(final HttpVersion version) {
        this(version, true);
    }
    
    protected DefaultHttpMessage(final HttpVersion version, final boolean validate) {
        if (version == null) {
            throw new NullPointerException("version");
        }
        this.version = version;
        this.headers = new DefaultHttpHeaders(validate);
    }
    
    @Override
    public HttpHeaders headers() {
        return this.headers;
    }
    
    @Override
    public HttpVersion getProtocolVersion() {
        return this.version;
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append(StringUtil.simpleClassName(this));
        buf.append("(version: ");
        buf.append(this.getProtocolVersion().text());
        buf.append(", keepAlive: ");
        buf.append(HttpHeaders.isKeepAlive(this));
        buf.append(')');
        buf.append(StringUtil.NEWLINE);
        this.appendHeaders(buf);
        buf.setLength(buf.length() - StringUtil.NEWLINE.length());
        return buf.toString();
    }
    
    @Override
    public HttpMessage setProtocolVersion(final HttpVersion version) {
        if (version == null) {
            throw new NullPointerException("version");
        }
        this.version = version;
        return this;
    }
    
    void appendHeaders(final StringBuilder buf) {
        for (final Map.Entry<String, String> e : this.headers()) {
            buf.append(e.getKey());
            buf.append(": ");
            buf.append(e.getValue());
            buf.append(StringUtil.NEWLINE);
        }
    }
}
