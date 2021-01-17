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

public class SocksCmdResponseDecoder extends ReplayingDecoder<State>
{
    private static final String name = "SOCKS_CMD_RESPONSE_DECODER";
    private SocksProtocolVersion version;
    private int fieldLength;
    private SocksCmdStatus cmdStatus;
    private SocksAddressType addressType;
    private byte reserved;
    private String host;
    private int port;
    private SocksResponse msg;
    
    @Deprecated
    public static String getName() {
        return "SOCKS_CMD_RESPONSE_DECODER";
    }
    
    public SocksCmdResponseDecoder() {
        super(State.CHECK_PROTOCOL_VERSION);
        this.msg = SocksCommonUtils.UNKNOWN_SOCKS_RESPONSE;
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf byteBuf, final List<Object> out) throws Exception {
        Label_0315: {
            switch (this.state()) {
                case CHECK_PROTOCOL_VERSION: {
                    this.version = SocksProtocolVersion.valueOf(byteBuf.readByte());
                    if (this.version != SocksProtocolVersion.SOCKS5) {
                        break;
                    }
                    this.checkpoint(State.READ_CMD_HEADER);
                }
                case READ_CMD_HEADER: {
                    this.cmdStatus = SocksCmdStatus.valueOf(byteBuf.readByte());
                    this.reserved = byteBuf.readByte();
                    this.addressType = SocksAddressType.valueOf(byteBuf.readByte());
                    this.checkpoint(State.READ_CMD_ADDRESS);
                }
                case READ_CMD_ADDRESS: {
                    switch (this.addressType) {
                        case IPv4: {
                            this.host = SocksCommonUtils.intToIp(byteBuf.readInt());
                            this.port = byteBuf.readUnsignedShort();
                            this.msg = new SocksCmdResponse(this.cmdStatus, this.addressType, this.host, this.port);
                            break Label_0315;
                        }
                        case DOMAIN: {
                            this.fieldLength = byteBuf.readByte();
                            this.host = byteBuf.readBytes(this.fieldLength).toString(CharsetUtil.US_ASCII);
                            this.port = byteBuf.readUnsignedShort();
                            this.msg = new SocksCmdResponse(this.cmdStatus, this.addressType, this.host, this.port);
                            break Label_0315;
                        }
                        case IPv6: {
                            this.host = SocksCommonUtils.ipv6toStr(byteBuf.readBytes(16).array());
                            this.port = byteBuf.readUnsignedShort();
                            this.msg = new SocksCmdResponse(this.cmdStatus, this.addressType, this.host, this.port);
                            break Label_0315;
                        }
                    }
                    break;
                }
            }
        }
        ctx.pipeline().remove(this);
        out.add(this.msg);
    }
    
    enum State
    {
        CHECK_PROTOCOL_VERSION, 
        READ_CMD_HEADER, 
        READ_CMD_ADDRESS;
    }
}
