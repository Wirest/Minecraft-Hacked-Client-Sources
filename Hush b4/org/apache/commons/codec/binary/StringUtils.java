// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.codec.binary;

import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.Charsets;
import java.nio.charset.Charset;

public class StringUtils
{
    private static byte[] getBytes(final String string, final Charset charset) {
        if (string == null) {
            return null;
        }
        return string.getBytes(charset);
    }
    
    public static byte[] getBytesIso8859_1(final String string) {
        return getBytes(string, Charsets.ISO_8859_1);
    }
    
    public static byte[] getBytesUnchecked(final String string, final String charsetName) {
        if (string == null) {
            return null;
        }
        try {
            return string.getBytes(charsetName);
        }
        catch (UnsupportedEncodingException e) {
            throw newIllegalStateException(charsetName, e);
        }
    }
    
    public static byte[] getBytesUsAscii(final String string) {
        return getBytes(string, Charsets.US_ASCII);
    }
    
    public static byte[] getBytesUtf16(final String string) {
        return getBytes(string, Charsets.UTF_16);
    }
    
    public static byte[] getBytesUtf16Be(final String string) {
        return getBytes(string, Charsets.UTF_16BE);
    }
    
    public static byte[] getBytesUtf16Le(final String string) {
        return getBytes(string, Charsets.UTF_16LE);
    }
    
    public static byte[] getBytesUtf8(final String string) {
        return getBytes(string, Charsets.UTF_8);
    }
    
    private static IllegalStateException newIllegalStateException(final String charsetName, final UnsupportedEncodingException e) {
        return new IllegalStateException(charsetName + ": " + e);
    }
    
    private static String newString(final byte[] bytes, final Charset charset) {
        return (bytes == null) ? null : new String(bytes, charset);
    }
    
    public static String newString(final byte[] bytes, final String charsetName) {
        if (bytes == null) {
            return null;
        }
        try {
            return new String(bytes, charsetName);
        }
        catch (UnsupportedEncodingException e) {
            throw newIllegalStateException(charsetName, e);
        }
    }
    
    public static String newStringIso8859_1(final byte[] bytes) {
        return new String(bytes, Charsets.ISO_8859_1);
    }
    
    public static String newStringUsAscii(final byte[] bytes) {
        return new String(bytes, Charsets.US_ASCII);
    }
    
    public static String newStringUtf16(final byte[] bytes) {
        return new String(bytes, Charsets.UTF_16);
    }
    
    public static String newStringUtf16Be(final byte[] bytes) {
        return new String(bytes, Charsets.UTF_16BE);
    }
    
    public static String newStringUtf16Le(final byte[] bytes) {
        return new String(bytes, Charsets.UTF_16LE);
    }
    
    public static String newStringUtf8(final byte[] bytes) {
        return newString(bytes, Charsets.UTF_8);
    }
}
