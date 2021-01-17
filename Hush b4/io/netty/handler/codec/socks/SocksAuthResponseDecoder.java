// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.socks;

import io.netty.channel.ChannelHandler;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

public class SocksAuthResponseDecoder extends ReplayingDecoder<State>
{
    private static final String name = "SOCKS_AUTH_RESPONSE_DECODER";
    private SocksSubnegotiationVersion version;
    private SocksAuthStatus authStatus;
    private SocksResponse msg;
    
    @Deprecated
    public static String getName() {
        return "SOCKS_AUTH_RESPONSE_DECODER";
    }
    
    public SocksAuthResponseDecoder() {
        super(State.CHECK_PROTOCOL_VERSION);
        this.msg = SocksCommonUtils.UNKNOWN_SOCKS_RESPONSE;
    }
    
    @Override
    protected void decode(final ChannelHandlerContext channelHandlerContext, final ByteBuf byteBuf, final List<Object> out) throws Exception {
        switch (this.state()) {
            case CHECK_PROTOCOL_VERSION: {
                this.version = SocksSubnegotiationVersion.valueOf(byteBuf.readByte());
                if (this.version != SocksSubnegotiationVersion.AUTH_PASSWORD) {
                    break;
                }
                this.checkpoint(State.READ_AUTH_RESPONSE);
            }
            case READ_AUTH_RESPONSE: {
                this.authStatus = SocksAuthStatus.valueOf(byteBuf.readByte());
                this.msg = new SocksAuthResponse(this.authStatus);
                break;
            }
        }
        channelHandlerContext.pipeline().remove(this);
        out.add(this.msg);
    }
    
    enum State
    {
        CHECK_PROTOCOL_VERSION, 
        READ_AUTH_RESPONSE;
    }
}
