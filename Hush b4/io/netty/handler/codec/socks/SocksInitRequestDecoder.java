// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.socks;

import io.netty.channel.ChannelHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.ArrayList;
import java.util.List;
import io.netty.handler.codec.ReplayingDecoder;

public class SocksInitRequestDecoder extends ReplayingDecoder<State>
{
    private static final String name = "SOCKS_INIT_REQUEST_DECODER";
    private final List<SocksAuthScheme> authSchemes;
    private SocksProtocolVersion version;
    private byte authSchemeNum;
    private SocksRequest msg;
    
    @Deprecated
    public static String getName() {
        return "SOCKS_INIT_REQUEST_DECODER";
    }
    
    public SocksInitRequestDecoder() {
        super(State.CHECK_PROTOCOL_VERSION);
        this.authSchemes = new ArrayList<SocksAuthScheme>();
        this.msg = SocksCommonUtils.UNKNOWN_SOCKS_REQUEST;
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf byteBuf, final List<Object> out) throws Exception {
        switch (this.state()) {
            case CHECK_PROTOCOL_VERSION: {
                this.version = SocksProtocolVersion.valueOf(byteBuf.readByte());
                if (this.version != SocksProtocolVersion.SOCKS5) {
                    break;
                }
                this.checkpoint(State.READ_AUTH_SCHEMES);
            }
            case READ_AUTH_SCHEMES: {
                this.authSchemes.clear();
                this.authSchemeNum = byteBuf.readByte();
                for (int i = 0; i < this.authSchemeNum; ++i) {
                    this.authSchemes.add(SocksAuthScheme.valueOf(byteBuf.readByte()));
                }
                this.msg = new SocksInitRequest(this.authSchemes);
                break;
            }
        }
        ctx.pipeline().remove(this);
        out.add(this.msg);
    }
    
    enum State
    {
        CHECK_PROTOCOL_VERSION, 
        READ_AUTH_SCHEMES;
    }
}
