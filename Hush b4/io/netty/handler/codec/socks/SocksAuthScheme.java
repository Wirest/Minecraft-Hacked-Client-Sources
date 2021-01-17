// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.socks;

public enum SocksAuthScheme
{
    NO_AUTH((byte)0), 
    AUTH_GSSAPI((byte)1), 
    AUTH_PASSWORD((byte)2), 
    UNKNOWN((byte)(-1));
    
    private final byte b;
    
    private SocksAuthScheme(final byte b) {
        this.b = b;
    }
    
    @Deprecated
    public static SocksAuthScheme fromByte(final byte b) {
        return valueOf(b);
    }
    
    public static SocksAuthScheme valueOf(final byte b) {
        for (final SocksAuthScheme code : values()) {
            if (code.b == b) {
                return code;
            }
        }
        return SocksAuthScheme.UNKNOWN;
    }
    
    public byte byteValue() {
        return this.b;
    }
}
