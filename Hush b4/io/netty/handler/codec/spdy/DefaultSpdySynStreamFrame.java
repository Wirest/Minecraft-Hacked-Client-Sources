// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.spdy;

import io.netty.util.internal.StringUtil;

public class DefaultSpdySynStreamFrame extends DefaultSpdyHeadersFrame implements SpdySynStreamFrame
{
    private int associatedStreamId;
    private byte priority;
    private boolean unidirectional;
    
    public DefaultSpdySynStreamFrame(final int streamId, final int associatedStreamId, final byte priority) {
        super(streamId);
        this.setAssociatedStreamId(associatedStreamId);
        this.setPriority(priority);
    }
    
    @Override
    public SpdySynStreamFrame setStreamId(final int streamId) {
        super.setStreamId(streamId);
        return this;
    }
    
    @Override
    public SpdySynStreamFrame setLast(final boolean last) {
        super.setLast(last);
        return this;
    }
    
    @Override
    public SpdySynStreamFrame setInvalid() {
        super.setInvalid();
        return this;
    }
    
    @Override
    public int associatedStreamId() {
        return this.associatedStreamId;
    }
    
    @Override
    public SpdySynStreamFrame setAssociatedStreamId(final int associatedStreamId) {
        if (associatedStreamId < 0) {
            throw new IllegalArgumentException("Associated-To-Stream-ID cannot be negative: " + associatedStreamId);
        }
        this.associatedStreamId = associatedStreamId;
        return this;
    }
    
    @Override
    public byte priority() {
        return this.priority;
    }
    
    @Override
    public SpdySynStreamFrame setPriority(final byte priority) {
        if (priority < 0 || priority > 7) {
            throw new IllegalArgumentException("Priority must be between 0 and 7 inclusive: " + priority);
        }
        this.priority = priority;
        return this;
    }
    
    @Override
    public boolean isUnidirectional() {
        return this.unidirectional;
    }
    
    @Override
    public SpdySynStreamFrame setUnidirectional(final boolean unidirectional) {
        this.unidirectional = unidirectional;
        return this;
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append(StringUtil.simpleClassName(this));
        buf.append("(last: ");
        buf.append(this.isLast());
        buf.append("; unidirectional: ");
        buf.append(this.isUnidirectional());
        buf.append(')');
        buf.append(StringUtil.NEWLINE);
        buf.append("--> Stream-ID = ");
        buf.append(this.streamId());
        buf.append(StringUtil.NEWLINE);
        if (this.associatedStreamId != 0) {
            buf.append("--> Associated-To-Stream-ID = ");
            buf.append(this.associatedStreamId());
            buf.append(StringUtil.NEWLINE);
        }
        buf.append("--> Priority = ");
        buf.append(this.priority());
        buf.append(StringUtil.NEWLINE);
        buf.append("--> Headers:");
        buf.append(StringUtil.NEWLINE);
        this.appendHeaders(buf);
        buf.setLength(buf.length() - StringUtil.NEWLINE.length());
        return buf.toString();
    }
}
