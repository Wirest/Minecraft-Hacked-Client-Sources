// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.charset.Charset;

public class CharsetUtils
{
    public static Charset lookup(final String name) {
        if (name == null) {
            return null;
        }
        try {
            return Charset.forName(name);
        }
        catch (UnsupportedCharsetException ex) {
            return null;
        }
    }
    
    public static Charset get(final String name) throws UnsupportedEncodingException {
        if (name == null) {
            return null;
        }
        try {
            return Charset.forName(name);
        }
        catch (UnsupportedCharsetException ex) {
            throw new UnsupportedEncodingException(name);
        }
    }
}
