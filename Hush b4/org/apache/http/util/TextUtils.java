// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.util;

public final class TextUtils
{
    public static boolean isEmpty(final CharSequence s) {
        return s == null || s.length() == 0;
    }
    
    public static boolean isBlank(final CharSequence s) {
        if (s == null) {
            return true;
        }
        for (int i = 0; i < s.length(); ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
