// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.socks;

public enum SocksAddressType
{
    IPv4((byte)1), 
    DOMAIN((byte)3), 
    IPv6((byte)4), 
    UNKNOWN((byte)(-1));
    
    private final byte b;
    
    private SocksAddressType(final byte b) {
        this.b = b;
    }
    
    @Deprecated
    public static SocksAddressType fromByte(final byte b) {
        return valueOf(b);
    }
    
    public static SocksAddressType valueOf(final byte b) {
        for (final SocksAddressType code : values()) {
            if (code.b == b) {
                return code;
            }
        }
        return SocksAddressType.UNKNOWN;
    }
    
    public byte byteValue() {
        return this.b;
    }
}
