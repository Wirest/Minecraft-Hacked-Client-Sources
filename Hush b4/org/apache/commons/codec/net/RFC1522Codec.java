// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.StringUtils;
import java.nio.charset.Charset;

abstract class RFC1522Codec
{
    protected static final char SEP = '?';
    protected static final String POSTFIX = "?=";
    protected static final String PREFIX = "=?";
    
    protected String encodeText(final String text, final Charset charset) throws EncoderException {
        if (text == null) {
            return null;
        }
        final StringBuilder buffer = new StringBuilder();
        buffer.append("=?");
        buffer.append(charset);
        buffer.append('?');
        buffer.append(this.getEncoding());
        buffer.append('?');
        final byte[] rawData = this.doEncoding(text.getBytes(charset));
        buffer.append(StringUtils.newStringUsAscii(rawData));
        buffer.append("?=");
        return buffer.toString();
    }
    
    protected String encodeText(final String text, final String charsetName) throws EncoderException, UnsupportedEncodingException {
        if (text == null) {
            return null;
        }
        return this.encodeText(text, Charset.forName(charsetName));
    }
    
    protected String decodeText(final String text) throws DecoderException, UnsupportedEncodingException {
        if (text == null) {
            return null;
        }
        if (!text.startsWith("=?") || !text.endsWith("?=")) {
            throw new DecoderException("RFC 1522 violation: malformed encoded content");
        }
        final int terminator = text.length() - 2;
        int from = 2;
        int to = text.indexOf(63, from);
        if (to == terminator) {
            throw new DecoderException("RFC 1522 violation: charset token not found");
        }
        final String charset = text.substring(from, to);
        if (charset.equals("")) {
            throw new DecoderException("RFC 1522 violation: charset not specified");
        }
        from = to + 1;
        to = text.indexOf(63, from);
        if (to == terminator) {
            throw new DecoderException("RFC 1522 violation: encoding token not found");
        }
        final String encoding = text.substring(from, to);
        if (!this.getEncoding().equalsIgnoreCase(encoding)) {
            throw new DecoderException("This codec cannot decode " + encoding + " encoded content");
        }
        from = to + 1;
        to = text.indexOf(63, from);
        byte[] data = StringUtils.getBytesUsAscii(text.substring(from, to));
        data = this.doDecoding(data);
        return new String(data, charset);
    }
    
    protected abstract String getEncoding();
    
    protected abstract byte[] doEncoding(final byte[] p0) throws EncoderException;
    
    protected abstract byte[] doDecoding(final byte[] p0) throws DecoderException;
}
