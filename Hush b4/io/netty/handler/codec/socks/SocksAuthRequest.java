// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.socks;

import io.netty.util.CharsetUtil;
import io.netty.buffer.ByteBuf;
import java.nio.charset.CharsetEncoder;

public final class SocksAuthRequest extends SocksRequest
{
    private static final CharsetEncoder asciiEncoder;
    private static final SocksSubnegotiationVersion SUBNEGOTIATION_VERSION;
    private final String username;
    private final String password;
    
    public SocksAuthRequest(final String username, final String password) {
        super(SocksRequestType.AUTH);
        if (username == null) {
            throw new NullPointerException("username");
        }
        if (password == null) {
            throw new NullPointerException("username");
        }
        if (!SocksAuthRequest.asciiEncoder.canEncode(username) || !SocksAuthRequest.asciiEncoder.canEncode(password)) {
            throw new IllegalArgumentException(" username: " + username + " or password: " + password + " values should be in pure ascii");
        }
        if (username.length() > 255) {
            throw new IllegalArgumentException(username + " exceeds 255 char limit");
        }
        if (password.length() > 255) {
            throw new IllegalArgumentException(password + " exceeds 255 char limit");
        }
        this.username = username;
        this.password = password;
    }
    
    public String username() {
        return this.username;
    }
    
    public String password() {
        return this.password;
    }
    
    @Override
    public void encodeAsByteBuf(final ByteBuf byteBuf) {
        byteBuf.writeByte(SocksAuthRequest.SUBNEGOTIATION_VERSION.byteValue());
        byteBuf.writeByte(this.username.length());
        byteBuf.writeBytes(this.username.getBytes(CharsetUtil.US_ASCII));
        byteBuf.writeByte(this.password.length());
        byteBuf.writeBytes(this.password.getBytes(CharsetUtil.US_ASCII));
    }
    
    static {
        asciiEncoder = CharsetUtil.getEncoder(CharsetUtil.US_ASCII);
        SUBNEGOTIATION_VERSION = SocksSubnegotiationVersion.AUTH_PASSWORD;
    }
}
