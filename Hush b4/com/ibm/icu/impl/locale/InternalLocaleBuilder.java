// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.locale;

import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;

public final class InternalLocaleBuilder
{
    private static final boolean JDKIMPL = false;
    private String _language;
    private String _script;
    private String _region;
    private String _variant;
    private static final CaseInsensitiveChar PRIVUSE_KEY;
    private HashMap<CaseInsensitiveChar, String> _extensions;
    private HashSet<CaseInsensitiveString> _uattributes;
    private HashMap<CaseInsensitiveString, String> _ukeywords;
    
    public InternalLocaleBuilder() {
        this._language = "";
        this._script = "";
        this._region = "";
        this._variant = "";
    }
    
    public InternalLocaleBuilder setLanguage(final String language) throws LocaleSyntaxException {
        if (language == null || language.length() == 0) {
            this._language = "";
        }
        else {
            if (!LanguageTag.isLanguage(language)) {
                throw new LocaleSyntaxException("Ill-formed language: " + language, 0);
            }
            this._language = language;
        }
        return this;
    }
    
    public InternalLocaleBuilder setScript(final String script) throws LocaleSyntaxException {
        if (script == null || script.length() == 0) {
            this._script = "";
        }
        else {
            if (!LanguageTag.isScript(script)) {
                throw new LocaleSyntaxException("Ill-formed script: " + script, 0);
            }
            this._script = script;
        }
        return this;
    }
    
    public InternalLocaleBuilder setRegion(final String region) throws LocaleSyntaxException {
        if (region == null || region.length() == 0) {
            this._region = "";
        }
        else {
            if (!LanguageTag.isRegion(region)) {
                throw new LocaleSyntaxException("Ill-formed region: " + region, 0);
            }
            this._region = region;
        }
        return this;
    }
    
    public InternalLocaleBuilder setVariant(final String variant) throws LocaleSyntaxException {
        if (variant == null || variant.length() == 0) {
            this._variant = "";
        }
        else {
            final String var = variant.replaceAll("-", "_");
            final int errIdx = this.checkVariants(var, "_");
            if (errIdx != -1) {
                throw new LocaleSyntaxException("Ill-formed variant: " + variant, errIdx);
            }
            this._variant = var;
        }
        return this;
    }
    
    public InternalLocaleBuilder addUnicodeLocaleAttribute(final String attribute) throws LocaleSyntaxException {
        if (attribute == null || !UnicodeLocaleExtension.isAttribute(attribute)) {
            throw new LocaleSyntaxException("Ill-formed Unicode locale attribute: " + attribute);
        }
        if (this._uattributes == null) {
            this._uattributes = new HashSet<CaseInsensitiveString>(4);
        }
        this._uattributes.add(new CaseInsensitiveString(attribute));
        return this;
    }
    
    public InternalLocaleBuilder removeUnicodeLocaleAttribute(final String attribute) throws LocaleSyntaxException {
        if (attribute == null || !UnicodeLocaleExtension.isAttribute(attribute)) {
            throw new LocaleSyntaxException("Ill-formed Unicode locale attribute: " + attribute);
        }
        if (this._uattributes != null) {
            this._uattributes.remove(new CaseInsensitiveString(attribute));
        }
        return this;
    }
    
    public InternalLocaleBuilder setUnicodeLocaleKeyword(final String key, final String type) throws LocaleSyntaxException {
        if (!UnicodeLocaleExtension.isKey(key)) {
            throw new LocaleSyntaxException("Ill-formed Unicode locale keyword key: " + key);
        }
        final CaseInsensitiveString cikey = new CaseInsensitiveString(key);
        if (type == null) {
            if (this._ukeywords != null) {
                this._ukeywords.remove(cikey);
            }
        }
        else {
            if (type.length() != 0) {
                final String tp = type.replaceAll("_", "-");
                final StringTokenIterator itr = new StringTokenIterator(tp, "-");
                while (!itr.isDone()) {
                    final String s = itr.current();
                    if (!UnicodeLocaleExtension.isTypeSubtag(s)) {
                        throw new LocaleSyntaxException("Ill-formed Unicode locale keyword type: " + type, itr.currentStart());
                    }
                    itr.next();
                }
            }
            if (this._ukeywords == null) {
                this._ukeywords = new HashMap<CaseInsensitiveString, String>(4);
            }
            this._ukeywords.put(cikey, type);
        }
        return this;
    }
    
