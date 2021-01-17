// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.utils;

import java.nio.charset.Charset;

public class Charsets
{
    public static final Charset ISO_8859_1;
    public static final Charset US_ASCII;
    public static final Charset UTF_16;
    public static final Charset UTF_16BE;
    public static final Charset UTF_16LE;
    public static final Charset UTF_8;
    
    public static Charset toCharset(final Charset charset) {
        return (charset == null) ? Charset.defaultCharset() : charset;
    }
    
    public static Charset toCharset(final String charset) {
        return (charset == null) ? Charset.defaultCharset() : Charset.forName(charset);
    }
    
    static {
        ISO_8859_1 = Charset.forName("ISO-8859-1");
        US_ASCII = Charset.forName("US-ASCII");
        UTF_16 = Charset.forName("UTF-16");
        UTF_16BE = Charset.forName("UTF-16BE");
        UTF_16LE = Charset.forName("UTF-16LE");
        UTF_8 = Charset.forName("UTF-8");
    }
}
