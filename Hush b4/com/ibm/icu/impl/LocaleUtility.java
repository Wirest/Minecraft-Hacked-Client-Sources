// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.util.Locale;

public class LocaleUtility
{
    public static Locale getLocaleFromName(final String name) {
        String language = "";
        String country = "";
        String variant = "";
        int i1 = name.indexOf(95);
        if (i1 < 0) {
            language = name;
        }
        else {
            language = name.substring(0, i1);
            ++i1;
            final int i2 = name.indexOf(95, i1);
            if (i2 < 0) {
                country = name.substring(i1);
            }
            else {
                country = name.substring(i1, i2);
                variant = name.substring(i2 + 1);
            }
        }
        return new Locale(language, country, variant);
    }
    
    public static boolean isFallbackOf(final String parent, final String child) {
        if (!child.startsWith(parent)) {
            return false;
        }
        final int i = parent.length();
        return i == child.length() || child.charAt(i) == '_';
    }
    
    public static boolean isFallbackOf(final Locale parent, final Locale child) {
        return isFallbackOf(parent.toString(), child.toString());
    }
    
    public static Locale fallback(final Locale loc) {
        final String[] parts = { loc.getLanguage(), loc.getCountry(), loc.getVariant() };
        int i;
        for (i = 2; i >= 0; --i) {
            if (parts[i].length() != 0) {
                parts[i] = "";
                break;
            }
        }
        if (i < 0) {
            return null;
        }
        return new Locale(parts[0], parts[1], parts[2]);
    }
}