    public InternalLocaleBuilder setExtension(final char singleton, final String value) throws LocaleSyntaxException {
        final boolean isBcpPrivateuse = LanguageTag.isPrivateusePrefixChar(singleton);
        if (!isBcpPrivateuse && !LanguageTag.isExtensionSingletonChar(singleton)) {
            throw new LocaleSyntaxException("Ill-formed extension key: " + singleton);
        }
        final boolean remove = value == null || value.length() == 0;
        final CaseInsensitiveChar key = new CaseInsensitiveChar(singleton);
        if (remove) {
            if (UnicodeLocaleExtension.isSingletonChar(key.value())) {
                if (this._uattributes != null) {
                    this._uattributes.clear();
                }
                if (this._ukeywords != null) {
                    this._ukeywords.clear();
                }
            }
            else if (this._extensions != null && this._extensions.containsKey(key)) {
                this._extensions.remove(key);
            }
        }
        else {
            final String val = value.replaceAll("_", "-");
            final StringTokenIterator itr = new StringTokenIterator(val, "-");
            while (!itr.isDone()) {
                final String s = itr.current();
                boolean validSubtag;
                if (isBcpPrivateuse) {
                    validSubtag = LanguageTag.isPrivateuseSubtag(s);
                }
                else {
                    validSubtag = LanguageTag.isExtensionSubtag(s);
                }
                if (!validSubtag) {
                    throw new LocaleSyntaxException("Ill-formed extension value: " + s, itr.currentStart());
                }
                itr.next();
            }
            if (UnicodeLocaleExtension.isSingletonChar(key.value())) {
                this.setUnicodeLocaleExtension(val);
            }
            else {
                if (this._extensions == null) {
                    this._extensions = new HashMap<CaseInsensitiveChar, String>(4);
                }
                this._extensions.put(key, val);
            }
        }
        return this;
    }
    
    public InternalLocaleBuilder setExtensions(String subtags) throws LocaleSyntaxException {
        if (subtags == null || subtags.length() == 0) {
            this.clearExtensions();
            return this;
        }
        subtags = subtags.replaceAll("_", "-");
        final StringTokenIterator itr = new StringTokenIterator(subtags, "-");
        List<String> extensions = null;
        String privateuse = null;
        int parsed = 0;
        while (!itr.isDone()) {
            String s = itr.current();
            if (!LanguageTag.isExtensionSingleton(s)) {
                break;
            }
            final int start = itr.currentStart();
            final String singleton = s;
            final StringBuilder sb = new StringBuilder(singleton);
            itr.next();
            while (!itr.isDone()) {
                s = itr.current();
                if (!LanguageTag.isExtensionSubtag(s)) {
                    break;
                }
                sb.append("-").append(s);
                parsed = itr.currentEnd();
                itr.next();
            }
            if (parsed < start) {
                throw new LocaleSyntaxException("Incomplete extension '" + singleton + "'", start);
            }
            if (extensions == null) {
                extensions = new ArrayList<String>(4);
            }
            extensions.add(sb.toString());
        }
        if (!itr.isDone()) {
            String s = itr.current();
            if (LanguageTag.isPrivateusePrefix(s)) {
                final int start = itr.currentStart();
                final StringBuilder sb2 = new StringBuilder(s);
                itr.next();
                while (!itr.isDone()) {
                    s = itr.current();
                    if (!LanguageTag.isPrivateuseSubtag(s)) {
                        break;
                    }
                    sb2.append("-").append(s);
                    parsed = itr.currentEnd();
                    itr.next();
                }
                if (parsed <= start) {
                    throw new LocaleSyntaxException("Incomplete privateuse:" + subtags.substring(start), start);
                }
                privateuse = sb2.toString();
            }
        }
        if (!itr.isDone()) {
            throw new LocaleSyntaxException("Ill-formed extension subtags:" + subtags.substring(itr.currentStart()), itr.currentStart());
        }
        return this.setExtensions(extensions, privateuse);
    }
    
    private InternalLocaleBuilder setExtensions(final List<String> bcpExtensions, final String privateuse) {
        this.clearExtensions();
        if (bcpExtensions != null && bcpExtensions.size() > 0) {
            final HashSet<CaseInsensitiveChar> processedExtensions = new HashSet<CaseInsensitiveChar>(bcpExtensions.size());
            for (final String bcpExt : bcpExtensions) {
                final CaseInsensitiveChar key = new CaseInsensitiveChar(bcpExt.charAt(0));
                if (!processedExtensions.contains(key)) {
                    if (UnicodeLocaleExtension.isSingletonChar(key.value())) {
                        this.setUnicodeLocaleExtension(bcpExt.substring(2));
                    }
                    else {
                        if (this._extensions == null) {
                            this._extensions = new HashMap<CaseInsensitiveChar, String>(4);
                        }
                        this._extensions.put(key, bcpExt.substring(2));
                    }
                }
            }
        }
        if (privateuse != null && privateuse.length() > 0) {
            if (this._extensions == null) {
                this._extensions = new HashMap<CaseInsensitiveChar, String>(1);
            }
            this._extensions.put(new CaseInsensitiveChar(privateuse.charAt(0)), privateuse.substring(2));
        }
        return this;
    }
    
