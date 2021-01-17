// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.locale;

public final class BaseLocale
{
    private static final boolean JDKIMPL = false;
    public static final String SEP = "_";
    private static final Cache CACHE;
    public static final BaseLocale ROOT;
    private String _language;
    private String _script;
    private String _region;
    private String _variant;
    private transient volatile int _hash;
    
    private BaseLocale(final String language, final String script, final String region, final String variant) {
        this._language = "";
        this._script = "";
        this._region = "";
        this._variant = "";
        this._hash = 0;
        if (language != null) {
            this._language = AsciiUtil.toLowerString(language).intern();
        }
        if (script != null) {
            this._script = AsciiUtil.toTitleString(script).intern();
        }
        if (region != null) {
            this._region = AsciiUtil.toUpperString(region).intern();
        }
        if (variant != null) {
            this._variant = AsciiUtil.toUpperString(variant).intern();
        }
    }
    
    public static BaseLocale getInstance(final String language, final String script, final String region, final String variant) {
        final Key key = new Key(language, script, region, variant);
        final BaseLocale baseLocale = BaseLocale.CACHE.get(key);
        return baseLocale;
    }
    
    public String getLanguage() {
        return this._language;
    }
    
    public String getScript() {
        return this._script;
    }
    
    public String getRegion() {
        return this._region;
    }
    
    public String getVariant() {
        return this._variant;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof BaseLocale)) {
            return false;
        }
        final BaseLocale other = (BaseLocale)obj;
        return this.hashCode() == other.hashCode() && this._language.equals(other._language) && this._script.equals(other._script) && this._region.equals(other._region) && this._variant.equals(other._variant);
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        if (this._language.length() > 0) {
            buf.append("language=");
            buf.append(this._language);
        }
        if (this._script.length() > 0) {
            if (buf.length() > 0) {
                buf.append(", ");
            }
            buf.append("script=");
            buf.append(this._script);
        }
        if (this._region.length() > 0) {
            if (buf.length() > 0) {
                buf.append(", ");
            }
            buf.append("region=");
            buf.append(this._region);
        }
        if (this._variant.length() > 0) {
            if (buf.length() > 0) {
                buf.append(", ");
            }
            buf.append("variant=");
            buf.append(this._variant);
        }
        return buf.toString();
    }
    
    @Override
    public int hashCode() {
        int h = this._hash;
        if (h == 0) {
            for (int i = 0; i < this._language.length(); ++i) {
                h = 31 * h + this._language.charAt(i);
            }
            for (int i = 0; i < this._script.length(); ++i) {
                h = 31 * h + this._script.charAt(i);
            }
            for (int i = 0; i < this._region.length(); ++i) {
                h = 31 * h + this._region.charAt(i);
            }
            for (int i = 0; i < this._variant.length(); ++i) {
                h = 31 * h + this._variant.charAt(i);
            }
            this._hash = h;
        }
        return h;
    }
    
    static {
        CACHE = new Cache();
        ROOT = getInstance("", "", "", "");
    }
    
    private static class Key implements Comparable<Key>
    {
        private String _lang;
        private String _scrt;
        private String _regn;
        private String _vart;
        private volatile int _hash;
        
        public Key(final String language, final String script, final String region, final String variant) {
            this._lang = "";
            this._scrt = "";
            this._regn = "";
            this._vart = "";
            if (language != null) {
                this._lang = language;
            }
            if (script != null) {
                this._scrt = script;
            }
            if (region != null) {
                this._regn = region;
            }
            if (variant != null) {
                this._vart = variant;
            }
        }
        
        @Override
        public boolean equals(final Object obj) {
            return this == obj || (obj instanceof Key && AsciiUtil.caseIgnoreMatch(((Key)obj)._lang, this._lang) && AsciiUtil.caseIgnoreMatch(((Key)obj)._scrt, this._scrt) && AsciiUtil.caseIgnoreMatch(((Key)obj)._regn, this._regn) && AsciiUtil.caseIgnoreMatch(((Key)obj)._vart, this._vart));
        }
        
        public int compareTo(final Key other) {
            int res = AsciiUtil.caseIgnoreCompare(this._lang, other._lang);
            if (res == 0) {
                res = AsciiUtil.caseIgnoreCompare(this._scrt, other._scrt);
                if (res == 0) {
                    res = AsciiUtil.caseIgnoreCompare(this._regn, other._regn);
                    if (res == 0) {
                        res = AsciiUtil.caseIgnoreCompare(this._vart, other._vart);
                    }
                }
            }
            return res;
        }
        
        @Override
        public int hashCode() {
            int h = this._hash;
            if (h == 0) {
                for (int i = 0; i < this._lang.length(); ++i) {
                    h = 31 * h + AsciiUtil.toLower(this._lang.charAt(i));
                }
                for (int i = 0; i < this._scrt.length(); ++i) {
                    h = 31 * h + AsciiUtil.toLower(this._scrt.charAt(i));
                }
                for (int i = 0; i < this._regn.length(); ++i) {
                    h = 31 * h + AsciiUtil.toLower(this._regn.charAt(i));
                }
                for (int i = 0; i < this._vart.length(); ++i) {
                    h = 31 * h + AsciiUtil.toLower(this._vart.charAt(i));
                }
                this._hash = h;
            }
            return h;
        }
        
        public static Key normalize(final Key key) {
            final String lang = AsciiUtil.toLowerString(key._lang).intern();
            final String scrt = AsciiUtil.toTitleString(key._scrt).intern();
            final String regn = AsciiUtil.toUpperString(key._regn).intern();
            final String vart = AsciiUtil.toUpperString(key._vart).intern();
            return new Key(lang, scrt, regn, vart);
        }
    }
    
    private static class Cache extends LocaleObjectCache<Key, BaseLocale>
    {
        public Cache() {
        }
        
        @Override
        protected Key normalizeKey(final Key key) {
            return Key.normalize(key);
        }
        
        @Override
        protected BaseLocale createObject(final Key key) {
            return new BaseLocale(key._lang, key._scrt, key._regn, key._vart, null);
        }
    }
}
