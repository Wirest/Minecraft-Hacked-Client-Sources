// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.locale;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.List;

public class LanguageTag
{
    private static final boolean JDKIMPL = false;
    public static final String SEP = "-";
    public static final String PRIVATEUSE = "x";
    public static String UNDETERMINED;
    public static final String PRIVUSE_VARIANT_PREFIX = "lvariant";
    private String _language;
    private String _script;
    private String _region;
    private String _privateuse;
    private List<String> _extlangs;
    private List<String> _variants;
    private List<String> _extensions;
    private static final Map<AsciiUtil.CaseInsensitiveKey, String[]> GRANDFATHERED;
    
    private LanguageTag() {
        this._language = "";
        this._script = "";
        this._region = "";
        this._privateuse = "";
        this._extlangs = Collections.emptyList();
        this._variants = Collections.emptyList();
        this._extensions = Collections.emptyList();
    }
    
    public static LanguageTag parse(final String languageTag, ParseStatus sts) {
        if (sts == null) {
            sts = new ParseStatus();
        }
        else {
            sts.reset();
        }
        final String[] gfmap = LanguageTag.GRANDFATHERED.get(new AsciiUtil.CaseInsensitiveKey(languageTag));
        StringTokenIterator itr;
        if (gfmap != null) {
            itr = new StringTokenIterator(gfmap[1], "-");
        }
        else {
            itr = new StringTokenIterator(languageTag, "-");
        }
        final LanguageTag tag = new LanguageTag();
        if (tag.parseLanguage(itr, sts)) {
            tag.parseExtlangs(itr, sts);
            tag.parseScript(itr, sts);
            tag.parseRegion(itr, sts);
            tag.parseVariants(itr, sts);
            tag.parseExtensions(itr, sts);
        }
        tag.parsePrivateuse(itr, sts);
        if (!itr.isDone() && !sts.isError()) {
            final String s = itr.current();
            sts._errorIndex = itr.currentStart();
            if (s.length() == 0) {
                sts._errorMsg = "Empty subtag";
            }
            else {
                sts._errorMsg = "Invalid subtag: " + s;
            }
        }
        return tag;
    }
    
    private boolean parseLanguage(final StringTokenIterator itr, final ParseStatus sts) {
        if (itr.isDone() || sts.isError()) {
            return false;
        }
        boolean found = false;
        final String s = itr.current();
        if (isLanguage(s)) {
            found = true;
            this._language = s;
            sts._parseLength = itr.currentEnd();
            itr.next();
        }
        return found;
    }
    
    private boolean parseExtlangs(final StringTokenIterator itr, final ParseStatus sts) {
        if (itr.isDone() || sts.isError()) {
            return false;
        }
        boolean found = false;
        while (!itr.isDone()) {
            final String s = itr.current();
            if (!isExtlang(s)) {
                break;
            }
            found = true;
            if (this._extlangs.isEmpty()) {
                this._extlangs = new ArrayList<String>(3);
            }
            this._extlangs.add(s);
            sts._parseLength = itr.currentEnd();
            itr.next();
            if (this._extlangs.size() == 3) {
                break;
            }
        }
        return found;
    }
    
    private boolean parseScript(final StringTokenIterator itr, final ParseStatus sts) {
        if (itr.isDone() || sts.isError()) {
            return false;
        }
        boolean found = false;
        final String s = itr.current();
        if (isScript(s)) {
            found = true;
            this._script = s;
            sts._parseLength = itr.currentEnd();
            itr.next();
        }
        return found;
    }
    
    private boolean parseRegion(final StringTokenIterator itr, final ParseStatus sts) {
        if (itr.isDone() || sts.isError()) {
            return false;
        }
        boolean found = false;
        final String s = itr.current();
        if (isRegion(s)) {
            found = true;
            this._region = s;
            sts._parseLength = itr.currentEnd();
            itr.next();
        }
        return found;
    }
    
    private boolean parseVariants(final StringTokenIterator itr, final ParseStatus sts) {
        if (itr.isDone() || sts.isError()) {
            return false;
        }
        boolean found = false;
        while (!itr.isDone()) {
            final String s = itr.current();
            if (!isVariant(s)) {
                break;
            }
            found = true;
            if (this._variants.isEmpty()) {
                this._variants = new ArrayList<String>(3);
            }
            this._variants.add(s);
            sts._parseLength = itr.currentEnd();
            itr.next();
        }
        return found;
    }
    
    private boolean parseExtensions(final StringTokenIterator itr, final ParseStatus sts) {
        if (itr.isDone() || sts.isError()) {
            return false;
        }
        boolean found = false;
        while (!itr.isDone()) {
            String s = itr.current();
            if (!isExtensionSingleton(s)) {
                break;
            }
            final int start = itr.currentStart();
            final String singleton = s;
            final StringBuilder sb = new StringBuilder(singleton);
            itr.next();
            while (!itr.isDone()) {
                s = itr.current();
                if (!isExtensionSubtag(s)) {
                    break;
                }
                sb.append("-").append(s);
                sts._parseLength = itr.currentEnd();
                itr.next();
            }
            if (sts._parseLength <= start) {
                sts._errorIndex = start;
                sts._errorMsg = "Incomplete extension '" + singleton + "'";
                break;
            }
            if (this._extensions.size() == 0) {
                this._extensions = new ArrayList<String>(4);
            }
            this._extensions.add(sb.toString());
            found = true;
        }
        return found;
    }
    
