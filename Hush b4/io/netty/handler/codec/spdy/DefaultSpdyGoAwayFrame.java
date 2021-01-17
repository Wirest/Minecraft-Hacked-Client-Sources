// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.spdy;

import io.netty.util.internal.StringUtil;

public class DefaultSpdyGoAwayFrame implements SpdyGoAwayFrame
{
    private int lastGoodStreamId;
    private SpdySessionStatus status;
    
    public DefaultSpdyGoAwayFrame(final int lastGoodStreamId) {
        this(lastGoodStreamId, 0);
    }
    
    public DefaultSpdyGoAwayFrame(final int lastGoodStreamId, final int statusCode) {
        this(lastGoodStreamId, SpdySessionStatus.valueOf(statusCode));
    }
    
    public DefaultSpdyGoAwayFrame(final int lastGoodStreamId, final SpdySessionStatus status) {
        this.setLastGoodStreamId(lastGoodStreamId);
        this.setStatus(status);
    }
    
    @Override
    public int lastGoodStreamId() {
        return this.lastGoodStreamId;
    }
    
    @Override
    public SpdyGoAwayFrame setLastGoodStreamId(final int lastGoodStreamId) {
        if (lastGoodStreamId < 0) {
            throw new IllegalArgumentException("Last-good-stream-ID cannot be negative: " + lastGoodStreamId);
        }
        this.lastGoodStreamId = lastGoodStreamId;
        return this;
    }
    
    @Override
    public SpdySessionStatus status() {
        return this.status;
    }
    
    @Override
    public SpdyGoAwayFrame setStatus(final SpdySessionStatus status) {
        this.status = status;
        return this;
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append(StringUtil.simpleClassName(this));
        buf.append(StringUtil.NEWLINE);
        buf.append("--> Last-good-stream-ID = ");
        buf.append(this.lastGoodStreamId());
        buf.append(StringUtil.NEWLINE);
        buf.append("--> Status: ");
        buf.append(this.status());
        return buf.toString();
    }
}
