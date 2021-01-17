// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.spdy;

import java.util.Iterator;
import java.util.Map;
import io.netty.util.internal.StringUtil;

public class DefaultSpdyHeadersFrame extends DefaultSpdyStreamFrame implements SpdyHeadersFrame
{
    private boolean invalid;
    private boolean truncated;
    private final SpdyHeaders headers;
    
    public DefaultSpdyHeadersFrame(final int streamId) {
        super(streamId);
        this.headers = new DefaultSpdyHeaders();
    }
    
    @Override
    public SpdyHeadersFrame setStreamId(final int streamId) {
        super.setStreamId(streamId);
        return this;
    }
    
    @Override
    public SpdyHeadersFrame setLast(final boolean last) {
        super.setLast(last);
        return this;
    }
    
    @Override
    public boolean isInvalid() {
        return this.invalid;
    }
    
    @Override
    public SpdyHeadersFrame setInvalid() {
        this.invalid = true;
        return this;
    }
    
    @Override
    public boolean isTruncated() {
        return this.truncated;
    }
    
    @Override
    public SpdyHeadersFrame setTruncated() {
        this.truncated = true;
        return this;
    }
    
    @Override
    public SpdyHeaders headers() {
        return this.headers;
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append(StringUtil.simpleClassName(this));
        buf.append("(last: ");
        buf.append(this.isLast());
        buf.append(')');
        buf.append(StringUtil.NEWLINE);
        buf.append("--> Stream-ID = ");
        buf.append(this.streamId());
        buf.append(StringUtil.NEWLINE);
        buf.append("--> Headers:");
        buf.append(StringUtil.NEWLINE);
        this.appendHeaders(buf);
        buf.setLength(buf.length() - StringUtil.NEWLINE.length());
        return buf.toString();
    }
    
    protected void appendHeaders(final StringBuilder buf) {
        for (final Map.Entry<String, String> e : this.headers()) {
            buf.append("    ");
            buf.append(e.getKey());
            buf.append(": ");
            buf.append(e.getValue());
            buf.append(StringUtil.NEWLINE);
        }
    }
}