    private boolean parsePrivateuse(final StringTokenIterator itr, final ParseStatus sts) {
        if (itr.isDone() || sts.isError()) {
            return false;
        }
        boolean found = false;
        String s = itr.current();
        if (isPrivateusePrefix(s)) {
            final int start = itr.currentStart();
            final StringBuilder sb = new StringBuilder(s);
            itr.next();
            while (!itr.isDone()) {
                s = itr.current();
                if (!isPrivateuseSubtag(s)) {
                    break;
                }
                sb.append("-").append(s);
                sts._parseLength = itr.currentEnd();
                itr.next();
            }
            if (sts._parseLength <= start) {
                sts._errorIndex = start;
                sts._errorMsg = "Incomplete privateuse";
            }
            else {
                this._privateuse = sb.toString();
                found = true;
            }
        }
        return found;
    }
    
    public static LanguageTag parseLocale(final BaseLocale baseLocale, final LocaleExtensions localeExtensions) {
        final LanguageTag tag = new LanguageTag();
        String language = baseLocale.getLanguage();
        final String script = baseLocale.getScript();
        final String region = baseLocale.getRegion();
        final String variant = baseLocale.getVariant();
        boolean hasSubtag = false;
        String privuseVar = null;
        if (language.length() > 0 && isLanguage(language)) {
            if (language.equals("iw")) {
                language = "he";
            }
            else if (language.equals("ji")) {
                language = "yi";
            }
            else if (language.equals("in")) {
                language = "id";
            }
            tag._language = language;
        }
        if (script.length() > 0 && isScript(script)) {
            tag._script = canonicalizeScript(script);
            hasSubtag = true;
        }
        if (region.length() > 0 && isRegion(region)) {
            tag._region = canonicalizeRegion(region);
            hasSubtag = true;
        }
        if (variant.length() > 0) {
            List<String> variants = null;
            final StringTokenIterator varitr = new StringTokenIterator(variant, "_");
            while (!varitr.isDone()) {
                final String var = varitr.current();
                if (!isVariant(var)) {
                    break;
                }
                if (variants == null) {
                    variants = new ArrayList<String>();
                }
                variants.add(canonicalizeVariant(var));
                varitr.next();
            }
            if (variants != null) {
                tag._variants = variants;
                hasSubtag = true;
            }
            if (!varitr.isDone()) {
                final StringBuilder buf = new StringBuilder();
                while (!varitr.isDone()) {
                    String prvv = varitr.current();
                    if (!isPrivateuseSubtag(prvv)) {
                        break;
                    }
                    if (buf.length() > 0) {
                        buf.append("-");
                    }
                    prvv = AsciiUtil.toLowerString(prvv);
                    buf.append(prvv);
                    varitr.next();
                }
                if (buf.length() > 0) {
                    privuseVar = buf.toString();
                }
            }
        }
        List<String> extensions = null;
        String privateuse = null;
        final Set<Character> locextKeys = localeExtensions.getKeys();
        for (final Character locextKey : locextKeys) {
            final Extension ext = localeExtensions.getExtension(locextKey);
            if (isPrivateusePrefixChar(locextKey)) {
                privateuse = ext.getValue();
            }
            else {
                if (extensions == null) {
                    extensions = new ArrayList<String>();
                }
                extensions.add(locextKey.toString() + "-" + ext.getValue());
            }
        }
        if (extensions != null) {
            tag._extensions = extensions;
            hasSubtag = true;
        }
        if (privuseVar != null) {
            if (privateuse == null) {
                privateuse = "lvariant-" + privuseVar;
            }
            else {
                privateuse = privateuse + "-" + "lvariant" + "-" + privuseVar.replace("_", "-");
            }
        }
        if (privateuse != null) {
            tag._privateuse = privateuse;
        }
        if (tag._language.length() == 0 && (hasSubtag || privateuse == null)) {
            tag._language = LanguageTag.UNDETERMINED;
        }
        return tag;
    }
    
    public String getLanguage() {
        return this._language;
    }
    
    public List<String> getExtlangs() {
        return Collections.unmodifiableList((List<? extends String>)this._extlangs);
    }
    
    public String getScript() {
        return this._script;
    }
    
    public String getRegion() {
        return this._region;
    }
    
    public List<String> getVariants() {
        return Collections.unmodifiableList((List<? extends String>)this._variants);
    }
    
    public List<String> getExtensions() {
        return Collections.unmodifiableList((List<? extends String>)this._extensions);
    }
    
    public String getPrivateuse() {
        return this._privateuse;
    }
    
    public static boolean isLanguage(final String s) {
        return s.length() >= 2 && s.length() <= 8 && AsciiUtil.isAlphaString(s);
    }
    
    public static boolean isExtlang(final String s) {
        return s.length() == 3 && AsciiUtil.isAlphaString(s);
    }
    
