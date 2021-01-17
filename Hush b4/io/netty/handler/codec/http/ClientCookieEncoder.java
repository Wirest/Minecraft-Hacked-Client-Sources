// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http;

import java.util.Iterator;

public final class ClientCookieEncoder
{
    public static String encode(final String name, final String value) {
        return encode(new DefaultCookie(name, value));
    }
    
    public static String encode(final Cookie cookie) {
        if (cookie == null) {
            throw new NullPointerException("cookie");
        }
        final StringBuilder buf = CookieEncoderUtil.stringBuilder();
        encode(buf, cookie);
        return CookieEncoderUtil.stripTrailingSeparator(buf);
    }
    
    public static String encode(final Cookie... cookies) {
        if (cookies == null) {
            throw new NullPointerException("cookies");
        }
        final StringBuilder buf = CookieEncoderUtil.stringBuilder();
        for (final Cookie c : cookies) {
            if (c == null) {
                break;
            }
            encode(buf, c);
        }
        return CookieEncoderUtil.stripTrailingSeparator(buf);
    }
    
    public static String encode(final Iterable<Cookie> cookies) {
        if (cookies == null) {
            throw new NullPointerException("cookies");
        }
        final StringBuilder buf = CookieEncoderUtil.stringBuilder();
        for (final Cookie c : cookies) {
            if (c == null) {
                break;
            }
            encode(buf, c);
        }
        return CookieEncoderUtil.stripTrailingSeparator(buf);
    }
    
    private static void encode(final StringBuilder buf, final Cookie c) {
        if (c.getVersion() >= 1) {
            CookieEncoderUtil.add(buf, "$Version", 1L);
        }
        CookieEncoderUtil.add(buf, c.getName(), c.getValue());
        if (c.getPath() != null) {
            CookieEncoderUtil.add(buf, "$Path", c.getPath());
        }
        if (c.getDomain() != null) {
            CookieEncoderUtil.add(buf, "$Domain", c.getDomain());
        }
        if (c.getVersion() >= 1 && !c.getPorts().isEmpty()) {
            buf.append('$');
            buf.append("Port");
            buf.append('=');
            buf.append('\"');
            for (final int port : c.getPorts()) {
                buf.append(port);
                buf.append(',');
            }
            buf.setCharAt(buf.length() - 1, '\"');
            buf.append(';');
            buf.append(' ');
        }
    }
    
    private ClientCookieEncoder() {
    }
}
