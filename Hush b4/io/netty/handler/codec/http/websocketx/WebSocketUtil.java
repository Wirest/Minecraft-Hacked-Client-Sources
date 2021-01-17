// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import io.netty.handler.codec.base64.Base64;
import io.netty.buffer.Unpooled;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

final class WebSocketUtil
{
    static byte[] md5(final byte[] data) {
        try {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(data);
        }
        catch (NoSuchAlgorithmException e) {
            throw new InternalError("MD5 not supported on this platform - Outdated?");
        }
    }
    
    static byte[] sha1(final byte[] data) {
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA1");
            return md.digest(data);
        }
        catch (NoSuchAlgorithmException e) {
            throw new InternalError("SHA-1 is not supported on this platform - Outdated?");
        }
    }
    
    static String base64(final byte[] data) {
        final ByteBuf encodedData = Unpooled.wrappedBuffer(data);
        final ByteBuf encoded = Base64.encode(encodedData);
        final String encodedString = encoded.toString(CharsetUtil.UTF_8);
        encoded.release();
        return encodedString;
    }
    
    static byte[] randomBytes(final int size) {
        final byte[] bytes = new byte[size];
        for (int index = 0; index < size; ++index) {
            bytes[index] = (byte)randomNumber(0, 255);
        }
        return bytes;
    }
    
    static int randomNumber(final int minimum, final int maximum) {
        return (int)(Math.random() * maximum + minimum);
    }
    
    private WebSocketUtil() {
    }
}
