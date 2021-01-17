// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.socks;

public enum SocksSubnegotiationVersion
{
    AUTH_PASSWORD((byte)1), 
    UNKNOWN((byte)(-1));
    
    private final byte b;
    
    private SocksSubnegotiationVersion(final byte b) {
        this.b = b;
    }
    
    @Deprecated
    public static SocksSubnegotiationVersion fromByte(final byte b) {
        return valueOf(b);
    }
    
    public static SocksSubnegotiationVersion valueOf(final byte b) {
        for (final SocksSubnegotiationVersion code : values()) {
            if (code.b == b) {
                return code;
            }
        }
        return SocksSubnegotiationVersion.UNKNOWN;
    }
    
    public byte byteValue() {
        return this.b;
    }
}
