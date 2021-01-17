// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.socks;

public enum SocksCmdStatus
{
    SUCCESS((byte)0), 
    FAILURE((byte)1), 
    FORBIDDEN((byte)2), 
    NETWORK_UNREACHABLE((byte)3), 
    HOST_UNREACHABLE((byte)4), 
    REFUSED((byte)5), 
    TTL_EXPIRED((byte)6), 
    COMMAND_NOT_SUPPORTED((byte)7), 
    ADDRESS_NOT_SUPPORTED((byte)8), 
    UNASSIGNED((byte)(-1));
    
    private final byte b;
    
    private SocksCmdStatus(final byte b) {
        this.b = b;
    }
    
    @Deprecated
    public static SocksCmdStatus fromByte(final byte b) {
        return valueOf(b);
    }
    
    public static SocksCmdStatus valueOf(final byte b) {
        for (final SocksCmdStatus code : values()) {
            if (code.b == b) {
                return code;
            }
        }
        return SocksCmdStatus.UNASSIGNED;
    }
    
    public byte byteValue() {
        return this.b;
    }
}
