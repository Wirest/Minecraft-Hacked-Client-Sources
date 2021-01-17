// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.base64;

import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBuf;

public final class Base64
{
    private static final int MAX_LINE_LENGTH = 76;
    private static final byte EQUALS_SIGN = 61;
    private static final byte NEW_LINE = 10;
    private static final byte WHITE_SPACE_ENC = -5;
    private static final byte EQUALS_SIGN_ENC = -1;
    
    private static byte[] alphabet(final Base64Dialect dialect) {
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        return dialect.alphabet;
    }
    
    private static byte[] decodabet(final Base64Dialect dialect) {
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        return dialect.decodabet;
    }
    
    private static boolean breakLines(final Base64Dialect dialect) {
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        return dialect.breakLinesByDefault;
    }
    
    public static ByteBuf encode(final ByteBuf src) {
        return encode(src, Base64Dialect.STANDARD);
    }
    
    public static ByteBuf encode(final ByteBuf src, final Base64Dialect dialect) {
        return encode(src, breakLines(dialect), dialect);
    }
    
    public static ByteBuf encode(final ByteBuf src, final boolean breakLines) {
        return encode(src, breakLines, Base64Dialect.STANDARD);
    }
    
    public static ByteBuf encode(final ByteBuf src, final boolean breakLines, final Base64Dialect dialect) {
        if (src == null) {
            throw new NullPointerException("src");
        }
        final ByteBuf dest = encode(src, src.readerIndex(), src.readableBytes(), breakLines, dialect);
        src.readerIndex(src.writerIndex());
        return dest;
    }
    
    public static ByteBuf encode(final ByteBuf src, final int off, final int len) {
        return encode(src, off, len, Base64Dialect.STANDARD);
    }
    
    public static ByteBuf encode(final ByteBuf src, final int off, final int len, final Base64Dialect dialect) {
        return encode(src, off, len, breakLines(dialect), dialect);
    }
    
    public static ByteBuf encode(final ByteBuf src, final int off, final int len, final boolean breakLines) {
        return encode(src, off, len, breakLines, Base64Dialect.STANDARD);
    }
    
    public static ByteBuf encode(final ByteBuf src, final int off, final int len, final boolean breakLines, final Base64Dialect dialect) {
        if (src == null) {
            throw new NullPointerException("src");
        }
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        final int len2 = len * 4 / 3;
        final ByteBuf dest = Unpooled.buffer(len2 + ((len % 3 > 0) ? 4 : 0) + (breakLines ? (len2 / 76) : 0)).order(src.order());
        int d = 0;
        int e = 0;
        final int len3 = len - 2;
        int lineLength = 0;
        while (d < len3) {
            encode3to4(src, d + off, 3, dest, e, dialect);
            lineLength += 4;
            if (breakLines && lineLength == 76) {
                dest.setByte(e + 4, 10);
                ++e;
                lineLength = 0;
            }
            d += 3;
            e += 4;
        }
        if (d < len) {
            encode3to4(src, d + off, len - d, dest, e, dialect);
            e += 4;
        }
        return dest.slice(0, e);
    }
    
    private static void encode3to4(final ByteBuf src, final int srcOffset, final int numSigBytes, final ByteBuf dest, final int destOffset, final Base64Dialect dialect) {
        final byte[] ALPHABET = alphabet(dialect);
        final int inBuff = ((numSigBytes > 0) ? (src.getByte(srcOffset) << 24 >>> 8) : 0) | ((numSigBytes > 1) ? (src.getByte(srcOffset + 1) << 24 >>> 16) : 0) | ((numSigBytes > 2) ? (src.getByte(srcOffset + 2) << 24 >>> 24) : 0);
        switch (numSigBytes) {
            case 3: {
                dest.setByte(destOffset, ALPHABET[inBuff >>> 18]);
                dest.setByte(destOffset + 1, ALPHABET[inBuff >>> 12 & 0x3F]);
                dest.setByte(destOffset + 2, ALPHABET[inBuff >>> 6 & 0x3F]);
                dest.setByte(destOffset + 3, ALPHABET[inBuff & 0x3F]);
                break;
            }
            case 2: {
                dest.setByte(destOffset, ALPHABET[inBuff >>> 18]);
                dest.setByte(destOffset + 1, ALPHABET[inBuff >>> 12 & 0x3F]);
                dest.setByte(destOffset + 2, ALPHABET[inBuff >>> 6 & 0x3F]);
                dest.setByte(destOffset + 3, 61);
                break;
            }
            case 1: {
                dest.setByte(destOffset, ALPHABET[inBuff >>> 18]);
                dest.setByte(destOffset + 1, ALPHABET[inBuff >>> 12 & 0x3F]);
                dest.setByte(destOffset + 2, 61);
                dest.setByte(destOffset + 3, 61);
                break;
            }
        }
    }
    
