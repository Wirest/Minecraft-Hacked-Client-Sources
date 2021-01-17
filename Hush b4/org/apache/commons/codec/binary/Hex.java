// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.codec.binary;

import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.DecoderException;
import java.nio.charset.Charset;
import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;

public class Hex implements BinaryEncoder, BinaryDecoder
{
    public static final Charset DEFAULT_CHARSET;
    public static final String DEFAULT_CHARSET_NAME = "UTF-8";
    private static final char[] DIGITS_LOWER;
    private static final char[] DIGITS_UPPER;
    private final Charset charset;
    
    public static byte[] decodeHex(final char[] data) throws DecoderException {
        final int len = data.length;
        if ((len & 0x1) != 0x0) {
            throw new DecoderException("Odd number of characters.");
        }
        final byte[] out = new byte[len >> 1];
        int f;
        for (int i = 0, j = 0; j < len; ++j, f |= toDigit(data[j], j), ++j, out[i] = (byte)(f & 0xFF), ++i) {
            f = toDigit(data[j], j) << 4;
        }
        return out;
    }
    
    public static char[] encodeHex(final byte[] data) {
        return encodeHex(data, true);
    }
    
    public static char[] encodeHex(final byte[] data, final boolean toLowerCase) {
        return encodeHex(data, toLowerCase ? Hex.DIGITS_LOWER : Hex.DIGITS_UPPER);
    }
    
    protected static char[] encodeHex(final byte[] data, final char[] toDigits) {
        final int l = data.length;
        final char[] out = new char[l << 1];
        int i = 0;
        int j = 0;
        while (i < l) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0xF & data[i]];
            ++i;
        }
        return out;
    }
    
    public static String encodeHexString(final byte[] data) {
        return new String(encodeHex(data));
    }
    
    protected static int toDigit(final char ch, final int index) throws DecoderException {
        final int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new DecoderException("Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }
    
    public Hex() {
        this.charset = Hex.DEFAULT_CHARSET;
    }
    
    public Hex(final Charset charset) {
        this.charset = charset;
    }
    
    public Hex(final String charsetName) {
        this(Charset.forName(charsetName));
    }
    
    @Override
    public byte[] decode(final byte[] array) throws DecoderException {
        return decodeHex(new String(array, this.getCharset()).toCharArray());
    }
    
    @Override
    public Object decode(final Object object) throws DecoderException {
        try {
            final char[] charArray = (object instanceof String) ? ((String)object).toCharArray() : object;
            return decodeHex(charArray);
        }
        catch (ClassCastException e) {
            throw new DecoderException(e.getMessage(), e);
        }
    }
    
    @Override
    public byte[] encode(final byte[] array) {
        return encodeHexString(array).getBytes(this.getCharset());
    }
    
    @Override
    public Object encode(final Object object) throws EncoderException {
        try {
            final byte[] byteArray = (object instanceof String) ? ((String)object).getBytes(this.getCharset()) : object;
            return encodeHex(byteArray);
        }
        catch (ClassCastException e) {
            throw new EncoderException(e.getMessage(), e);
        }
    }
    
    public Charset getCharset() {
        return this.charset;
    }
    
    public String getCharsetName() {
        return this.charset.name();
    }
    
    @Override
    public String toString() {
        return super.toString() + "[charsetName=" + this.charset + "]";
    }
    
    static {
        DEFAULT_CHARSET = Charsets.UTF_8;
        DIGITS_LOWER = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        DIGITS_UPPER = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
    }
}
