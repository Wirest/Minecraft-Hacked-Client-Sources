// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.socks;

import java.util.Iterator;
import io.netty.buffer.ByteBuf;
import java.util.Collections;
import java.util.List;

public final class SocksInitRequest extends SocksRequest
{
    private final List<SocksAuthScheme> authSchemes;
    
    public SocksInitRequest(final List<SocksAuthScheme> authSchemes) {
        super(SocksRequestType.INIT);
        if (authSchemes == null) {
            throw new NullPointerException("authSchemes");
        }
        this.authSchemes = authSchemes;
    }
    
    public List<SocksAuthScheme> authSchemes() {
        return Collections.unmodifiableList((List<? extends SocksAuthScheme>)this.authSchemes);
    }
    
    @Override
    public void encodeAsByteBuf(final ByteBuf byteBuf) {
        byteBuf.writeByte(this.protocolVersion().byteValue());
        byteBuf.writeByte(this.authSchemes.size());
        for (final SocksAuthScheme authScheme : this.authSchemes) {
            byteBuf.writeByte(authScheme.byteValue());
        }
    }
}
