// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.socks;

import io.netty.util.internal.StringUtil;

final class SocksCommonUtils
{
    public static final SocksRequest UNKNOWN_SOCKS_REQUEST;
    public static final SocksResponse UNKNOWN_SOCKS_RESPONSE;
    private static final int SECOND_ADDRESS_OCTET_SHIFT = 16;
    private static final int FIRST_ADDRESS_OCTET_SHIFT = 24;
    private static final int THIRD_ADDRESS_OCTET_SHIFT = 8;
    private static final int XOR_DEFAULT_VALUE = 255;
    private static final char[] ipv6conseqZeroFiller;
    private static final char ipv6hextetSeparator = ':';
    
    private SocksCommonUtils() {
    }
    
    public static String intToIp(final int i) {
        return String.valueOf(i >> 24 & 0xFF) + '.' + (i >> 16 & 0xFF) + '.' + (i >> 8 & 0xFF) + '.' + (i & 0xFF);
    }
    
    public static String ipv6toCompressedForm(final byte[] src) {
        assert src.length == 16;
        int cmprHextet = -1;
        int cmprSize = 0;
        int curByte;
        for (int hextet = 0; hextet < 8; hextet = curByte / 2 + 1) {
            int size;
            for (curByte = hextet * 2, size = 0; curByte < src.length && src[curByte] == 0 && src[curByte + 1] == 0; curByte += 2, ++size) {}
            if (size > cmprSize) {
                cmprHextet = hextet;
                cmprSize = size;
            }
        }
        if (cmprHextet == -1 || cmprSize < 2) {
            return ipv6toStr(src);
        }
        final StringBuilder sb = new StringBuilder(39);
        ipv6toStr(sb, src, 0, cmprHextet);
        sb.append(SocksCommonUtils.ipv6conseqZeroFiller);
        ipv6toStr(sb, src, cmprHextet + cmprSize, 8);
        return sb.toString();
    }
    
    public static String ipv6toStr(final byte[] src) {
        assert src.length == 16;
        final StringBuilder sb = new StringBuilder(39);
        ipv6toStr(sb, src, 0, 8);
        return sb.toString();
    }
    
    private static void ipv6toStr(final StringBuilder sb, final byte[] src, final int fromHextet, int toHextet) {
        --toHextet;
        int i;
        for (i = fromHextet; i < toHextet; ++i) {
            appendHextet(sb, src, i);
            sb.append(':');
        }
        appendHextet(sb, src, i);
    }
    
    private static void appendHextet(final StringBuilder sb, final byte[] src, final int i) {
        StringUtil.toHexString(sb, src, i << 1, 2);
    }
    
    static {
        UNKNOWN_SOCKS_REQUEST = new UnknownSocksRequest();
        UNKNOWN_SOCKS_RESPONSE = new UnknownSocksResponse();
        ipv6conseqZeroFiller = new char[] { ':', ':' };
    }
}
