// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.codec.net;

import org.apache.commons.codec.EncoderException;
import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.DecoderException;
import java.io.ByteArrayOutputStream;
import java.util.BitSet;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;

public class URLCodec implements BinaryEncoder, BinaryDecoder, StringEncoder, StringDecoder
{
    static final int RADIX = 16;
    @Deprecated
    protected String charset;
    protected static final byte ESCAPE_CHAR = 37;
    protected static final BitSet WWW_FORM_URL;
    
    public URLCodec() {
        this("UTF-8");
    }
    
    public URLCodec(final String charset) {
        this.charset = charset;
    }
    
    public static final byte[] encodeUrl(BitSet urlsafe, final byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        if (urlsafe == null) {
            urlsafe = URLCodec.WWW_FORM_URL;
        }
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        for (int b : bytes) {
            final byte c = (byte)b;
            if (b < 0) {
                b += 256;
            }
            if (urlsafe.get(b)) {
                if (b == 32) {
                    b = 43;
                }
                buffer.write(b);
            }
            else {
                buffer.write(37);
                final char hex1 = Character.toUpperCase(Character.forDigit(b >> 4 & 0xF, 16));
                final char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, 16));
                buffer.write(hex1);
                buffer.write(hex2);
            }
        }
        return buffer.toByteArray();
    }
    
    public static final byte[] decodeUrl(final byte[] bytes) throws DecoderException {
        if (bytes == null) {
            return null;
        }
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        for (int i = 0; i < bytes.length; ++i) {
            final int b = bytes[i];
            if (b == 43) {
                buffer.write(32);
            }
            else {
                if (b == 37) {
                    try {
                        final int u = Utils.digit16(bytes[++i]);
                        final int l = Utils.digit16(bytes[++i]);
                        buffer.write((char)((u << 4) + l));
                        continue;
                    }
                    catch (ArrayIndexOutOfBoundsException e) {
                        throw new DecoderException("Invalid URL encoding: ", e);
                    }
                }
                buffer.write(b);
            }
        }
        return buffer.toByteArray();
    }
    
    @Override
    public byte[] encode(final byte[] bytes) {
        return encodeUrl(URLCodec.WWW_FORM_URL, bytes);
    }
    
    @Override
    public byte[] decode(final byte[] bytes) throws DecoderException {
        return decodeUrl(bytes);
    }
    
    public String encode(final String str, final String charset) throws UnsupportedEncodingException {
        if (str == null) {
            return null;
        }
        return StringUtils.newStringUsAscii(this.encode(str.getBytes(charset)));
    }
    
    @Override
    public String encode(final String str) throws EncoderException {
        if (str == null) {
            return null;
        }
        try {
            return this.encode(str, this.getDefaultCharset());
        }
        catch (UnsupportedEncodingException e) {
            throw new EncoderException(e.getMessage(), e);
        }
    }
    
    public String decode(final String str, final String charset) throws DecoderException, UnsupportedEncodingException {
        if (str == null) {
            return null;
        }
        return new String(this.decode(StringUtils.getBytesUsAscii(str)), charset);
    }
    
    @Override
    public String decode(final String str) throws DecoderException {
        if (str == null) {
            return null;
        }
        try {
            return this.decode(str, this.getDefaultCharset());
        }
        catch (UnsupportedEncodingException e) {
            throw new DecoderException(e.getMessage(), e);
        }
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
        throw new EncoderException("Objects of type " + obj.getClass().getName() + " cannot be URL encoded");
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
        throw new DecoderException("Objects of type " + obj.getClass().getName() + " cannot be URL decoded");
    }
    
    public String getDefaultCharset() {
        return this.charset;
    }
    
    @Deprecated
    public String getEncoding() {
        return this.charset;
    }
    
    static {
        WWW_FORM_URL = new BitSet(256);
        for (int i = 97; i <= 122; ++i) {
            URLCodec.WWW_FORM_URL.set(i);
        }
        for (int i = 65; i <= 90; ++i) {
            URLCodec.WWW_FORM_URL.set(i);
        }
        for (int i = 48; i <= 57; ++i) {
            URLCodec.WWW_FORM_URL.set(i);
        }
        URLCodec.WWW_FORM_URL.set(45);
        URLCodec.WWW_FORM_URL.set(95);
        URLCodec.WWW_FORM_URL.set(46);
        URLCodec.WWW_FORM_URL.set(42);
        URLCodec.WWW_FORM_URL.set(32);
    }
}
