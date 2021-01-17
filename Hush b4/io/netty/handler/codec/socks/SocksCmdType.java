// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.socks;

public enum SocksCmdType
{
    CONNECT((byte)1), 
    BIND((byte)2), 
    UDP((byte)3), 
    UNKNOWN((byte)(-1));
    
    private final byte b;
    
    private SocksCmdType(final byte b) {
        this.b = b;
    }
    
    @Deprecated
    public static SocksCmdType fromByte(final byte b) {
        return valueOf(b);
    }
    
    public static SocksCmdType valueOf(final byte b) {
        for (final SocksCmdType code : values()) {
            if (code.b == b) {
                return code;
            }
        }
        return SocksCmdType.UNKNOWN;
    }
    
    public byte byteValue() {
        return this.b;
    }
}