    public InternalLocaleBuilder setLanguageTag(final LanguageTag langtag) {
        this.clear();
        if (langtag.getExtlangs().size() > 0) {
            this._language = langtag.getExtlangs().get(0);
        }
        else {
            final String language = langtag.getLanguage();
            if (!language.equals(LanguageTag.UNDETERMINED)) {
                this._language = language;
            }
        }
        this._script = langtag.getScript();
        this._region = langtag.getRegion();
        final List<String> bcpVariants = langtag.getVariants();
        if (bcpVariants.size() > 0) {
            final StringBuilder var = new StringBuilder(bcpVariants.get(0));
            for (int i = 1; i < bcpVariants.size(); ++i) {
                var.append("_").append(bcpVariants.get(i));
            }
            this._variant = var.toString();
        }
        this.setExtensions(langtag.getExtensions(), langtag.getPrivateuse());
        return this;
    }
    
    public InternalLocaleBuilder setLocale(final BaseLocale base, final LocaleExtensions extensions) throws LocaleSyntaxException {
        final String language = base.getLanguage();
        final String script = base.getScript();
        final String region = base.getRegion();
        final String variant = base.getVariant();
        if (language.length() > 0 && !LanguageTag.isLanguage(language)) {
            throw new LocaleSyntaxException("Ill-formed language: " + language);
        }
        if (script.length() > 0 && !LanguageTag.isScript(script)) {
            throw new LocaleSyntaxException("Ill-formed script: " + script);
        }
        if (region.length() > 0 && !LanguageTag.isRegion(region)) {
            throw new LocaleSyntaxException("Ill-formed region: " + region);
        }
        if (variant.length() > 0) {
            final int errIdx = this.checkVariants(variant, "_");
            if (errIdx != -1) {
                throw new LocaleSyntaxException("Ill-formed variant: " + variant, errIdx);
            }
        }
        this._language = language;
        this._script = script;
        this._region = region;
        this._variant = variant;
        this.clearExtensions();
        final Set<Character> extKeys = (extensions == null) ? null : extensions.getKeys();
        if (extKeys != null) {
            for (final Character key : extKeys) {
                final Extension e = extensions.getExtension(key);
                if (e instanceof UnicodeLocaleExtension) {
                    final UnicodeLocaleExtension ue = (UnicodeLocaleExtension)e;
                    for (final String uatr : ue.getUnicodeLocaleAttributes()) {
                        if (this._uattributes == null) {
                            this._uattributes = new HashSet<CaseInsensitiveString>(4);
                        }
                        this._uattributes.add(new CaseInsensitiveString(uatr));
                    }
                    for (final String ukey : ue.getUnicodeLocaleKeys()) {
                        if (this._ukeywords == null) {
                            this._ukeywords = new HashMap<CaseInsensitiveString, String>(4);
                        }
                        this._ukeywords.put(new CaseInsensitiveString(ukey), ue.getUnicodeLocaleType(ukey));
                    }
                }
                else {
                    if (this._extensions == null) {
                        this._extensions = new HashMap<CaseInsensitiveChar, String>(4);
                    }
                    this._extensions.put(new CaseInsensitiveChar(key), e.getValue());
                }
            }
        }
        return this;
    }
    
    public InternalLocaleBuilder clear() {
        this._language = "";
        this._script = "";
        this._region = "";
        this._variant = "";
        this.clearExtensions();
        return this;
    }
    
    public InternalLocaleBuilder clearExtensions() {
        if (this._extensions != null) {
            this._extensions.clear();
        }
        if (this._uattributes != null) {
            this._uattributes.clear();
        }
        if (this._ukeywords != null) {
            this._ukeywords.clear();
        }
        return this;
    }
    
    public BaseLocale getBaseLocale() {
        final String language = this._language;
        final String script = this._script;
        final String region = this._region;
        String variant = this._variant;
        if (this._extensions != null) {
            final String privuse = this._extensions.get(InternalLocaleBuilder.PRIVUSE_KEY);
            if (privuse != null) {
                final StringTokenIterator itr = new StringTokenIterator(privuse, "-");
                boolean sawPrefix = false;
                int privVarStart = -1;
                while (!itr.isDone()) {
                    if (sawPrefix) {
                        privVarStart = itr.currentStart();
                        break;
                    }
                    if (AsciiUtil.caseIgnoreMatch(itr.current(), "lvariant")) {
                        sawPrefix = true;
                    }
                    itr.next();
                }
                if (privVarStart != -1) {
                    final StringBuilder sb = new StringBuilder(variant);
                    if (sb.length() != 0) {
                        sb.append("_");
                    }
                    sb.append(privuse.substring(privVarStart).replaceAll("-", "_"));
                    variant = sb.toString();
                }
            }
        }
        return BaseLocale.getInstance(language, script, region, variant);
    }
    
