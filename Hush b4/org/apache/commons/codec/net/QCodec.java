// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.codec.net;

import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.Charsets;
import java.util.BitSet;
import java.nio.charset.Charset;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;

public class QCodec extends RFC1522Codec implements StringEncoder, StringDecoder
{
    private final Charset charset;
    private static final BitSet PRINTABLE_CHARS;
    private static final byte BLANK = 32;
    private static final byte UNDERSCORE = 95;
    private boolean encodeBlanks;
    
    public QCodec() {
        this(Charsets.UTF_8);
    }
    
    public QCodec(final Charset charset) {
        this.encodeBlanks = false;
        this.charset = charset;
    }
    
    public QCodec(final String charsetName) {
        this(Charset.forName(charsetName));
    }
    
    @Override
    protected String getEncoding() {
        return "Q";
    }
    
    @Override
    protected byte[] doEncoding(final byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        final byte[] data = QuotedPrintableCodec.encodeQuotedPrintable(QCodec.PRINTABLE_CHARS, bytes);
        if (this.encodeBlanks) {
            for (int i = 0; i < data.length; ++i) {
                if (data[i] == 32) {
                    data[i] = 95;
                }
            }
        }
        return data;
    }
    
    @Override
    protected byte[] doDecoding(final byte[] bytes) throws DecoderException {
        if (bytes == null) {
            return null;
        }
        boolean hasUnderscores = false;
        for (final byte b : bytes) {
            if (b == 95) {
                hasUnderscores = true;
                break;
            }
        }
        if (hasUnderscores) {
            final byte[] tmp = new byte[bytes.length];
            for (int i = 0; i < bytes.length; ++i) {
                final byte b2 = bytes[i];
                if (b2 != 95) {
                    tmp[i] = b2;
                }
                else {
                    tmp[i] = 32;
                }
            }
            return QuotedPrintableCodec.decodeQuotedPrintable(tmp);
        }
        return QuotedPrintableCodec.decodeQuotedPrintable(bytes);
    }
    
    public String encode(final String str, final Charset charset) throws EncoderException {
        if (str == null) {
            return null;
        }
        return this.encodeText(str, charset);
    }
    
    public String encode(final String str, final String charset) throws EncoderException {
        if (str == null) {
            return null;
        }
        try {
            return this.encodeText(str, charset);
        }
        catch (UnsupportedEncodingException e) {
            throw new EncoderException(e.getMessage(), e);
        }
    }
    
    @Override
    public String encode(final String str) throws EncoderException {
        if (str == null) {
            return null;
        }
        return this.encode(str, this.getCharset());
    }
    
    @Override
    public String decode(final String str) throws DecoderException {
        if (str == null) {
            return null;
        }
        try {
            return this.decodeText(str);
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
        if (obj instanceof String) {
            return this.encode((String)obj);
        }
        throw new EncoderException("Objects of type " + obj.getClass().getName() + " cannot be encoded using Q codec");
    }
    
    @Override
    public Object decode(final Object obj) throws DecoderException {
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return this.decode((String)obj);
        }
        throw new DecoderException("Objects of type " + obj.getClass().getName() + " cannot be decoded using Q codec");
    }
    
    public Charset getCharset() {
        return this.charset;
    }
    
    public String getDefaultCharset() {
        return this.charset.name();
    }
    
    public boolean isEncodeBlanks() {
        return this.encodeBlanks;
    }
    
    public void setEncodeBlanks(final boolean b) {
        this.encodeBlanks = b;
    }
    
    static {
        (PRINTABLE_CHARS = new BitSet(256)).set(32);
        QCodec.PRINTABLE_CHARS.set(33);
        QCodec.PRINTABLE_CHARS.set(34);
        QCodec.PRINTABLE_CHARS.set(35);
        QCodec.PRINTABLE_CHARS.set(36);
        QCodec.PRINTABLE_CHARS.set(37);
        QCodec.PRINTABLE_CHARS.set(38);
        QCodec.PRINTABLE_CHARS.set(39);
        QCodec.PRINTABLE_CHARS.set(40);
        QCodec.PRINTABLE_CHARS.set(41);
        QCodec.PRINTABLE_CHARS.set(42);
        QCodec.PRINTABLE_CHARS.set(43);
        QCodec.PRINTABLE_CHARS.set(44);
        QCodec.PRINTABLE_CHARS.set(45);
        QCodec.PRINTABLE_CHARS.set(46);
        QCodec.PRINTABLE_CHARS.set(47);
        for (int i = 48; i <= 57; ++i) {
            QCodec.PRINTABLE_CHARS.set(i);
        }
        QCodec.PRINTABLE_CHARS.set(58);
        QCodec.PRINTABLE_CHARS.set(59);
        QCodec.PRINTABLE_CHARS.set(60);
        QCodec.PRINTABLE_CHARS.set(62);
        QCodec.PRINTABLE_CHARS.set(64);
        for (int i = 65; i <= 90; ++i) {
            QCodec.PRINTABLE_CHARS.set(i);
        }
        QCodec.PRINTABLE_CHARS.set(91);
        QCodec.PRINTABLE_CHARS.set(92);
        QCodec.PRINTABLE_CHARS.set(93);
        QCodec.PRINTABLE_CHARS.set(94);
        QCodec.PRINTABLE_CHARS.set(96);
        for (int i = 97; i <= 122; ++i) {
            QCodec.PRINTABLE_CHARS.set(i);
        }
        QCodec.PRINTABLE_CHARS.set(123);
        QCodec.PRINTABLE_CHARS.set(124);
        QCodec.PRINTABLE_CHARS.set(125);
        QCodec.PRINTABLE_CHARS.set(126);
    }
}
