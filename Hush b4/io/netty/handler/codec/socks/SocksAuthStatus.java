// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.socks;

public enum SocksAuthStatus
{
    SUCCESS((byte)0), 
    FAILURE((byte)(-1));
    
    private final byte b;
    
    private SocksAuthStatus(final byte b) {
        this.b = b;
    }
    
    @Deprecated
    public static SocksAuthStatus fromByte(final byte b) {
        return valueOf(b);
    }
    
    public static SocksAuthStatus valueOf(final byte b) {
        for (final SocksAuthStatus code : values()) {
            if (code.b == b) {
                return code;
            }
        }
        return SocksAuthStatus.FAILURE;
    }
    
    public byte byteValue() {
        return this.b;
    }
}