    public static ByteBuf decode(final ByteBuf src) {
        return decode(src, Base64Dialect.STANDARD);
    }
    
    public static ByteBuf decode(final ByteBuf src, final Base64Dialect dialect) {
        if (src == null) {
            throw new NullPointerException("src");
        }
        final ByteBuf dest = decode(src, src.readerIndex(), src.readableBytes(), dialect);
        src.readerIndex(src.writerIndex());
        return dest;
    }
    
    public static ByteBuf decode(final ByteBuf src, final int off, final int len) {
        return decode(src, off, len, Base64Dialect.STANDARD);
    }
    
    public static ByteBuf decode(final ByteBuf src, final int off, final int len, final Base64Dialect dialect) {
        if (src == null) {
            throw new NullPointerException("src");
        }
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        final byte[] DECODABET = decodabet(dialect);
        final int len2 = len * 3 / 4;
        final ByteBuf dest = src.alloc().buffer(len2).order(src.order());
        int outBuffPosn = 0;
        final byte[] b4 = new byte[4];
        int b4Posn = 0;
        for (int i = off; i < off + len; ++i) {
            final byte sbiCrop = (byte)(src.getByte(i) & 0x7F);
            final byte sbiDecode = DECODABET[sbiCrop];
            if (sbiDecode < -5) {
                throw new IllegalArgumentException("bad Base64 input character at " + i + ": " + src.getUnsignedByte(i) + " (decimal)");
            }
            if (sbiDecode >= -1) {
                b4[b4Posn++] = sbiCrop;
                if (b4Posn > 3) {
                    outBuffPosn += decode4to3(b4, 0, dest, outBuffPosn, dialect);
                    b4Posn = 0;
                    if (sbiCrop == 61) {
                        break;
                    }
                }
            }
        }
        return dest.slice(0, outBuffPosn);
    }
    
    private static int decode4to3(final byte[] src, final int srcOffset, final ByteBuf dest, final int destOffset, final Base64Dialect dialect) {
        final byte[] DECODABET = decodabet(dialect);
        if (src[srcOffset + 2] == 61) {
            final int outBuff = (DECODABET[src[srcOffset]] & 0xFF) << 18 | (DECODABET[src[srcOffset + 1]] & 0xFF) << 12;
            dest.setByte(destOffset, (byte)(outBuff >>> 16));
            return 1;
        }
        if (src[srcOffset + 3] == 61) {
            final int outBuff = (DECODABET[src[srcOffset]] & 0xFF) << 18 | (DECODABET[src[srcOffset + 1]] & 0xFF) << 12 | (DECODABET[src[srcOffset + 2]] & 0xFF) << 6;
            dest.setByte(destOffset, (byte)(outBuff >>> 16));
            dest.setByte(destOffset + 1, (byte)(outBuff >>> 8));
            return 2;
        }
        int outBuff;
        try {
            outBuff = ((DECODABET[src[srcOffset]] & 0xFF) << 18 | (DECODABET[src[srcOffset + 1]] & 0xFF) << 12 | (DECODABET[src[srcOffset + 2]] & 0xFF) << 6 | (DECODABET[src[srcOffset + 3]] & 0xFF));
        }
        catch (IndexOutOfBoundsException ignored) {
            throw new IllegalArgumentException("not encoded in Base64");
        }
        dest.setByte(destOffset, (byte)(outBuff >> 16));
        dest.setByte(destOffset + 1, (byte)(outBuff >> 8));
        dest.setByte(destOffset + 2, (byte)outBuff);
        return 3;
    }
    
    private Base64() {
    }
}