    public static boolean isScript(final String s) {
        return s.length() == 4 && AsciiUtil.isAlphaString(s);
    }
    
    public static boolean isRegion(final String s) {
        return (s.length() == 2 && AsciiUtil.isAlphaString(s)) || (s.length() == 3 && AsciiUtil.isNumericString(s));
    }
    
    public static boolean isVariant(final String s) {
        final int len = s.length();
        if (len >= 5 && len <= 8) {
            return AsciiUtil.isAlphaNumericString(s);
        }
        return len == 4 && AsciiUtil.isNumeric(s.charAt(0)) && AsciiUtil.isAlphaNumeric(s.charAt(1)) && AsciiUtil.isAlphaNumeric(s.charAt(2)) && AsciiUtil.isAlphaNumeric(s.charAt(3));
    }
    
    public static boolean isExtensionSingleton(final String s) {
        return s.length() == 1 && AsciiUtil.isAlphaString(s) && !AsciiUtil.caseIgnoreMatch("x", s);
    }
    
    public static boolean isExtensionSingletonChar(final char c) {
        return isExtensionSingleton(String.valueOf(c));
    }
    
    public static boolean isExtensionSubtag(final String s) {
        return s.length() >= 2 && s.length() <= 8 && AsciiUtil.isAlphaNumericString(s);
    }
    
    public static boolean isPrivateusePrefix(final String s) {
        return s.length() == 1 && AsciiUtil.caseIgnoreMatch("x", s);
    }
    
    public static boolean isPrivateusePrefixChar(final char c) {
        return AsciiUtil.caseIgnoreMatch("x", String.valueOf(c));
    }
    
    public static boolean isPrivateuseSubtag(final String s) {
        return s.length() >= 1 && s.length() <= 8 && AsciiUtil.isAlphaNumericString(s);
    }
    
    public static String canonicalizeLanguage(final String s) {
        return AsciiUtil.toLowerString(s);
    }
    
    public static String canonicalizeExtlang(final String s) {
        return AsciiUtil.toLowerString(s);
    }
    
    public static String canonicalizeScript(final String s) {
        return AsciiUtil.toTitleString(s);
    }
    
    public static String canonicalizeRegion(final String s) {
        return AsciiUtil.toUpperString(s);
    }
    
    public static String canonicalizeVariant(final String s) {
        return AsciiUtil.toLowerString(s);
    }
    
    public static String canonicalizeExtension(final String s) {
        return AsciiUtil.toLowerString(s);
    }
    
    public static String canonicalizeExtensionSingleton(final String s) {
        return AsciiUtil.toLowerString(s);
    }
    
    public static String canonicalizeExtensionSubtag(final String s) {
        return AsciiUtil.toLowerString(s);
    }
    
    public static String canonicalizePrivateuse(final String s) {
        return AsciiUtil.toLowerString(s);
    }
    
    public static String canonicalizePrivateuseSubtag(final String s) {
        return AsciiUtil.toLowerString(s);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        if (this._language.length() > 0) {
            sb.append(this._language);
            for (final String extlang : this._extlangs) {
                sb.append("-").append(extlang);
            }
            if (this._script.length() > 0) {
                sb.append("-").append(this._script);
            }
            if (this._region.length() > 0) {
                sb.append("-").append(this._region);
            }
            for (final String variant : this._extlangs) {
                sb.append("-").append(variant);
            }
            for (final String extension : this._extensions) {
                sb.append("-").append(extension);
            }
        }
        if (this._privateuse.length() > 0) {
            if (sb.length() > 0) {
                sb.append("-");
            }
            sb.append(this._privateuse);
        }
        return sb.toString();
    }
    
    static {
        LanguageTag.UNDETERMINED = "und";
        GRANDFATHERED = new HashMap<AsciiUtil.CaseInsensitiveKey, String[]>();
        final String[][] arr$;
        final String[][] entries = arr$ = new String[][] { { "art-lojban", "jbo" }, { "cel-gaulish", "xtg-x-cel-gaulish" }, { "en-GB-oed", "en-GB-x-oed" }, { "i-ami", "ami" }, { "i-bnn", "bnn" }, { "i-default", "en-x-i-default" }, { "i-enochian", "und-x-i-enochian" }, { "i-hak", "hak" }, { "i-klingon", "tlh" }, { "i-lux", "lb" }, { "i-mingo", "see-x-i-mingo" }, { "i-navajo", "nv" }, { "i-pwn", "pwn" }, { "i-tao", "tao" }, { "i-tay", "tay" }, { "i-tsu", "tsu" }, { "no-bok", "nb" }, { "no-nyn", "nn" }, { "sgn-BE-FR", "sfb" }, { "sgn-BE-NL", "vgt" }, { "sgn-CH-DE", "sgg" }, { "zh-guoyu", "cmn" }, { "zh-hakka", "hak" }, { "zh-min", "nan-x-zh-min" }, { "zh-min-nan", "nan" }, { "zh-xiang", "hsn" } };
        for (final String[] e : arr$) {
            LanguageTag.GRANDFATHERED.put(new AsciiUtil.CaseInsensitiveKey(e[0]), e);
        }
    }
}
