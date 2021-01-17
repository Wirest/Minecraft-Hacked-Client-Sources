// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;

public final class SocksInitResponse extends SocksResponse
{
    private final SocksAuthScheme authScheme;
    
    public SocksInitResponse(final SocksAuthScheme authScheme) {
        super(SocksResponseType.INIT);
        if (authScheme == null) {
            throw new NullPointerException("authScheme");
        }
        this.authScheme = authScheme;
    }
    
    public SocksAuthScheme authScheme() {
        return this.authScheme;
    }
    
    @Override
    public void encodeAsByteBuf(final ByteBuf byteBuf) {
        byteBuf.writeByte(this.protocolVersion().byteValue());
        byteBuf.writeByte(this.authScheme.byteValue());
    }
}
