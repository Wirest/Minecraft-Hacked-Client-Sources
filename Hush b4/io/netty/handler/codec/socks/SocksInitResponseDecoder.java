// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.socks;

import io.netty.channel.ChannelHandler;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

public class SocksInitResponseDecoder extends ReplayingDecoder<State>
{
    private static final String name = "SOCKS_INIT_RESPONSE_DECODER";
    private SocksProtocolVersion version;
    private SocksAuthScheme authScheme;
    private SocksResponse msg;
    
    @Deprecated
    public static String getName() {
        return "SOCKS_INIT_RESPONSE_DECODER";
    }
    
    public SocksInitResponseDecoder() {
        super(State.CHECK_PROTOCOL_VERSION);
        this.msg = SocksCommonUtils.UNKNOWN_SOCKS_RESPONSE;
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf byteBuf, final List<Object> out) throws Exception {
        switch (this.state()) {
            case CHECK_PROTOCOL_VERSION: {
                this.version = SocksProtocolVersion.valueOf(byteBuf.readByte());
                if (this.version != SocksProtocolVersion.SOCKS5) {
                    break;
                }
                this.checkpoint(State.READ_PREFFERED_AUTH_TYPE);
            }
            case READ_PREFFERED_AUTH_TYPE: {
                this.authScheme = SocksAuthScheme.valueOf(byteBuf.readByte());
                this.msg = new SocksInitResponse(this.authScheme);
                break;
            }
        }
        ctx.pipeline().remove(this);
        out.add(this.msg);
    }
    
    enum State
    {
        CHECK_PROTOCOL_VERSION, 
        READ_PREFFERED_AUTH_TYPE;
    }
}