    public LocaleExtensions getLocaleExtensions() {
        if ((this._extensions == null || this._extensions.size() == 0) && (this._uattributes == null || this._uattributes.size() == 0) && (this._ukeywords == null || this._ukeywords.size() == 0)) {
            return LocaleExtensions.EMPTY_EXTENSIONS;
        }
        return new LocaleExtensions(this._extensions, this._uattributes, this._ukeywords);
    }
    
    static String removePrivateuseVariant(final String privuseVal) {
        final StringTokenIterator itr = new StringTokenIterator(privuseVal, "-");
        int prefixStart = -1;
        boolean sawPrivuseVar = false;
        while (!itr.isDone()) {
            if (prefixStart != -1) {
                sawPrivuseVar = true;
                break;
            }
            if (AsciiUtil.caseIgnoreMatch(itr.current(), "lvariant")) {
                prefixStart = itr.currentStart();
            }
            itr.next();
        }
        if (!sawPrivuseVar) {
            return privuseVal;
        }
        assert prefixStart > 1;
        return (prefixStart == 0) ? null : privuseVal.substring(0, prefixStart - 1);
    }
    
    private int checkVariants(final String variants, final String sep) {
        final StringTokenIterator itr = new StringTokenIterator(variants, sep);
        while (!itr.isDone()) {
            final String s = itr.current();
            if (!LanguageTag.isVariant(s)) {
                return itr.currentStart();
            }
            itr.next();
        }
        return -1;
    }
    
    private void setUnicodeLocaleExtension(final String subtags) {
        if (this._uattributes != null) {
            this._uattributes.clear();
        }
        if (this._ukeywords != null) {
            this._ukeywords.clear();
        }
        final StringTokenIterator itr = new StringTokenIterator(subtags, "-");
        while (!itr.isDone() && UnicodeLocaleExtension.isAttribute(itr.current())) {
            if (this._uattributes == null) {
                this._uattributes = new HashSet<CaseInsensitiveString>(4);
            }
            this._uattributes.add(new CaseInsensitiveString(itr.current()));
            itr.next();
        }
        CaseInsensitiveString key = null;
        int typeStart = -1;
        int typeEnd = -1;
        while (!itr.isDone()) {
            if (key != null) {
                if (UnicodeLocaleExtension.isKey(itr.current())) {
                    assert typeEnd != -1;
                    final String type = (typeStart == -1) ? "" : subtags.substring(typeStart, typeEnd);
                    if (this._ukeywords == null) {
                        this._ukeywords = new HashMap<CaseInsensitiveString, String>(4);
                    }
                    this._ukeywords.put(key, type);
                    final CaseInsensitiveString tmpKey = new CaseInsensitiveString(itr.current());
                    key = (this._ukeywords.containsKey(tmpKey) ? null : tmpKey);
                    typeEnd = (typeStart = -1);
                }
                else {
                    if (typeStart == -1) {
                        typeStart = itr.currentStart();
                    }
                    typeEnd = itr.currentEnd();
                }
            }
            else if (UnicodeLocaleExtension.isKey(itr.current())) {
                key = new CaseInsensitiveString(itr.current());
                if (this._ukeywords != null && this._ukeywords.containsKey(key)) {
                    key = null;
                }
            }
            if (!itr.hasNext()) {
                if (key == null) {
                    break;
                }
                assert typeEnd != -1;
                final String type = (typeStart == -1) ? "" : subtags.substring(typeStart, typeEnd);
                if (this._ukeywords == null) {
                    this._ukeywords = new HashMap<CaseInsensitiveString, String>(4);
                }
                this._ukeywords.put(key, type);
                break;
            }
            else {
                itr.next();
            }
        }
    }
    
    static {
        PRIVUSE_KEY = new CaseInsensitiveChar("x".charAt(0));
    }
    
    static class CaseInsensitiveString
    {
        private String _s;
        
        CaseInsensitiveString(final String s) {
            this._s = s;
        }
        
        public String value() {
            return this._s;
        }
        
        @Override
        public int hashCode() {
            return AsciiUtil.toLowerString(this._s).hashCode();
        }
        
        @Override
        public boolean equals(final Object obj) {
            return this == obj || (obj instanceof CaseInsensitiveString && AsciiUtil.caseIgnoreMatch(this._s, ((CaseInsensitiveString)obj).value()));
        }
    }
    
    static class CaseInsensitiveChar
    {
        private char _c;
        
        CaseInsensitiveChar(final char c) {
            this._c = c;
        }
        
        public char value() {
            return this._c;
        }
        
        @Override
        public int hashCode() {
            return AsciiUtil.toLower(this._c);
        }
        
        @Override
        public boolean equals(final Object obj) {
            return this == obj || (obj instanceof CaseInsensitiveChar && this._c == AsciiUtil.toLower(((CaseInsensitiveChar)obj).value()));
        }
    }
}
