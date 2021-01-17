// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.codec.binary;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.BinaryDecoder;

public class BinaryCodec implements BinaryDecoder, BinaryEncoder
{
    private static final char[] EMPTY_CHAR_ARRAY;
    private static final byte[] EMPTY_BYTE_ARRAY;
    private static final int BIT_0 = 1;
    private static final int BIT_1 = 2;
    private static final int BIT_2 = 4;
    private static final int BIT_3 = 8;
    private static final int BIT_4 = 16;
    private static final int BIT_5 = 32;
    private static final int BIT_6 = 64;
    private static final int BIT_7 = 128;
    private static final int[] BITS;
    
    @Override
    public byte[] encode(final byte[] raw) {
        return toAsciiBytes(raw);
    }
    
    @Override
    public Object encode(final Object raw) throws EncoderException {
        if (!(raw instanceof byte[])) {
            throw new EncoderException("argument not a byte array");
        }
        return toAsciiChars((byte[])raw);
    }
    
    @Override
    public Object decode(final Object ascii) throws DecoderException {
        if (ascii == null) {
            return BinaryCodec.EMPTY_BYTE_ARRAY;
        }
        if (ascii instanceof byte[]) {
            return fromAscii((byte[])ascii);
        }
        if (ascii instanceof char[]) {
            return fromAscii((char[])ascii);
        }
        if (ascii instanceof String) {
            return fromAscii(((String)ascii).toCharArray());
        }
        throw new DecoderException("argument not a byte array");
    }
    
    @Override
    public byte[] decode(final byte[] ascii) {
        return fromAscii(ascii);
    }
    
    public byte[] toByteArray(final String ascii) {
        if (ascii == null) {
            return BinaryCodec.EMPTY_BYTE_ARRAY;
        }
        return fromAscii(ascii.toCharArray());
    }
    
    public static byte[] fromAscii(final char[] ascii) {
        if (ascii == null || ascii.length == 0) {
            return BinaryCodec.EMPTY_BYTE_ARRAY;
        }
        final byte[] l_raw = new byte[ascii.length >> 3];
        for (int ii = 0, jj = ascii.length - 1; ii < l_raw.length; ++ii, jj -= 8) {
            for (int bits = 0; bits < BinaryCodec.BITS.length; ++bits) {
                if (ascii[jj - bits] == '1') {
                    final byte[] array = l_raw;
                    final int n = ii;
                    array[n] |= (byte)BinaryCodec.BITS[bits];
                }
            }
        }
        return l_raw;
    }
    
    public static byte[] fromAscii(final byte[] ascii) {
        if (isEmpty(ascii)) {
            return BinaryCodec.EMPTY_BYTE_ARRAY;
        }
        final byte[] l_raw = new byte[ascii.length >> 3];
        for (int ii = 0, jj = ascii.length - 1; ii < l_raw.length; ++ii, jj -= 8) {
            for (int bits = 0; bits < BinaryCodec.BITS.length; ++bits) {
                if (ascii[jj - bits] == 49) {
                    final byte[] array = l_raw;
                    final int n = ii;
                    array[n] |= (byte)BinaryCodec.BITS[bits];
                }
            }
        }
        return l_raw;
    }
    
    private static boolean isEmpty(final byte[] array) {
        return array == null || array.length == 0;
    }
    
    public static byte[] toAsciiBytes(final byte[] raw) {
        if (isEmpty(raw)) {
            return BinaryCodec.EMPTY_BYTE_ARRAY;
        }
        final byte[] l_ascii = new byte[raw.length << 3];
        for (int ii = 0, jj = l_ascii.length - 1; ii < raw.length; ++ii, jj -= 8) {
            for (int bits = 0; bits < BinaryCodec.BITS.length; ++bits) {
                if ((raw[ii] & BinaryCodec.BITS[bits]) == 0x0) {
                    l_ascii[jj - bits] = 48;
                }
                else {
                    l_ascii[jj - bits] = 49;
                }
            }
        }
        return l_ascii;
    }
    
    public static char[] toAsciiChars(final byte[] raw) {
        if (isEmpty(raw)) {
            return BinaryCodec.EMPTY_CHAR_ARRAY;
        }
        final char[] l_ascii = new char[raw.length << 3];
        for (int ii = 0, jj = l_ascii.length - 1; ii < raw.length; ++ii, jj -= 8) {
            for (int bits = 0; bits < BinaryCodec.BITS.length; ++bits) {
                if ((raw[ii] & BinaryCodec.BITS[bits]) == 0x0) {
                    l_ascii[jj - bits] = '0';
                }
                else {
                    l_ascii[jj - bits] = '1';
                }
            }
        }
        return l_ascii;
    }
    
    public static String toAsciiString(final byte[] raw) {
        return new String(toAsciiChars(raw));
    }
    
    static {
        EMPTY_CHAR_ARRAY = new char[0];
        EMPTY_BYTE_ARRAY = new byte[0];
        BITS = new int[] { 1, 2, 4, 8, 16, 32, 64, 128 };
    }
}
