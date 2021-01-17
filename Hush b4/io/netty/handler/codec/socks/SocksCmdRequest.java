// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.socks;

import io.netty.util.CharsetUtil;
import io.netty.buffer.ByteBuf;
import java.net.IDN;
import io.netty.util.NetUtil;

public final class SocksCmdRequest extends SocksRequest
{
    private final SocksCmdType cmdType;
    private final SocksAddressType addressType;
    private final String host;
    private final int port;
    
    public SocksCmdRequest(final SocksCmdType cmdType, final SocksAddressType addressType, final String host, final int port) {
        super(SocksRequestType.CMD);
        if (cmdType == null) {
            throw new NullPointerException("cmdType");
        }
        if (addressType == null) {
            throw new NullPointerException("addressType");
        }
        if (host == null) {
            throw new NullPointerException("host");
        }
        switch (addressType) {
            case IPv4: {
                if (!NetUtil.isValidIpV4Address(host)) {
                    throw new IllegalArgumentException(host + " is not a valid IPv4 address");
                }
                break;
            }
            case DOMAIN: {
                if (IDN.toASCII(host).length() > 255) {
                    throw new IllegalArgumentException(host + " IDN: " + IDN.toASCII(host) + " exceeds 255 char limit");
                }
                break;
            }
            case IPv6: {
                if (!NetUtil.isValidIpV6Address(host)) {
                    throw new IllegalArgumentException(host + " is not a valid IPv6 address");
                }
                break;
            }
        }
        if (port <= 0 || port >= 65536) {
            throw new IllegalArgumentException(port + " is not in bounds 0 < x < 65536");
        }
        this.cmdType = cmdType;
        this.addressType = addressType;
        this.host = IDN.toASCII(host);
        this.port = port;
    }
    
    public SocksCmdType cmdType() {
        return this.cmdType;
    }
    
    public SocksAddressType addressType() {
        return this.addressType;
    }
    
    public String host() {
        return IDN.toUnicode(this.host);
    }
    
    public int port() {
        return this.port;
    }
    
    @Override
    public void encodeAsByteBuf(final ByteBuf byteBuf) {
        byteBuf.writeByte(this.protocolVersion().byteValue());
        byteBuf.writeByte(this.cmdType.byteValue());
        byteBuf.writeByte(0);
        byteBuf.writeByte(this.addressType.byteValue());
        switch (this.addressType) {
            case IPv4: {
                byteBuf.writeBytes(NetUtil.createByteArrayFromIpAddressString(this.host));
                byteBuf.writeShort(this.port);
                break;
            }
            case DOMAIN: {
                byteBuf.writeByte(this.host.length());
                byteBuf.writeBytes(this.host.getBytes(CharsetUtil.US_ASCII));
                byteBuf.writeShort(this.port);
                break;
            }
            case IPv6: {
                byteBuf.writeBytes(NetUtil.createByteArrayFromIpAddressString(this.host));
                byteBuf.writeShort(this.port);
                break;
            }
        }
    }
}
