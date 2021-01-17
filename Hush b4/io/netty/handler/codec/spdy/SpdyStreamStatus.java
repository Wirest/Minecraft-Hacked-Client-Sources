// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.spdy;

public class SpdyStreamStatus implements Comparable<SpdyStreamStatus>
{
    public static final SpdyStreamStatus PROTOCOL_ERROR;
    public static final SpdyStreamStatus INVALID_STREAM;
    public static final SpdyStreamStatus REFUSED_STREAM;
    public static final SpdyStreamStatus UNSUPPORTED_VERSION;
    public static final SpdyStreamStatus CANCEL;
    public static final SpdyStreamStatus INTERNAL_ERROR;
    public static final SpdyStreamStatus FLOW_CONTROL_ERROR;
    public static final SpdyStreamStatus STREAM_IN_USE;
    public static final SpdyStreamStatus STREAM_ALREADY_CLOSED;
    public static final SpdyStreamStatus INVALID_CREDENTIALS;
    public static final SpdyStreamStatus FRAME_TOO_LARGE;
    private final int code;
    private final String statusPhrase;
    
    public static SpdyStreamStatus valueOf(final int code) {
        if (code == 0) {
            throw new IllegalArgumentException("0 is not a valid status code for a RST_STREAM");
        }
        switch (code) {
            case 1: {
                return SpdyStreamStatus.PROTOCOL_ERROR;
            }
            case 2: {
                return SpdyStreamStatus.INVALID_STREAM;
            }
            case 3: {
                return SpdyStreamStatus.REFUSED_STREAM;
            }
            case 4: {
                return SpdyStreamStatus.UNSUPPORTED_VERSION;
            }
            case 5: {
                return SpdyStreamStatus.CANCEL;
            }
            case 6: {
                return SpdyStreamStatus.INTERNAL_ERROR;
            }
            case 7: {
                return SpdyStreamStatus.FLOW_CONTROL_ERROR;
            }
            case 8: {
                return SpdyStreamStatus.STREAM_IN_USE;
            }
            case 9: {
                return SpdyStreamStatus.STREAM_ALREADY_CLOSED;
            }
            case 10: {
                return SpdyStreamStatus.INVALID_CREDENTIALS;
            }
            case 11: {
                return SpdyStreamStatus.FRAME_TOO_LARGE;
            }
            default: {
                return new SpdyStreamStatus(code, "UNKNOWN (" + code + ')');
            }
        }
    }
    
    public SpdyStreamStatus(final int code, final String statusPhrase) {
        if (code == 0) {
            throw new IllegalArgumentException("0 is not a valid status code for a RST_STREAM");
        }
        if (statusPhrase == null) {
            throw new NullPointerException("statusPhrase");
        }
        this.code = code;
        this.statusPhrase = statusPhrase;
    }
    
    public int code() {
        return this.code;
    }
    
    public String statusPhrase() {
        return this.statusPhrase;
    }
    
    @Override
    public int hashCode() {
        return this.code();
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof SpdyStreamStatus && this.code() == ((SpdyStreamStatus)o).code();
    }
    
    @Override
    public String toString() {
        return this.statusPhrase();
    }
    
    @Override
    public int compareTo(final SpdyStreamStatus o) {
        return this.code() - o.code();
    }
    
    static {
        PROTOCOL_ERROR = new SpdyStreamStatus(1, "PROTOCOL_ERROR");
        INVALID_STREAM = new SpdyStreamStatus(2, "INVALID_STREAM");
        REFUSED_STREAM = new SpdyStreamStatus(3, "REFUSED_STREAM");
        UNSUPPORTED_VERSION = new SpdyStreamStatus(4, "UNSUPPORTED_VERSION");
        CANCEL = new SpdyStreamStatus(5, "CANCEL");
        INTERNAL_ERROR = new SpdyStreamStatus(6, "INTERNAL_ERROR");
        FLOW_CONTROL_ERROR = new SpdyStreamStatus(7, "FLOW_CONTROL_ERROR");
        STREAM_IN_USE = new SpdyStreamStatus(8, "STREAM_IN_USE");
        STREAM_ALREADY_CLOSED = new SpdyStreamStatus(9, "STREAM_ALREADY_CLOSED");
        INVALID_CREDENTIALS = new SpdyStreamStatus(10, "INVALID_CREDENTIALS");
        FRAME_TOO_LARGE = new SpdyStreamStatus(11, "FRAME_TOO_LARGE");
    }
}
