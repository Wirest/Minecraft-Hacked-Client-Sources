// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.locale;

import java.util.Collections;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.TreeMap;
import java.util.Set;
import java.util.Map;
import java.util.SortedMap;

public class LocaleExtensions
{
    private SortedMap<Character, Extension> _map;
    private String _id;
    private static final SortedMap<Character, Extension> EMPTY_MAP;
    public static final LocaleExtensions EMPTY_EXTENSIONS;
    public static final LocaleExtensions CALENDAR_JAPANESE;
    public static final LocaleExtensions NUMBER_THAI;
    
    private LocaleExtensions() {
    }
    
    LocaleExtensions(final Map<InternalLocaleBuilder.CaseInsensitiveChar, String> extensions, final Set<InternalLocaleBuilder.CaseInsensitiveString> uattributes, final Map<InternalLocaleBuilder.CaseInsensitiveString, String> ukeywords) {
        final boolean hasExtension = extensions != null && extensions.size() > 0;
        final boolean hasUAttributes = uattributes != null && uattributes.size() > 0;
        final boolean hasUKeywords = ukeywords != null && ukeywords.size() > 0;
        if (!hasExtension && !hasUAttributes && !hasUKeywords) {
            this._map = LocaleExtensions.EMPTY_MAP;
            this._id = "";
            return;
        }
        this._map = new TreeMap<Character, Extension>();
        if (hasExtension) {
            for (final Map.Entry<InternalLocaleBuilder.CaseInsensitiveChar, String> ext : extensions.entrySet()) {
                final char key = AsciiUtil.toLower(ext.getKey().value());
                String value = ext.getValue();
                if (LanguageTag.isPrivateusePrefixChar(key)) {
                    value = InternalLocaleBuilder.removePrivateuseVariant(value);
                    if (value == null) {
                        continue;
                    }
                }
                final Extension e = new Extension(key, AsciiUtil.toLowerString(value));
                this._map.put(key, e);
            }
        }
        if (hasUAttributes || hasUKeywords) {
            TreeSet<String> uaset = null;
            TreeMap<String, String> ukmap = null;
            if (hasUAttributes) {
                uaset = new TreeSet<String>();
                for (final InternalLocaleBuilder.CaseInsensitiveString cis : uattributes) {
                    uaset.add(AsciiUtil.toLowerString(cis.value()));
                }
            }
            if (hasUKeywords) {
                ukmap = new TreeMap<String, String>();
                for (final Map.Entry<InternalLocaleBuilder.CaseInsensitiveString, String> kwd : ukeywords.entrySet()) {
                    final String key2 = AsciiUtil.toLowerString(kwd.getKey().value());
                    final String type = AsciiUtil.toLowerString(kwd.getValue());
                    ukmap.put(key2, type);
                }
            }
            final UnicodeLocaleExtension ule = new UnicodeLocaleExtension(uaset, ukmap);
            this._map.put('u', ule);
        }
        if (this._map.size() == 0) {
            this._map = LocaleExtensions.EMPTY_MAP;
            this._id = "";
        }
        else {
            this._id = toID(this._map);
        }
    }
    
    public Set<Character> getKeys() {
        return Collections.unmodifiableSet((Set<? extends Character>)this._map.keySet());
    }
    
    public Extension getExtension(final Character key) {
        return this._map.get(AsciiUtil.toLower(key));
    }
    
    public String getExtensionValue(final Character key) {
        final Extension ext = this._map.get(AsciiUtil.toLower(key));
        if (ext == null) {
            return null;
        }
        return ext.getValue();
    }
    
    public Set<String> getUnicodeLocaleAttributes() {
        final Extension ext = this._map.get('u');
        if (ext == null) {
            return Collections.emptySet();
        }
        assert ext instanceof UnicodeLocaleExtension;
        return ((UnicodeLocaleExtension)ext).getUnicodeLocaleAttributes();
    }
    
    public Set<String> getUnicodeLocaleKeys() {
        final Extension ext = this._map.get('u');
        if (ext == null) {
            return Collections.emptySet();
        }
        assert ext instanceof UnicodeLocaleExtension;
        return ((UnicodeLocaleExtension)ext).getUnicodeLocaleKeys();
    }
    
    public String getUnicodeLocaleType(final String unicodeLocaleKey) {
        final Extension ext = this._map.get('u');
        if (ext == null) {
            return null;
        }
        assert ext instanceof UnicodeLocaleExtension;
        return ((UnicodeLocaleExtension)ext).getUnicodeLocaleType(AsciiUtil.toLowerString(unicodeLocaleKey));
    }
    
    public boolean isEmpty() {
        return this._map.isEmpty();
    }
    
    public static boolean isValidKey(final char c) {
        return LanguageTag.isExtensionSingletonChar(c) || LanguageTag.isPrivateusePrefixChar(c);
    }
    
    public static boolean isValidUnicodeLocaleKey(final String ukey) {
        return UnicodeLocaleExtension.isKey(ukey);
    }
    
    private static String toID(final SortedMap<Character, Extension> map) {
        final StringBuilder buf = new StringBuilder();
        Extension privuse = null;
        for (final Map.Entry<Character, Extension> entry : map.entrySet()) {
            final char singleton = entry.getKey();
            final Extension extension = entry.getValue();
            if (LanguageTag.isPrivateusePrefixChar(singleton)) {
                privuse = extension;
            }
            else {
                if (buf.length() > 0) {
                    buf.append("-");
                }
                buf.append(extension);
            }
        }
        if (privuse != null) {
            if (buf.length() > 0) {
                buf.append("-");
            }
            buf.append(privuse);
        }
        return buf.toString();
    }
    
    @Override
    public String toString() {
        return this._id;
    }
    
    public String getID() {
        return this._id;
    }
    
    @Override
    public int hashCode() {
        return this._id.hashCode();
    }
    
    @Override
    public boolean equals(final Object other) {
        return this == other || (other instanceof LocaleExtensions && this._id.equals(((LocaleExtensions)other)._id));
    }
    
    static {
        EMPTY_MAP = Collections.unmodifiableSortedMap((SortedMap<Character, ? extends Extension>)new TreeMap<Character, Extension>());
        EMPTY_EXTENSIONS = new LocaleExtensions();
        LocaleExtensions.EMPTY_EXTENSIONS._id = "";
        LocaleExtensions.EMPTY_EXTENSIONS._map = LocaleExtensions.EMPTY_MAP;
        CALENDAR_JAPANESE = new LocaleExtensions();
        LocaleExtensions.CALENDAR_JAPANESE._id = "u-ca-japanese";
        (LocaleExtensions.CALENDAR_JAPANESE._map = new TreeMap<Character, Extension>()).put('u', UnicodeLocaleExtension.CA_JAPANESE);
        NUMBER_THAI = new LocaleExtensions();
        LocaleExtensions.NUMBER_THAI._id = "u-nu-thai";
        (LocaleExtensions.NUMBER_THAI._map = new TreeMap<Character, Extension>()).put('u', UnicodeLocaleExtension.NU_THAI);
    }
}
