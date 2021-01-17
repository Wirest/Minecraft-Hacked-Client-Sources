// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http;

import io.netty.util.internal.StringUtil;

public class DefaultHttpResponse extends DefaultHttpMessage implements HttpResponse
{
    private HttpResponseStatus status;
    
    public DefaultHttpResponse(final HttpVersion version, final HttpResponseStatus status) {
        this(version, status, true);
    }
    
    public DefaultHttpResponse(final HttpVersion version, final HttpResponseStatus status, final boolean validateHeaders) {
        super(version, validateHeaders);
        if (status == null) {
            throw new NullPointerException("status");
        }
        this.status = status;
    }
    
    @Override
    public HttpResponseStatus getStatus() {
        return this.status;
    }
    
    @Override
    public HttpResponse setStatus(final HttpResponseStatus status) {
        if (status == null) {
            throw new NullPointerException("status");
        }
        this.status = status;
        return this;
    }
    
    @Override
    public HttpResponse setProtocolVersion(final HttpVersion version) {
        super.setProtocolVersion(version);
        return this;
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append(StringUtil.simpleClassName(this));
        buf.append("(decodeResult: ");
        buf.append(this.getDecoderResult());
        buf.append(')');
        buf.append(StringUtil.NEWLINE);
        buf.append(this.getProtocolVersion().text());
        buf.append(' ');
        buf.append(this.getStatus());
        buf.append(StringUtil.NEWLINE);
        this.appendHeaders(buf);
        buf.setLength(buf.length() - StringUtil.NEWLINE.length());
        return buf.toString();
    }
}
