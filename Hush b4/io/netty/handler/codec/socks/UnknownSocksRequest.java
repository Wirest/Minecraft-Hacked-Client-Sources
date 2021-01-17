// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;

public final class UnknownSocksRequest extends SocksRequest
{
    public UnknownSocksRequest() {
        super(SocksRequestType.UNKNOWN);
    }
    
    @Override
    public void encodeAsByteBuf(final ByteBuf byteBuf) {
    }
}
