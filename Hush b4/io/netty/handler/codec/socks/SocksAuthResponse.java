// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;

public final class SocksAuthResponse extends SocksResponse
{
    private static final SocksSubnegotiationVersion SUBNEGOTIATION_VERSION;
    private final SocksAuthStatus authStatus;
    
    public SocksAuthResponse(final SocksAuthStatus authStatus) {
        super(SocksResponseType.AUTH);
        if (authStatus == null) {
            throw new NullPointerException("authStatus");
        }
        this.authStatus = authStatus;
    }
    
    public SocksAuthStatus authStatus() {
        return this.authStatus;
    }
    
    @Override
    public void encodeAsByteBuf(final ByteBuf byteBuf) {
        byteBuf.writeByte(SocksAuthResponse.SUBNEGOTIATION_VERSION.byteValue());
        byteBuf.writeByte(this.authStatus.byteValue());
    }
    
    static {
        SUBNEGOTIATION_VERSION = SocksSubnegotiationVersion.AUTH_PASSWORD;
    }
}
