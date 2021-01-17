// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.spdy;

import io.netty.util.internal.StringUtil;

public class DefaultSpdyRstStreamFrame extends DefaultSpdyStreamFrame implements SpdyRstStreamFrame
{
    private SpdyStreamStatus status;
    
    public DefaultSpdyRstStreamFrame(final int streamId, final int statusCode) {
        this(streamId, SpdyStreamStatus.valueOf(statusCode));
    }
    
    public DefaultSpdyRstStreamFrame(final int streamId, final SpdyStreamStatus status) {
        super(streamId);
        this.setStatus(status);
    }
    
    @Override
    public SpdyRstStreamFrame setStreamId(final int streamId) {
        super.setStreamId(streamId);
        return this;
    }
    
    @Override
    public SpdyRstStreamFrame setLast(final boolean last) {
        super.setLast(last);
        return this;
    }
    
    @Override
    public SpdyStreamStatus status() {
        return this.status;
    }
    
    @Override
    public SpdyRstStreamFrame setStatus(final SpdyStreamStatus status) {
        this.status = status;
        return this;
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append(StringUtil.simpleClassName(this));
        buf.append(StringUtil.NEWLINE);
        buf.append("--> Stream-ID = ");
        buf.append(this.streamId());
        buf.append(StringUtil.NEWLINE);
        buf.append("--> Status: ");
        buf.append(this.status());
        return buf.toString();
    }
}
