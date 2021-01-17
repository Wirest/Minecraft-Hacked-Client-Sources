// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.locale;

import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Collections;
import java.util.Set;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;

public class UnicodeLocaleExtension extends Extension
{
    public static final char SINGLETON = 'u';
    private static final SortedSet<String> EMPTY_SORTED_SET;
    private static final SortedMap<String, String> EMPTY_SORTED_MAP;
    private SortedSet<String> _attributes;
    private SortedMap<String, String> _keywords;
    public static final UnicodeLocaleExtension CA_JAPANESE;
    public static final UnicodeLocaleExtension NU_THAI;
    
    private UnicodeLocaleExtension() {
        super('u');
        this._attributes = UnicodeLocaleExtension.EMPTY_SORTED_SET;
        this._keywords = UnicodeLocaleExtension.EMPTY_SORTED_MAP;
    }
    
    UnicodeLocaleExtension(final SortedSet<String> attributes, final SortedMap<String, String> keywords) {
        this();
        if (attributes != null && attributes.size() > 0) {
            this._attributes = attributes;
        }
        if (keywords != null && keywords.size() > 0) {
            this._keywords = keywords;
        }
        if (this._attributes.size() > 0 || this._keywords.size() > 0) {
            final StringBuilder sb = new StringBuilder();
            for (final String attribute : this._attributes) {
                sb.append("-").append(attribute);
            }
            for (final Map.Entry<String, String> keyword : this._keywords.entrySet()) {
                final String key = keyword.getKey();
                final String value = keyword.getValue();
                sb.append("-").append(key);
                if (value.length() > 0) {
                    sb.append("-").append(value);
                }
            }
            this._value = sb.substring(1);
        }
    }
    
    public Set<String> getUnicodeLocaleAttributes() {
        return Collections.unmodifiableSet((Set<? extends String>)this._attributes);
    }
    
    public Set<String> getUnicodeLocaleKeys() {
        return Collections.unmodifiableSet((Set<? extends String>)this._keywords.keySet());
    }
    
    public String getUnicodeLocaleType(final String unicodeLocaleKey) {
        return this._keywords.get(unicodeLocaleKey);
    }
    
    public static boolean isSingletonChar(final char c) {
        return 'u' == AsciiUtil.toLower(c);
    }
    
    public static boolean isAttribute(final String s) {
        return s.length() >= 3 && s.length() <= 8 && AsciiUtil.isAlphaNumericString(s);
    }
    
    public static boolean isKey(final String s) {
        return s.length() == 2 && AsciiUtil.isAlphaNumericString(s);
    }
    
    public static boolean isTypeSubtag(final String s) {
        return s.length() >= 3 && s.length() <= 8 && AsciiUtil.isAlphaNumericString(s);
    }
    
    static {
        EMPTY_SORTED_SET = new TreeSet<String>();
        EMPTY_SORTED_MAP = new TreeMap<String, String>();
        CA_JAPANESE = new UnicodeLocaleExtension();
        (UnicodeLocaleExtension.CA_JAPANESE._keywords = new TreeMap<String, String>()).put("ca", "japanese");
        UnicodeLocaleExtension.CA_JAPANESE._value = "ca-japanese";
        NU_THAI = new UnicodeLocaleExtension();
        (UnicodeLocaleExtension.NU_THAI._keywords = new TreeMap<String, String>()).put("nu", "thai");
        UnicodeLocaleExtension.NU_THAI._value = "nu-thai";
    }
}
