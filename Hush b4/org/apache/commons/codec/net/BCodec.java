// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.Charsets;
import java.nio.charset.Charset;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;

public class BCodec extends RFC1522Codec implements StringEncoder, StringDecoder
{
    private final Charset charset;
    
    public BCodec() {
        this(Charsets.UTF_8);
    }
    
    public BCodec(final Charset charset) {
        this.charset = charset;
    }
    
    public BCodec(final String charsetName) {
        this(Charset.forName(charsetName));
    }
    
    @Override
    protected String getEncoding() {
        return "B";
    }
    
    @Override
    protected byte[] doEncoding(final byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return Base64.encodeBase64(bytes);
    }
    
    @Override
    protected byte[] doDecoding(final byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return Base64.decodeBase64(bytes);
    }
    
    public String encode(final String value, final Charset charset) throws EncoderException {
        if (value == null) {
            return null;
        }
        return this.encodeText(value, charset);
    }
    
    public String encode(final String value, final String charset) throws EncoderException {
        if (value == null) {
            return null;
        }
        try {
            return this.encodeText(value, charset);
        }
        catch (UnsupportedEncodingException e) {
            throw new EncoderException(e.getMessage(), e);
        }
    }
    
    @Override
    public String encode(final String value) throws EncoderException {
        if (value == null) {
            return null;
        }
        return this.encode(value, this.getCharset());
    }
    
    @Override
    public String decode(final String value) throws DecoderException {
        if (value == null) {
            return null;
        }
        try {
            return this.decodeText(value);
        }
        catch (UnsupportedEncodingException e) {
            throw new DecoderException(e.getMessage(), e);
        }
    }
    
    @Override
    public Object encode(final Object value) throws EncoderException {
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return this.encode((String)value);
        }
        throw new EncoderException("Objects of type " + value.getClass().getName() + " cannot be encoded using BCodec");
    }
    
    @Override
    public Object decode(final Object value) throws DecoderException {
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return this.decode((String)value);
        }
        throw new DecoderException("Objects of type " + value.getClass().getName() + " cannot be decoded using BCodec");
    }
    
    public Charset getCharset() {
        return this.charset;
    }
    
    public String getDefaultCharset() {
        return this.charset.name();
    }
}
