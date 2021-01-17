// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.codec.net;

import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.DecoderException;
import java.io.ByteArrayOutputStream;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.charset.IllegalCharsetNameException;
import org.apache.commons.codec.Charsets;
import java.util.BitSet;
import java.nio.charset.Charset;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;

public class QuotedPrintableCodec implements BinaryEncoder, BinaryDecoder, StringEncoder, StringDecoder
{
    private final Charset charset;
    private static final BitSet PRINTABLE_CHARS;
    private static final byte ESCAPE_CHAR = 61;
    private static final byte TAB = 9;
    private static final byte SPACE = 32;
    
    public QuotedPrintableCodec() {
        this(Charsets.UTF_8);
    }
    
    public QuotedPrintableCodec(final Charset charset) {
        this.charset = charset;
    }
    
    public QuotedPrintableCodec(final String charsetName) throws IllegalCharsetNameException, IllegalArgumentException, UnsupportedCharsetException {
        this(Charset.forName(charsetName));
    }
    
    private static final void encodeQuotedPrintable(final int b, final ByteArrayOutputStream buffer) {
        buffer.write(61);
        final char hex1 = Character.toUpperCase(Character.forDigit(b >> 4 & 0xF, 16));
        final char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, 16));
        buffer.write(hex1);
        buffer.write(hex2);
    }
    
    public static final byte[] encodeQuotedPrintable(BitSet printable, final byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        if (printable == null) {
            printable = QuotedPrintableCodec.PRINTABLE_CHARS;
        }
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        for (int b : bytes) {
            final byte c = (byte)b;
            if (b < 0) {
                b += 256;
            }
            if (printable.get(b)) {
                buffer.write(b);
            }
            else {
                encodeQuotedPrintable(b, buffer);
            }
        }
        return buffer.toByteArray();
    }
    
    public static final byte[] decodeQuotedPrintable(final byte[] bytes) throws DecoderException {
        if (bytes == null) {
            return null;
        }
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        for (int i = 0; i < bytes.length; ++i) {
            final int b = bytes[i];
            if (b == 61) {
                try {
                    final int u = Utils.digit16(bytes[++i]);
                    final int l = Utils.digit16(bytes[++i]);
                    buffer.write((char)((u << 4) + l));
                    continue;
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    throw new DecoderException("Invalid quoted-printable encoding", e);
                }
            }
            buffer.write(b);
        }
        return buffer.toByteArray();
    }
    
    @Override
    public byte[] encode(final byte[] bytes) {
        return encodeQuotedPrintable(QuotedPrintableCodec.PRINTABLE_CHARS, bytes);
    }
    
    @Override
    public byte[] decode(final byte[] bytes) throws DecoderException {
        return decodeQuotedPrintable(bytes);
    }
    
    @Override
    public String encode(final String str) throws EncoderException {
        return this.encode(str, this.getCharset());
    }
    
    public String decode(final String str, final Charset charset) throws DecoderException {
        if (str == null) {
            return null;
        }
        return new String(this.decode(StringUtils.getBytesUsAscii(str)), charset);
    }
    
    public String decode(final String str, final String charset) throws DecoderException, UnsupportedEncodingException {
        if (str == null) {
            return null;
        }
        return new String(this.decode(StringUtils.getBytesUsAscii(str)), charset);
    }
    
    @Override
    public String decode(final String str) throws DecoderException {
        return this.decode(str, this.getCharset());
    }
    
    @Override
    public Object encode(final Object obj) throws EncoderException {
        if (obj == null) {
            return null;
        }
        if (obj instanceof byte[]) {
            return this.encode((byte[])obj);
        }
        if (obj instanceof String) {
            return this.encode((String)obj);
        }
        throw new EncoderException("Objects of type " + obj.getClass().getName() + " cannot be quoted-printable encoded");
    }
    
    @Override
    public Object decode(final Object obj) throws DecoderException {
        if (obj == null) {
            return null;
        }
        if (obj instanceof byte[]) {
            return this.decode((byte[])obj);
        }
        if (obj instanceof String) {
            return this.decode((String)obj);
        }
        throw new DecoderException("Objects of type " + obj.getClass().getName() + " cannot be quoted-printable decoded");
    }
    
    public Charset getCharset() {
        return this.charset;
    }
    
    public String getDefaultCharset() {
        return this.charset.name();
    }
    
    public String encode(final String str, final Charset charset) {
        if (str == null) {
            return null;
        }
        return StringUtils.newStringUsAscii(this.encode(str.getBytes(charset)));
    }
    
    public String encode(final String str, final String charset) throws UnsupportedEncodingException {
        if (str == null) {
            return null;
        }
        return StringUtils.newStringUsAscii(this.encode(str.getBytes(charset)));
    }
    
    static {
        PRINTABLE_CHARS = new BitSet(256);
        for (int i = 33; i <= 60; ++i) {
            QuotedPrintableCodec.PRINTABLE_CHARS.set(i);
        }
        for (int i = 62; i <= 126; ++i) {
            QuotedPrintableCodec.PRINTABLE_CHARS.set(i);
        }
        QuotedPrintableCodec.PRINTABLE_CHARS.set(9);
        QuotedPrintableCodec.PRINTABLE_CHARS.set(32);
    }
}
