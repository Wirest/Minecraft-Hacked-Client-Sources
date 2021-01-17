// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.socks;

import io.netty.channel.ChannelHandler;
import io.netty.util.CharsetUtil;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

public class SocksAuthRequestDecoder extends ReplayingDecoder<State>
{
    private static final String name = "SOCKS_AUTH_REQUEST_DECODER";
    private SocksSubnegotiationVersion version;
    private int fieldLength;
    private String username;
    private String password;
    private SocksRequest msg;
    
    @Deprecated
    public static String getName() {
        return "SOCKS_AUTH_REQUEST_DECODER";
    }
    
    public SocksAuthRequestDecoder() {
        super(State.CHECK_PROTOCOL_VERSION);
        this.msg = SocksCommonUtils.UNKNOWN_SOCKS_REQUEST;
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf byteBuf, final List<Object> out) throws Exception {
        switch (this.state()) {
            case CHECK_PROTOCOL_VERSION: {
                this.version = SocksSubnegotiationVersion.valueOf(byteBuf.readByte());
                if (this.version != SocksSubnegotiationVersion.AUTH_PASSWORD) {
                    break;
                }
                this.checkpoint(State.READ_USERNAME);
            }
            case READ_USERNAME: {
                this.fieldLength = byteBuf.readByte();
                this.username = byteBuf.readBytes(this.fieldLength).toString(CharsetUtil.US_ASCII);
                this.checkpoint(State.READ_PASSWORD);
            }
            case READ_PASSWORD: {
                this.fieldLength = byteBuf.readByte();
                this.password = byteBuf.readBytes(this.fieldLength).toString(CharsetUtil.US_ASCII);
                this.msg = new SocksAuthRequest(this.username, this.password);
                break;
            }
        }
        ctx.pipeline().remove(this);
        out.add(this.msg);
    }
    
    enum State
    {
        CHECK_PROTOCOL_VERSION, 
        READ_USERNAME, 
        READ_PASSWORD;
    }
}
