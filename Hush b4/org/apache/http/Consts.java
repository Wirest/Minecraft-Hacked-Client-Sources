// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http;

import java.nio.charset.Charset;

public final class Consts
{
    public static final int CR = 13;
    public static final int LF = 10;
    public static final int SP = 32;
    public static final int HT = 9;
    public static final Charset UTF_8;
    public static final Charset ASCII;
    public static final Charset ISO_8859_1;
    
    private Consts() {
    }
    
    static {
        UTF_8 = Charset.forName("UTF-8");
        ASCII = Charset.forName("US-ASCII");
        ISO_8859_1 = Charset.forName("ISO-8859-1");
    }
}
